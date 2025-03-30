package ee.helmes.home.assignment.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    private MockMvc MOCK_MVC;

    private static JsonNode getResourceAsJson(String nameOfFileWithExpectedResult) {
        try {
            URL resource = CategoryControllerTest.class.getClassLoader().getResource(nameOfFileWithExpectedResult);
            File resourceFile = new File(resource.toURI());

            return OBJECT_MAPPER.readTree(resourceFile);
        } catch (IOException e) {
            fail("Failed to read or parse JSON: " + e.getMessage());
        } catch (URISyntaxException e) {
            fail("Failed to locate file: " + e.getMessage());
        }

        return null;
    }

    private static void compareActualResponseToExpectedResponse(String actualResponseString, String nameOfFileWithExpectedResult) {
        try {
            JsonNode expected = getResourceAsJson(nameOfFileWithExpectedResult);
            JsonNode actual = OBJECT_MAPPER.readTree(actualResponseString);

            assertTrue(areJsonNodesEqual(expected, actual));
        } catch (JsonProcessingException e) {
            fail("Failed to process JSON: " + e.getMessage());
        }
    }

    private static boolean areJsonNodesEqual(JsonNode node1, JsonNode node2) {
        if (node1 == null || node2 == null) {
            return node1 == node2;
        }

        // Compare primitive values directly
        if (node1.isValueNode() || node2.isValueNode()) {
            return node1.equals(node2);
        }

        // Compare Objects (key-value pairs)
        if (node1.isObject() && node2.isObject()) {
            if (!node1.fieldNames().equals(node2.fieldNames())) {
                return false;
            }
            for (Iterator<String> it = node1.fieldNames(); it.hasNext(); ) {
                String fieldName = it.next();
                if (!areJsonNodesEqual(node1.get(fieldName), node2.get(fieldName))) {
                    return false;
                }
            }
            return true;
        }

        // Compare Arrays (ignoring order)
        if (node1.isArray() && node2.isArray()) {
            if (node1.size() != node2.size()) {
                return false;
            }
            Set<JsonNode> set1 = new HashSet<>();
            Set<JsonNode> set2 = new HashSet<>();
            StreamSupport.stream(node1.spliterator(), false).forEach(set1::add);
            StreamSupport.stream(node2.spliterator(), false).forEach(set2::add);

            return set1.equals(set2);
        }

        return false; // If types are different
    }

    @Test
    public void testAllCategoriesArePresent() throws Exception {
        MvcResult mvcResult = MOCK_MVC.perform(MockMvcRequestBuilders.get("/categories"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        compareActualResponseToExpectedResponse(responseBody, "listCategories_response_ok.json");
    }

    @Test
    public void testParentCategoriesAreDisplayedCorrectly() throws Exception {
        MvcResult mvcResult = MOCK_MVC.perform(MockMvcRequestBuilders.get("/categories/parent"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        compareActualResponseToExpectedResponse(responseBody, "listParentCategories_response_ok.json");
    }

    @Test
    public void testChildCategoriesAreDisplayedCorrectlyForParentCategory() throws Exception {
        MvcResult mvcResult = MOCK_MVC.perform(MockMvcRequestBuilders.get("/categories/children/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        compareActualResponseToExpectedResponse(responseBody, "listChildrenCategories_response_ok.json");
    }
}