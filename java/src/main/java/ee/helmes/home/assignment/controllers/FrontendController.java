package ee.helmes.home.assignment.controllers;

import ee.helmes.home.assignment.dto.CategoryDTO;
import ee.helmes.home.assignment.dto.mapper.CategoryDTOMapper;
import ee.helmes.home.assignment.enums.CustomHttpSessionAttributes;
import ee.helmes.home.assignment.models.Category;
import ee.helmes.home.assignment.models.UserData;
import ee.helmes.home.assignment.services.CategoryService;
import ee.helmes.home.assignment.services.UserDataService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class FrontendController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FrontendController.class);

    private final CategoryService categoryService;
    private final UserDataService userDataService;

    @Autowired
    public FrontendController(CategoryService categoryService, UserDataService userDataService) {
        this.categoryService = categoryService;
        this.userDataService = userDataService;
    }


    @GetMapping("/form")
    public String showForm(Model model, HttpSession httpSession) {
        UserData userData;
        List<CategoryDTO> categoryEntries = new CategoryDTOMapper(categoryService)
                .getOrganisedCategoryDTOsForTemplate();
        Long recordId = (Long) httpSession.getAttribute(CustomHttpSessionAttributes.SUBMITTED_USER_DATA_ID.name());


        if (recordId == null) {
            LOGGER.debug("Did not find any previously submitted data for current HTTP session");
            userData = new UserData();
        } else {
            Optional<UserData> userDataById = userDataService.getUserDataById(recordId);
            userData = userDataById.orElse(new UserData());
            LOGGER.info("Got recordID param. Found data: {}", userData);
        }
        model.addAttribute("categories", categoryEntries);
        model.addAttribute("idsOfSelectedCategories", userData.getIdsOfInvolvedSectors());
        model.addAttribute("registrationForm", userData);

        return "categories";
    }

    @PostMapping("/userData")
    public String userDataSubmit(@ModelAttribute UserData userData, Model model, HttpSession httpSession) {
        LOGGER.info("Got UserData form submission from Thymeleaf frontend: {}", userData);
        userData.setId((Long) httpSession.getAttribute(CustomHttpSessionAttributes.SUBMITTED_USER_DATA_ID.name()));

        userData.getIdsOfInvolvedSectors().forEach(id -> {
            Optional<Category> involvedCategory = categoryService.getCategoryById(id);

            if (involvedCategory.isEmpty()) {
                LOGGER.warn("Invalid category ID '{}': category not found", id);
            }
        });

        LOGGER.debug("Saving the userData");
        Optional<Long> savedRecordId = userDataService.validateAndSaveUserData(userData);
        if (savedRecordId.isPresent()) {
            Long recordId = savedRecordId.get();
            LOGGER.info("Successfully saved user data. ID: {}", recordId);
            httpSession.setAttribute(CustomHttpSessionAttributes.SUBMITTED_USER_DATA_ID.name(), recordId);
        }

        return "redirect:/form";
    }

//    @GetMapping("/json")
//    public ResponseEntity<String> getOrganisedCategories() {
//        List<CategoryDTO> categoryEntries = new CategoryDTOMapper(categoryService)
//                .getOrganisedCategoryDTOsForTemplate();
//
//        ObjectMapper mapper = new ObjectMapper();
//        String jsonResponse;
//        HttpStatus httpStatus;
//        try {
//            jsonResponse = mapper.writeValueAsString(categoryEntries);
//            httpStatus = HttpStatus.OK;
//        } catch (JsonProcessingException e) {
//            jsonResponse = "{\"error\": \"Failed to write organised categories as JSON\"}";
//            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//
//        return new ResponseEntity<>(jsonResponse, httpStatus);
//    }

//    private static class CategoryEntryForTemplateManager {
//        private final List<Category> parentCategories;
//        private final HashMap<Integer, Category> categories;
//        private final HashMap<Integer, Set<Integer>> mapOfParentToChildrenIds;
//
//        public CategoryEntryForTemplateManager(CategoryService categoryService) {
//            parentCategories = categoryService.getParentCategories();
//            categories = categoryService.getCategoryIdToCategoryMap();
//            mapOfParentToChildrenIds = categoryService.getMapOfParentToChildrenIds();
//        }
//
//        public List<CategoryEntryForTemplate> getOrganisedCategoryEntriesForTemplate() {
//            List<CategoryEntryForTemplate> categories = new ArrayList<>();
//
//            for (Category parentCategory : parentCategories) {
//                CategoryEntryForTemplate templateCategory = new CategoryEntryForTemplate(parentCategory);
//                categories.add(getCategoryEntryWithSetupChildren(templateCategory));
//            }
//
//            return categories;
//        }
//
//        private CategoryEntryForTemplate getCategoryEntryWithSetupChildren(CategoryEntryForTemplate categoryEntry) {
//            if (!mapOfParentToChildrenIds.containsKey(categoryEntry.getCategory().getId())) {
//                // This is a leaf (the most low-level) category, so it does not have any subcategories => no actions are needed
//                return categoryEntry;
//            }
//
//            // fill children recursively
//            for (Integer subcategoryId : mapOfParentToChildrenIds.get(categoryEntry.getCategory().getId())) {
//                CategoryEntryForTemplate subcategoryEntry = getCategoryEntryWithSetupChildren(new CategoryEntryForTemplate(categories.get(subcategoryId)));
//                categoryEntry.getSubcategories().add(subcategoryEntry);
//            }
//            categoryEntry.getSubcategories().sort(new SortByCategoryNameAndMoveCategoryOtherToBottom());
//
//            return categoryEntry;
//        }
//
//        private static final class SortByCategoryNameAndMoveCategoryOtherToBottom implements Comparator<CategoryEntryForTemplate> {
//            private static final String OTHER_CATEGORY = "other";
//
//            @Override
//            public int compare(CategoryEntryForTemplate category1, CategoryEntryForTemplate category2) {
//                if (category1 == null || category2 == null) {
//                    throw new NullPointerException("Categories must not be null");
//                }
//
//                String name1 = Objects.requireNonNullElse(category1.getName(), "");
//                String name2 = Objects.requireNonNullElse(category2.getName(), "");
//
//                if (name1.equalsIgnoreCase(OTHER_CATEGORY)) {
//                    return 1;
//                }
//                if (name2.equalsIgnoreCase(OTHER_CATEGORY)) {
//                    return -1;
//                }
//
//                return name1.compareToIgnoreCase(name2);
//            }
//        }
//    }


//    private static class CategoryEntryForTemplate {
//        private final Category category;
//        private final List<CategoryEntryForTemplate> subcategories;
//
//        public CategoryEntryForTemplate(Category category) {
//            this.category = category;
//            subcategories = new ArrayList<>();
//        }
//
//        public Category getCategory() {
//            return category;
//        }
//
//        public List<CategoryEntryForTemplate> getSubcategories() {
//            return subcategories;
//        }
//
//        @JsonIgnore
//        public String getName() {
//            return getCategory().getName();
//        }
//
//        @JsonIgnore
//        public int getId() {
//            return getCategory().getId();
//        }
//    }

}
