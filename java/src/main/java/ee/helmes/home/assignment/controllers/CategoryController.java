package ee.helmes.home.assignment.controllers;

import ee.helmes.home.assignment.models.Category;
import ee.helmes.home.assignment.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
//    private final CategoryService categoryService;
//
//    @Autowired
//    public CategoryController(CategoryService categoryService) {
//        this.categoryService = categoryService;
//    }
//
//    @GetMapping("")
//    public List<Category> listCategories() {
//        return categoryService.getAllCategories();
//    }
//
//    @GetMapping("/parent")
//    public ResponseEntity<Object> listParentCategories() {
//        List<Category> parentCategories = categoryService.getParentCategories();
//
//        return new ResponseEntity<>(parentCategories, HttpStatus.OK);
//    }
//
//    @GetMapping("/children/{parentId}")
//    public ResponseEntity<Object> getChildren(@PathVariable int parentId) {
//        List<Category> childCategories = categoryService.getChildrenCategories(parentId);
//
//        return new ResponseEntity<>(childCategories, HttpStatus.OK);
//    }
}
