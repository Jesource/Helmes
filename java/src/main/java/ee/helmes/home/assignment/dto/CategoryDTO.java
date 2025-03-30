package ee.helmes.home.assignment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ee.helmes.home.assignment.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDTO {
    private final Category category;
    private final List<CategoryDTO> subcategories;

    public CategoryDTO(Category category) {
        this.category = category;
        subcategories = new ArrayList<>();
    }

    public Category getCategory() {
        return category;
    }

    public List<CategoryDTO> getSubcategories() {
        return subcategories;
    }

    @JsonIgnore
    public String getName() {
        return getCategory().getName();
    }

    @JsonIgnore
    public int getId() {
        return getCategory().getId();
    }
}
