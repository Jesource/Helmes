package ee.helmes.home.assignment.controllers;

import ee.helmes.home.assignment.dto.CategoryDTO;
import ee.helmes.home.assignment.dto.mapper.CategoryDTOMapper;
import ee.helmes.home.assignment.models.Category;
import ee.helmes.home.assignment.models.UserData;
import ee.helmes.home.assignment.services.CategoryService;
import ee.helmes.home.assignment.services.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RequestMapping(value = "/rest", produces = "application/json")
@RestController
public class RestApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiController.class);

    private final CategoryService categoryService;
    private final UserDataService userDataService;

    @Autowired
    public RestApiController(CategoryService categoryService, UserDataService userDataService) {
        this.categoryService = categoryService;
        this.userDataService = userDataService;
    }


    @GetMapping("/categories")
    public List<?> listCategories(@RequestParam(required = false, defaultValue = "false") Boolean isDetailed) {
        if (!isDetailed) {
            return categoryService.getAllCategories();
        }

        return new CategoryDTOMapper(categoryService).getOrganisedCategoryDTOsForTemplate();
    }

    @GetMapping("/categories/{categoryId}")
    public Object getCategoryById(@PathVariable Integer categoryId,
                                  @RequestParam(required = false, defaultValue = "false") Boolean isDetailed) {
        LOGGER.info("Requested data for a category with ID '{}'", categoryId);

        if (isDetailed) {
            LOGGER.info("Requested for detailed response for a category with ID '{}'", categoryId);
            return getCategoryDTOWithSetupChildrenForProvidedCategoryId(categoryId);
        }

        return getCategoryDetailsForCategoryId(categoryId);
    }

    @PostMapping("/userData")
    public ResponseEntity<?> sendUserData(@RequestBody UserData userData) {
        LOGGER.info("Received user data: {}", userData);

        Optional<Long> idOfCreatedRecord = userDataService.validateAndSaveUserData(userData);
        if (idOfCreatedRecord.isEmpty()) {
            LOGGER.info("The received user data is not valid, returning unsuccessful response");
            return ResponseEntity.unprocessableEntity().build();
        }

        ResponseEntity<Object> response = ResponseEntity.created(
                        ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}").buildAndExpand(idOfCreatedRecord.get()).toUri())
                .build();

        LOGGER.debug("Returning response: {}", response);
        return response;
    }


    @GetMapping("/userData/{recordId}")
    public UserData getUserDataById(@PathVariable Integer recordId) {
        LOGGER.info("Requested for saved user data with ID '{}'", recordId);

        return userDataService.getUserDataById(recordId).orElse(null);
    }

    private CategoryDTO getCategoryDTOWithSetupChildrenForProvidedCategoryId(Integer categoryId) {
        return new CategoryDTOMapper(categoryService)
                .getCategoryDTOWithSetupChildrenForProvidedCategoryId(categoryId);
    }

    private ResponseEntity<Category> getCategoryDetailsForCategoryId(Integer categoryId) {
        Optional<Category> categoryById = categoryService.getCategoryById(categoryId);
        if (categoryById.isEmpty()) {
            LOGGER.debug("Could not find any data for a category with ID '{}'", categoryId);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(categoryById.get());
    }


}
