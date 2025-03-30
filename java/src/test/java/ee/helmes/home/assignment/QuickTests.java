package ee.helmes.home.assignment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.helmes.home.assignment.enums.CustomHttpSessionAttributes;
import ee.helmes.home.assignment.models.Category;
import ee.helmes.home.assignment.models.UserData;
import ee.helmes.home.assignment.repos.CategoryRepository;
import ee.helmes.home.assignment.services.CategoryServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class QuickTests {

    private final List<Category> tuples = List.of(
//            new Tuple(2, null),
//            new Tuple(25, 2),
//            new Tuple(35, 2),
//            new Tuple(28, 2),
//            new Tuple(22, 2),
//            new Tuple(141, 2),
//            new Tuple(21, 2)
    );
    @Autowired
    private MockMvc MOCK_MVC;
    @Autowired
    private CategoryServiceImpl categoryService;
    private HashMap<Integer, Set<Integer>> idToChildIdSet = new HashMap<>();

    @Test
    public void testOrganization() {
        for (Category tuple : tuples) {
            addTupleToMap(tuple, idToChildIdSet);
        }

        for (Integer key : idToChildIdSet.keySet()) {
            System.out.println(key + ": " + idToChildIdSet.get(key));
        }
    }

    @Test
    public void runOverRealData() throws Exception {
        MvcResult mvcResult = MOCK_MVC.perform(MockMvcRequestBuilders.get("/categories"))
//                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Category> items = objectMapper.readValue(responseBody, new TypeReference<ArrayList<Category>>() {
        });
//        System.out.println(items);

        HashMap<Integer, Set<Integer>> idToChildIdSet1 = new HashMap<>();
        HashMap<Integer, String> idToNameMap = new HashMap<>();
        items.forEach(
                tuple -> {
                    addTupleToMap(tuple, idToChildIdSet1);
                    idToNameMap.put(tuple.getParentId(), tuple.getName());
                }
        );

        System.out.println(idToChildIdSet1);

        idToChildIdSet1.forEach(
                (id, childrenSet) -> {
                    System.out.println("%d (%s): ".formatted(id, idToNameMap.get(id)) + childrenSet);
                }
        );
    }


    private void addTupleToMap(Category tuple, HashMap<Integer, Set<Integer>> map) {
        if (tuple.getParentId() == null) {
            return;
        }

        if (!map.containsKey(tuple.getParentId())) {
            // initialize list
            map.put(tuple.getParentId(), new HashSet<>());
        }

        // get the list and add to the list of children IDs
        map.get(tuple.getParentId()).add(tuple.getId());
    }

    @Test
    public void testOptional() {
        Optional<String> empty = Optional.empty();
        Optional<String> valid = Optional.of("Valid value B)");

        System.out.println(empty.orElse("Plan B"));
        System.out.println(valid.orElse("Plan B"));
    }

    @Test
    public void testDataValidation() {
        UserData invalid = new UserData();
        UserData invalid2 = new UserData();
        invalid2.setName("");
        UserData invalid3 = new UserData();
        invalid3.setName("  ");
        UserData valid = new UserData();
        valid.setName("Some name");

        assertFalse(isUserDataValid(invalid));
        assertFalse(isUserDataValid(invalid2));
        assertFalse(isUserDataValid(invalid3));
        assertTrue(isUserDataValid(valid));
    }


    private boolean isUserDataValid(UserData userData) {
        return Optional.ofNullable(userData.getName())
                .filter(name -> !name.isBlank())
                .isPresent();
    }


    @Test
    public void tryPrintingCategories() {
        CategoryRepository mockCategoryRepository = Mockito.mock(CategoryRepository.class);
        when(mockCategoryRepository.findAll()).thenReturn(List.of(
                new Category(1, "parent"),
                new Category(11, "child1", 1),
                new Category(12, "child2", 1),
                new Category(13, "child3", 1),
                new Category(2, "parent2"),
                new Category(21, "child2.1", 2),
                new Category(22, "child2.2", 2),
                new Category(23, "child2.3", 2)
        ));

        CategoryServiceImpl mockCategoryService = new CategoryServiceImpl(mockCategoryRepository);
//        CategoryManager categoryManager = new CategoryManager(mockCategoryService);
        CategoryManager categoryManager = new CategoryManager(categoryService);

        categoryManager.printFormattedCategories();
        System.out.println("\n-------------------\n");
        categoryManager.printCategoryAndItsLevel();
    }


    private class CategoryManager {
        private final CategoryServiceImpl categoryService;

        private final List<Category> parentCategories;
        private final HashMap<Integer, Category> allCategories;
        private final HashMap<Integer, Set<Integer>> mapOfParentToChildrenIds;

        public CategoryManager(CategoryServiceImpl categoryService) {
            this.categoryService = categoryService;

            parentCategories = categoryService.getParentCategories();
            allCategories = categoryService.getCategoryIdToCategoryMap();
            mapOfParentToChildrenIds = categoryService.getMapOfParentToChildrenIds();
        }

        public void printFormattedCategories() {
            StringBuilder result = new StringBuilder("Formatted categories: \n");

            for (Category parentCategory : parentCategories) {
                result.append(getFormattedLineForCategoryEntry("", "-", parentCategory));
            }

            System.out.println("Final result is:\n\n" + result.toString());

        }

        private String getFormattedLineForCategoryEntry(String formattedCategories, String offset, Category parentCategory) {
            formattedCategories += getCategoryData(parentCategory, offset) + "\n";  // Print the parent before children are added
            if (mapOfParentToChildrenIds.containsKey(parentCategory.getId())) {
                // This category has some children, they need to be handled

                for (Integer childId : mapOfParentToChildrenIds.get(parentCategory.getId())) {
                    String formattedLineForCategoryEntry = getFormattedLineForCategoryEntry(formattedCategories, offset + offset, allCategories.get(childId));
                    formattedCategories = formattedLineForCategoryEntry;
                }
//                formattedCategories += " --------- \n";
            }


            return formattedCategories;
        }

        private String getCategoryData(Category category, String offset) {
            return "%s> %s: %s".formatted(offset, category.getId(), category.getName());
        }

        public void printCategoryAndItsLevel() {
            for (Integer categoryId : categoryService.getIdsOfParentCategories()) {
                printTreeForParentCategory(categoryId, 0);
            }
        }

        private void printTreeForParentCategory(int categoryId, int level) {
            System.out.println(getLine(categoryId, level));

            if (mapOfParentToChildrenIds.containsKey(categoryId)) {
                for (Integer childId : mapOfParentToChildrenIds.get(categoryId)) {
                    printTreeForParentCategory(childId, level + 1);
                }
            }
        }

        private String getLine(int categoryId, int level) {
            StringBuilder formatterString = new StringBuilder();
            String offsetCharacter = "-";

            for (int i = 0; i < level; i++) {
                formatterString.append(offsetCharacter);
            }

            formatterString.append(categoryId)
                    .append(": ")
                    .append(allCategories.get(categoryId).getName());

            return formatterString.toString();
        }

    }

}
