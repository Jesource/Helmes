package ee.helmes.home.assignment.services;

import ee.helmes.home.assignment.models.Category;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryService {
    List<Category> getAllCategories();
    List<Category> getChildrenCategories(int parentId);
    List<Category> getParentCategories();
    Optional<Category> getCategoryById(int categoryId);
    HashMap<Integer, Set<Integer>> getMapOfParentToChildrenIds();
    HashMap<Integer, Category> getCategoryIdToCategoryMap();
}
