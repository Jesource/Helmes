package ee.helmes.home.assignment.services;

import ee.helmes.home.assignment.models.Category;
import ee.helmes.home.assignment.repos.CategoryRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceImplTest {
    private final CategoryRepository mockCategoryRepository = mock(CategoryRepository.class);
    private final CategoryServiceImpl categoryService = new CategoryServiceImpl(mockCategoryRepository);

    @BeforeEach
    void setUp() {
        reset(mockCategoryRepository);
    }

    @Test
    public void givenCategories_whenRequestingForParentCategoryIds_thenCorrectIdsReturned() {
        when(mockCategoryRepository.findAll()).thenReturn(List.of(
                new Category(1, "parent"),
                new Category(11, "child1", 1),
                new Category(12, "child2", 1),
                new Category(121, "sub-child of child2", 12),
                new Category(13, "child3", 1),
                new Category(2, "parent2"),
                new Category(21, "child2.1", 2),
                new Category(22, "child2.2", 2),
                new Category(23, "child2.3", 2)
        ));

        List<Integer> expectedIdsOfParentCategories = List.of(1, 2);
        List<Integer> actualIdsOfParentCategories = categoryService.getIdsOfParentCategories();

        assertEquals(expectedIdsOfParentCategories, actualIdsOfParentCategories);
    }

    @Test
    public void givenCategories_whenRequestingAllCategories_thenAllCategoriesReturned() {
        when(mockCategoryRepository.findAll()).thenReturn(List.of(
                new Category(1, "parent"),
                new Category(12, "child2", 1),
                new Category(121, "sub-child of child2", 12)
        ));

        List<Category> allCategories = categoryService.getAllCategories();

        assertEquals(3, allCategories.size());
    }

    @Test
    public void givenParentId_whenRequestingForChildrenCategories_thenListOfChildrenReturned() {
        when(mockCategoryRepository.findAll()).thenReturn(List.of(
                new Category(1, "parent"),
                new Category(11, "child1", 1),
                new Category(12, "child2", 1),
                new Category(121, "sub-child of child2", 12),
                new Category(13, "child3", 1),
                new Category(2, "parent2"),
                new Category(21, "child2.1", 2),
                new Category(22, "child2.2", 2),
                new Category(23, "child2.3", 2)
        ));

        assertEquals(3, categoryService.getChildrenCategories(1).size());
        assertEquals(3, categoryService.getChildrenCategories(2).size());
        assertEquals(1, categoryService.getChildrenCategories(12).size());
    }

    @Test
    public void givenCategoryId_whenRequestingForCategory_thenCorrectCategoryReturned() {
        when(mockCategoryRepository.findAll()).thenReturn(List.of(
                new Category(1, "parent")
        ));

        Optional<Category> actualOptional = categoryService.getCategoryById(1);
        assertTrue(actualOptional.isPresent());

        Category expected = new Category(1, "parent");
        Category actual = actualOptional.get();
        assertEquals(expected, actual);
    }
    @Test
    public void givenInvalidCategoryId_whenRequestingForCategory_thenNothingReturned() {
        when(mockCategoryRepository.findAll()).thenReturn(List.of(
                new Category(1, "parent")
        ));

        Optional<Category> actualOptional = categoryService.getCategoryById(11);
        assertTrue(actualOptional.isEmpty());
    }


}