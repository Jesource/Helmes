package ee.helmes.home.assignment.controllers;

import ee.helmes.home.assignment.dto.CategoryDTO;
import ee.helmes.home.assignment.dto.mapper.CategoryDTOMapper;
import ee.helmes.home.assignment.enums.CustomHttpSessionAttributes;
import ee.helmes.home.assignment.models.Category;
import ee.helmes.home.assignment.models.UserData;
import ee.helmes.home.assignment.services.CategoryService;
import ee.helmes.home.assignment.services.UserDataService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class FrontendController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FrontendController.class);

    private final CategoryService categoryService;
    private final UserDataService userDataService;

    @Autowired
    public FrontendController(CategoryService categoryService, UserDataService userDataService) {
        this.categoryService = categoryService;
        this.userDataService = userDataService;
    }


    @GetMapping("/form")
    public String showForm(Model model, HttpSession httpSession) {
        UserData userData;
        List<CategoryDTO> categoryEntries = new CategoryDTOMapper(categoryService)
                .getOrganisedCategoryDTOsForTemplate();
        Long recordId = (Long) httpSession.getAttribute(CustomHttpSessionAttributes.SUBMITTED_USER_DATA_ID.name());


        if (recordId == null) {
            LOGGER.debug("Did not find any previously submitted data for current HTTP session");
            userData = new UserData();
        } else {
            Optional<UserData> userDataById = userDataService.getUserDataById(recordId);
            userData = userDataById.orElse(new UserData());
            LOGGER.info("Got recordID param. Found data: {}", userData);
        }
        model.addAttribute("categories", categoryEntries);
        model.addAttribute("idsOfSelectedCategories", userData.getIdsOfInvolvedSectors());
        model.addAttribute("registrationForm", userData);

        return "categories";
    }

    @PostMapping("/userData")
    public String userDataSubmit(@ModelAttribute UserData userData, Model model, HttpSession httpSession) {
        LOGGER.info("Got UserData form submission from Thymeleaf frontend: {}", userData);
        userData.setId((Long) httpSession.getAttribute(CustomHttpSessionAttributes.SUBMITTED_USER_DATA_ID.name()));

        userData.getIdsOfInvolvedSectors().forEach(id -> {
            Optional<Category> involvedCategory = categoryService.getCategoryById(id);

            if (involvedCategory.isEmpty()) {
                LOGGER.warn("Invalid category ID '{}': category not found", id);
            }
        });

        LOGGER.debug("Saving the userData");
        Optional<Long> savedRecordId = userDataService.validateAndSaveUserData(userData);
        if (savedRecordId.isPresent()) {
            Long recordId = savedRecordId.get();
            LOGGER.info("Successfully saved user data. ID: {}", recordId);
            httpSession.setAttribute(CustomHttpSessionAttributes.SUBMITTED_USER_DATA_ID.name(), recordId);
        }

        return "redirect:/form";
    }

}
