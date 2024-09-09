package com.viser.StockTrade.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.viser.StockTrade.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final ProductService productService;
    private final PurchaseService purchaseService;

    /**
     * Displays the index page with relevant data.
     *
     * This method handles the request to display the index page. It populates the model with data necessary for the
     * index page, including user information, category count, product count, and supplier count. The current
     * authenticated user's data is also fetched and added to the model. The method then returns the view name for
     * the index page.
     *
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the index page
     */
    @GetMapping("/index")
    public String showIndexPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("categoryCount", categoryService.countAll());
        model.addAttribute("productCount", productService.countAll());
        model.addAttribute("supplerCount", supplierService.countAll());
        return "index";
    }

    /**
     * Displays the login page.
     *
     * This method handles the request to show the login page. It returns the view name for the login page, which
     * will be used to render the login form to the user.
     *
     * @return the view name for the login page
     */
    @GetMapping("/login-page")
    public String showLoginPage() {
        return "login-page";
    }

    /**
     * Displays the users page with a list of all users.
     *
     * This method handles the request to show the users page, which is accessible only to users with the 'ADMIN' authority.
     * It populates the model with user-related data, including the current user's information and a list of all users.
     * The method then returns the view name for the users page.
     *
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the users page
     */
    @GetMapping("/users-page")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showUsersPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("users", userService.getAll());
        return "users-page";
    }

    /**
     * Displays the add user page.
     *
     * This method handles the request to show the page for adding a new user, which is accessible only to users
     * with the 'ADMIN' authority. It populates the model with user-related data, including the current user's information.
     * The method then returns the view name for the add user page.
     *
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the add user page
     */
    @GetMapping("/add-user-page")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showAddUser(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "add-user-page";
    }

    /**
     * Displays the edit user page for a specific user.
     *
     * This method handles the request to show the page for editing an existing user, which is accessible only to users
     * with the 'ADMIN' authority. It populates the model with data for the user being edited and the current user's information.
     * The method also adds the page title to the model. The method then returns the view name for the edit user page.
     *
     * @param id the ID of the user to be edited
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the edit user page
     */
    @GetMapping("/edit-user-page/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showEditUserPage(@PathVariable("id") Integer id, Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("user", userService.getById(id));
        model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
        return "edit-user-page";
    }

    /**
     * Displays the user profile page.
     *
     * This method handles the request to show the profile page for the currently authenticated user. It populates
     * the model with user-related data, including information about the currently authenticated user. The method
     * then returns the view name for the profile page.
     *
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the profile page
     */
    @GetMapping("/profile-page")
    public String showUserProfile(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "profile-page";
    }

    /**
     * Displays the edit profile page.
     *
     * This method handles the request to show the page for editing the profile of the currently authenticated user.
     * It populates the model with user-related data, including information about the currently authenticated user.
     * The method then returns the view name for the edit profile page.
     *
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the edit profile page
     */
    @GetMapping("/edit-profile")
    public String shoeEditProfilePage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "edit-profile";
    }

    /**
     * Displays the category page.
     *
     * This method handles the request to show the category page. It populates the model with user-related data,
     * including information about the currently authenticated user, and retrieves all categories to be displayed on the page.
     * The method then returns the view name for the category page.
     *
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the category page
     */
    @GetMapping("/category-page")
    public String showCategoryPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("categories", categoryService.getAll());
        return "category-page";
    }

    /**
     * Displays the add category page.
     *
     * This method handles the request to show the page for adding a new category. It populates the model with user-related
     * data, including information about the currently authenticated user. The method then returns the view name for the
     * add category page.
     *
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the add category page
     */
    @GetMapping("/add-category-page")
    public String showAddCategory(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "add-category-page";
    }

    /**
     * Displays the edit category page for a specific category.
     *
     * This method handles the request to show the page for editing an existing category identified by its ID.
     * It populates the model with user-related data, including information about the currently authenticated user,
     * and retrieves the category data to be edited. The method then returns the view name for the edit category page.
     *
     * @param id the ID of the category to be edited
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the edit category page
     */
    @GetMapping("/edit-category-page/{id}")
    public String showEditCategoryPage(@PathVariable("id") Integer id, Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("category", categoryService.getById(id));
        return "/edit-category-page";
    }

    /**
     * Displays the product page.
     *
     * This method handles the request to show the product page. It populates the model with user-related data,
     * including information about the currently authenticated user, and retrieves all products to be displayed on the page.
     * The method then returns the view name for the product page.
     *
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the product page
     */
    @GetMapping("/product-page")
    public String showProductPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("products", productService.getAll());
        return "product-page";
    }

    /**
     * Displays the add product page.
     *
     * This method handles the request to show the page for adding a new product. It populates the model with user-related
     * data, including information about the currently authenticated user. Additionally, it retrieves and adds lists of
     * categories and suppliers to the model, which are needed for selecting appropriate options when adding a product.
     * The method then returns the view name for the add product page.
     *
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the add product page
     */
    @GetMapping("/add-product-page")
    public String showAddProductPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("suppliers", supplierService.getAll());
        return "add-product-page";
    }

    /**
     * Displays the edit product page for a specific product.
     *
     * This method handles the request to show the page for editing an existing product identified by its ID.
     * It populates the model with user-related data, including information about the currently authenticated user.
     * Additionally, it retrieves and adds lists of categories and suppliers to the model, which are needed for
     * selecting appropriate options when editing a product. The method also retrieves the details of the product
     * to be edited and adds it to the model. The method then returns the view name for the edit product page.
     *
     * @param id the ID of the product to be edited
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the edit product page
     */
    @GetMapping("/edit-product-page/{id}")
    public String showEditProductPage(@PathVariable("id") Integer id, Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("suppliers", supplierService.getAll());
        model.addAttribute("product", productService.getById(id));
        return "edit-product-page";
    }

    /**
     * Displays the supplier page.
     *
     * This method handles the request to show the supplier page. It populates the model with user-related data,
     * including information about the currently authenticated user. Additionally, it retrieves and adds a list of
     * suppliers to the model, which is needed for displaying on the page. The method then returns the view name for
     * the supplier page.
     *
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the supplier page
     */
    @GetMapping("/supplier-page")
    public String showSupplierPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("suppliers", supplierService.getAll());
        return "supplier-page";
    }

    /**
     * Displays the add supplier page.
     *
     * This method handles the request to show the page for adding a new supplier. It populates the model with
     * user-related data, including information about the currently authenticated user. The method then returns the
     * view name for the add supplier page.
     *
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the add supplier page
     */
    @GetMapping("/add-supplier-page")
    public String showAddSupplier(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "add-supplier-page";
    }

    /**
     * Displays the edit supplier page for a specific supplier.
     *
     * This method handles the request to show the page for editing an existing supplier identified by its ID.
     * It populates the model with user-related data, including information about the currently authenticated user.
     * Additionally, it retrieves and adds the details of the supplier to be edited to the model. The method then
     * returns the view name for the edit supplier page.
     *
     * @param id the ID of the supplier to be edited
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the edit supplier page
     */
    @GetMapping("/edit-supplier-page/{id}")
    public String showEditSupplierCategory(@PathVariable("id") Integer id, Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("supplier", supplierService.getById(id));
        return "edit-supplier-page";
    }

    /**
     * Displays the purchases page.
     *
     * This method handles the request to show the purchases page. It populates the model with user-related data,
     * including information about the currently authenticated user. Additionally, it retrieves and adds a list of
     * all purchases to the model, which is needed for displaying on the page. The method then returns the view name
     * for the purchases page.
     *
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the purchases page
     */
    @GetMapping("/purchases-page")
    public String showPurchasesPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("purchases", purchaseService.getAll());
        return "purchases-page";
    }

    /**
     * Displays the add purchase page.
     *
     * This method handles the request to show the page for adding a new purchase. It populates the model with user-related
     * data, including information about the currently authenticated user. Additionally, it retrieves and adds a list of
     * all suppliers and a JSON representation of all products to the model. These attributes are needed for creating a new
     * purchase. The method then returns the view name for the add purchase page.
     *
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the add purchase page
     */
    @GetMapping("/add-purchase-page")
    public String showAddPurchasePage(Model model, Principal principal) throws JsonProcessingException {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("suppliers", supplierService.getAll());
        model.addAttribute("products", productService.getProductListInJson());
        return "add-purchase-page";
    }

    /**
     * Displays the invoice page.
     *
     * This method handles the request to show the invoice page. It populates the model with user-related data, including
     * information about the currently authenticated user. Additionally, it retrieves and adds a list of all purchases to
     * the model, which is needed for displaying invoices on the page. The method then returns the view name for the
     * invoice page.
     *
     * @param model the {@link Model} object used to add attributes for the view
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @return the view name for the invoice page
     */
    @GetMapping("/invoice-page")
    public String showInvoicePage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("purchases", purchaseService.getAll());
        return "invoice-page";
    }
}
