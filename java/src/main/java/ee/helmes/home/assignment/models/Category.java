package ee.helmes.home.assignment.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "categories")
public class Category {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Id
    private Integer id;
    private String name;
    @JoinColumn(name = "parent_id")  // Foreign key to parent category
    private Integer parentId;


    public Category() {}

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(int id, String name, int parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getParentId() {
        return parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;
        return Objects.equals(id, category.id)
               && Objects.equals(name, category.name)
               && Objects.equals(parentId, category.parentId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(parentId);
        return result;
    }

    @Override
    public String toString() {
        try {
            return OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"error\": \"failed to write category as JSON\"}";
        }
    }
}
