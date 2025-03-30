package ee.helmes.home.assignment.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    @Test
    public void testConversionToString() {
        System.out.println("Hi");

        Category manufacturing = new Category(1, "Manufacturing");
        Category constructionMaterials = new Category(19, "Construction materials", 1);

        System.out.println(constructionMaterials);

    }


}