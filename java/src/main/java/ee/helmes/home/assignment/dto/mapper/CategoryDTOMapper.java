package ee.helmes.home.assignment.dto.mapper;

import ee.helmes.home.assignment.dto.CategoryDTO;
import ee.helmes.home.assignment.models.Category;
import ee.helmes.home.assignment.services.CategoryService;

import java.util.*;

public class CategoryDTOMapper {
    private final List<Category> parentCategories;
    private final HashMap<Integer, Category> categories;
    private final HashMap<Integer, Set<Integer>> mapOfParentToChildrenIds;

    public CategoryDTOMapper(CategoryService categoryService) {
        parentCategories = categoryService.getParentCategories();
        categories = categoryService.getCategoryIdToCategoryMap();
        mapOfParentToChildrenIds = categoryService.getMapOfParentToChildrenIds();
    }

    public List<CategoryDTO> getOrganisedCategoryDTOsForTemplate() {
        List<CategoryDTO> categories = new ArrayList<>();

        for (Category parentCategory : parentCategories) {
            CategoryDTO templateCategory = new CategoryDTO(parentCategory);
            categories.add(setupSubcategoriesForCategoryDTO(templateCategory));
        }

        return categories;
    }

    public CategoryDTO getCategoryDTOWithSetupChildrenForProvidedCategoryId(int categoryId) {
        if (!categories.containsKey(categoryId)) {
            throw new IllegalArgumentException("Provided category ID has associated no records");
        }
        Category category = categories.get(categoryId);

        return setupSubcategoriesForCategoryDTO(new CategoryDTO(category));
    }

    private CategoryDTO setupSubcategoriesForCategoryDTO(CategoryDTO categoryEntry) {
        if (!mapOfParentToChildrenIds.containsKey(categoryEntry.getCategory().getId())) {
            // This is a leaf (the most low-level) category, so it does not have any subcategories => no actions are needed
            return categoryEntry;
        }

        // fill children recursively
        for (Integer subcategoryId : mapOfParentToChildrenIds.get(categoryEntry.getCategory().getId())) {
            CategoryDTO subcategoryEntry = setupSubcategoriesForCategoryDTO(new CategoryDTO(categories.get(subcategoryId)));
            categoryEntry.getSubcategories().add(subcategoryEntry);
        }
        categoryEntry.getSubcategories().sort(new CategoryNameComparator());

        return categoryEntry;
    }

    private static final class CategoryNameComparator implements Comparator<CategoryDTO> {
        private static final String OTHER_CATEGORY = "other";

        @Override
        public int compare(CategoryDTO category1, CategoryDTO category2) {
            if (category1 == null || category2 == null) {
                throw new NullPointerException("Categories must not be null");
            }

            String name1 = Objects.requireNonNullElse(category1.getName(), "");
            String name2 = Objects.requireNonNullElse(category2.getName(), "");

            if (name1.equalsIgnoreCase(OTHER_CATEGORY)) {
                return 1;
            }
            if (name2.equalsIgnoreCase(OTHER_CATEGORY)) {
                return -1;
            }

            return name1.compareToIgnoreCase(name2);
        }
    }

}
