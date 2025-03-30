package ee.helmes.home.assignment.services;

import ee.helmes.home.assignment.models.Category;
import ee.helmes.home.assignment.repos.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final HashMap<Integer, Set<Integer>> mapOfParentCategoryIdToItsChildrenIds = new HashMap<>();
    private final HashMap<Integer, Category> cachedCategories = new HashMap<>();

    private long nextDbFetchTimestamp = 0;
    private final long DB_FETCH_INTERVAL_MS = 60_000;  // 60 seconds  TODO: move to config


    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        updateCachedCategoriesIfNeeded();

        return new ArrayList<>(cachedCategories.values());
    }

    @Override
    public List<Category> getChildrenCategories(int parentId) {
        updateCachedCategoriesIfNeeded();

        Set<Integer> idsOfChildCategories = mapOfParentCategoryIdToItsChildrenIds.get(parentId);
        List<Category> childCategories = new ArrayList<>(idsOfChildCategories.size());

        idsOfChildCategories.forEach(childCategoryId ->
                childCategories.add(cachedCategories.get(childCategoryId))
        );

        return childCategories;
    }

    @Override
    public List<Category> getParentCategories() {
        updateCachedCategoriesIfNeeded();

        return cachedCategories.values().stream()
                .filter(category -> category.getParentId() == null)
                .toList();
    }

    public List<Integer> getIdsOfParentCategories() {
        updateCachedCategoriesIfNeeded();

        return cachedCategories.values().stream()
                .filter(category -> category.getParentId() == null)
                .map(Category::getId)
                .toList();
    }


    @Override
    public Optional<Category> getCategoryById(int categoryId) {
        updateCachedCategoriesIfNeeded();

        return Optional.ofNullable(cachedCategories.get(categoryId));
    }

    @Override
    public HashMap<Integer, Set<Integer>> getMapOfParentToChildrenIds() {
        updateCachedCategoriesIfNeeded();

        return mapOfParentCategoryIdToItsChildrenIds;
    }

    @Override
    public HashMap<Integer, Category> getCategoryIdToCategoryMap() {
        updateCachedCategoriesIfNeeded();

        return cachedCategories;
    }

    private void updateCachedCategoriesIfNeeded() {
        if (!isUpdateNeeded()) {
            return;
        }

        updateCachedCategories();
    }


    private boolean isUpdateNeeded() {
        return nextDbFetchTimestamp < System.currentTimeMillis();
    }

    private void updateCachedCategories() {
        clearCachedCategories();
        fetchAndCacheCategories();
        updateNextDbFetchTimestamp();
    }

    private void updateNextDbFetchTimestamp() {
        nextDbFetchTimestamp = System.currentTimeMillis() + DB_FETCH_INTERVAL_MS;
    }

    private void clearCachedCategories() {
        cachedCategories.clear();
        mapOfParentCategoryIdToItsChildrenIds.clear();
    }

    public void fetchAndCacheCategories() {
        List<Category> categories = requestAllCategoriesFromDb();
        cacheCategories(categories);
    }

    private void cacheCategories(List<Category> categories) {
        categories.forEach(category -> {
            cachedCategories.put(category.getId(), category);
            registerCategoryAsSubcategoryIfNeeded(category);
        });
    }

    public List<Category> requestAllCategoriesFromDb() {
        return categoryRepository.findAll();
    }

    private void registerCategoryAsSubcategoryIfNeeded(Category category) {
        Integer parentCategoryId = category.getParentId();
        if (parentCategoryId == null) {
            return;
        }

        if (!mapOfParentCategoryIdToItsChildrenIds.containsKey(parentCategoryId)) {
            // initialize list
            mapOfParentCategoryIdToItsChildrenIds.put(parentCategoryId, new HashSet<>());
        }

        // get the list and add to the list of children IDs
        mapOfParentCategoryIdToItsChildrenIds
                .get(parentCategoryId)
                .add(category.getId());
    }
}
