package ee.helmes.home.assignment.repos;

import ee.helmes.home.assignment.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByParentIdIsNull();
    List<Category> findAllByParentId(int parentId);
}
