package ee.helmes.home.assignment.controllers;

import ee.helmes.home.assignment.models.Category;
import ee.helmes.home.assignment.repos.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MainController {

    private final CategoryRepository categoryRepository;

    @Autowired
    public MainController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @GetMapping("/create")
    public String populateDb() {
        categoryRepository.saveAllAndFlush(List.of(
                new Category(1, "Manufacturing"),

    new Category(19, "Construction materials", 1),
    new Category(18, "Electronics and Optics", 1),

    new Category(6, "Food and Beverage", 1),
    new Category(342, "Bakery & confectionery products", 6),
    new Category(43, "Beverages", 6),
    new Category(42, "Fish & fish products ", 6),
    new Category(40, "Meat & meat products", 6),
    new Category(39, "Milk & dairy products ", 6),
    new Category(437, "Other", 6),
    new Category(378, "Sweets & snack food", 6),

    new Category(13, "Furniture", 1),
    new Category(389,   "Bathroom/sauna ", 13),
    new Category(385,   "Bedroom", 13),
    new Category(390,   "Children’s room ", 13),
    new Category(98,    "Kitchen ", 13),
    new Category(101,   "Living room ", 13),
    new Category(392,   "Office", 13),
//    new Category(394,   "Other (Furniture)", 13),
    new Category(394,   "Other", 13),
    new Category(341,   "Outdoor ", 13),
    new Category(99,    "Project furniture", 13),

    new Category(12, "Machinery", 1),
    new Category(94,    "Machinery components", 12),
    new Category(91,    "Machinery equipment/tools", 12),
    new Category(224,   "Manufacture of machinery ", 12),
    new Category(97,    "Maritime", 12),
    new Category(271,       "Aluminium and steel workboats ", 97),
    new Category(269,       "Boat/Yacht building", 97),
    new Category(230,       "Ship repair and conversion", 97),
    new Category(93,    "Metal structures", 12),
    new Category(508,   "Other", 12),
    new Category(227,   "Repair and maintenance service", 12),

    new Category(11, "Metalworking", 1),
    new Category(67,    "Construction of metal structures", 11),
    new Category(263,   "Houses and buildings", 11),
    new Category(267,   "Metal products", 11),
    new Category(542,   "Metal works", 11),
    new Category(75,        "CNC-machining", 542),
    new Category(62,        "Forgings, Fasteners ", 542),
    new Category(69,        "Gas, Plasma, Laser cutting", 542),
    new Category(66,        "MIG, TIG, Aluminum welding", 542),

    new Category(9, "Plastic and Rubber", 1),
    new Category(54,    "Packaging", 9),
    new Category(556,   "Plastic goods", 9),
    new Category(559,   "Plastic processing technology", 9),
    new Category(55,    "Blowing", 559),
    new Category(57,    "Moulding", 559),
    new Category(53,    "Plastics welding and processing", 559),
    new Category(560,   "Plastic profiles", 9),

    new Category(5, "Printing ", 1),
    new Category(148, "Advertising", 5),
    new Category(150, "Book/Periodicals printing", 5),
    new Category(145, "Labelling and packaging printing", 5),

    new Category(7, "Textile and Clothing", 1),
    new Category(44, "Clothing", 7),
    new Category(45, "Textile", 7),

    new Category(8, "Wood", 1),
//    new Category(337,   "Other (Wood)", 8),
    new Category(337,   "Other", 8),
    new Category(51,    "Wooden building materials", 8),
    new Category(47,    "Wooden houses", 8),

                
                

                new Category(2, "Service"),
                new Category(25, "Business services", 2),
                new Category(35, "Engineering", 2),
                new Category(28, "Information Technology and Telecommunications", 2),
                    new Category(581, "Data processing, Web portals, E-marketing", 28),
                    new Category(576, "Programming, Consultancy", 28),
                    new Category(121, "Software, Hardware", 28),
                    new Category(122, "Telecommunications", 28),
                new Category(22, "Tourism", 2),
                new Category(141, "Translation services", 2),
                new Category(21, "Transport and Logistics", 2),
                    new Category(111, "Air", 21),
                    new Category(114, "Rail", 21),
                    new Category(112, "Road", 21),
                    new Category(113, "Water", 21),


                new Category(3, "Other"),
                new Category(37, "Creative industries", 3),
                new Category(29, "Energy technology", 3),
                new Category(33, "Environment", 3)
        ));

        return "redirect:/form";
    }

}
