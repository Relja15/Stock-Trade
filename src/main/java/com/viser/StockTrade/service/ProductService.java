package com.viser.StockTrade.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viser.StockTrade.dto.ProductDto;
import com.viser.StockTrade.entity.Product;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.viser.StockTrade.exceptions.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repo;
    private final CategoryService categoryService;
    private final SupplierService supplierService;

    /**
     * Saves a {@link Product} object to the repository.
     *
     * This method persists the provided {@link Product} instance to the database. If the product
     * already exists, it will be updated with any changes; if it does not exist, it will be created.
     *
     * @param product the {@link Product} object to be saved or updated
     */
    public void save(Product product) {
        repo.save(product);
    }

    /**
     * Deletes a {@link Product} from the repository by its ID.
     *
     * This method removes the {@link Product} entity with the specified ID from the database. If no
     * product with the given ID exists, the operation will have no effect.
     *
     * @param id the ID of the {@link Product} to be deleted
     */
    public void deleteById(int id) {
        repo.deleteById(id);
    }

    /**
     * Retrieves all {@link Product} entities from the repository.
     *
     * This method fetches and returns a list of all {@link Product} entities stored in the repository.
     * The list may be empty if no products are found.
     *
     * @return a {@link List} of {@link Product} entities. If no products are present, an empty list is returned.
     */
    public List<Product> getAll() {
        return repo.findAll();
    }

    /**
     * Retrieves a {@link Product} entity by its ID.
     *
     * This method searches for a {@link Product} in the repository using the provided ID. If a product with the given ID exists, it is returned.
     * If no product is found with the specified ID, this method returns {@code null}.
     *
     * @param id the ID of the product to retrieve.
     * @return the {@link Product} entity with the specified ID, or {@code null} if no such product exists.
     * @throws IllegalArgumentException if the provided ID is negative.
     */
    public Product getById(int id) {
        return repo.findById(id);
    }

    /**
     * Retrieves a {@link Product} entity by its name.
     *
     * This method searches for a {@link Product} in the repository using the provided name. If a product with the given name exists, it is returned.
     * If no product is found with the specified name, this method returns {@code null}.
     *
     * @param name the name of the product to retrieve.
     * @return the {@link Product} entity with the specified name, or {@code null} if no such product exists.
     */
    public Product getByName(String name) {
        return repo.findByName(name);
    }

    /**
     * Checks if a {@link Product} exists in the repository with the given name.
     *
     * This method queries the repository to determine whether there is a {@link Product} with the specified name.
     * It returns {@code true} if a product with that name exists, and {@code false} otherwise.
     *
     * @param name the name of the product to check for existence.
     * @return {@code true} if a product with the specified name exists, {@code false} otherwise.
     */
    public boolean existByName(String name) {
        return repo.existsByName(name);
    }

    /**
     * Checks if a {@link Product} exists in the repository with the given ID.
     *
     * This method queries the repository to determine whether there is a {@link Product} with the specified ID.
     * It returns {@code true} if a product with that ID exists, and {@code false} otherwise.
     *
     * @param id the ID of the product to check for existence.
     * @return {@code true} if a product with the specified ID exists, {@code false} otherwise.
     */
    public boolean existById(int id) {
        return repo.existsById(id);
    }

    /**
     * Returns the total number of {@link Product} records in the repository.
     *
     * This method queries the repository to count all the {@link Product} entities present.
     * It returns the total count of products.
     *
     * @return the number of {@link Product} records in the repository.
     */
    public long countAll() {
        return repo.count();
    }

    /**
     * Adds a new {@link Product} to the repository based on the provided {@link ProductDto}.
     *
     * This method validates the input data, checks if a product with the given name already exists,
     * and then creates and saves a new {@link Product} entity if all conditions are met.
     *
     * @param productDto the data transfer object containing the product information to be added.
     * @param result     the binding result that contains validation errors, if any.
     *
     * @throws ValidationException if validation errors are present in the binding result.
     * @throws NameExistException  if a product with the same name already exists in the repository.
     */
    public void add(ProductDto productDto, BindingResult result) throws ValidationException, NameExistException {
        throwValidationException(result, "/add-product-page");
        throwNameExistException(existByName(productDto.getName()), "A product with this name already exists. Please choose a different name.", "/add-product-page");
        Product product = new Product();
        updateProductFields(product, productDto);
        save(product);
    }

    /**
     * Deletes a {@link Product} from the repository by its ID.
     *
     * This method first checks if a {@link Product} with the given ID exists in the repository.
     * If it does not, a {@link NotFoundException} is thrown. If the product exists, it is then deleted.
     *
     * @param id the ID of the {@link Product} to be deleted.
     *
     * @throws NotFoundException if no product with the specified ID is found in the repository.
     */
    public void delete(int id) throws NotFoundException {
        throwNotFoundException(existById(id), "Could not find any customer with ID " + id, "/product-page");
        deleteById(id);
    }

    /**
     * Edits an existing {@link Product} in the repository.
     *
     * This method performs several operations:
     * 1. Validates the provided data and throws a {@link ValidationException} if there are errors.
     * 2. Retrieves the {@link Product} by its ID. Throws a {@link NotFoundException} if no product with the specified ID is found.
     * 3. Checks if the new name for the product is already taken and throws a {@link NameExistException} if it is.
     * 4. Updates the product's fields with the data from the {@link ProductDto} and saves the updated product to the repository.
     *
     * @param id the ID of the {@link Product} to be edited.
     * @param productDto the data transfer object containing updated product details.
     * @param result the {@link BindingResult} that contains validation errors.
     *
     * @throws ValidationException if validation errors are found in the provided data.
     * @throws NotFoundException if no product with the specified ID is found in the repository.
     * @throws NameExistException if a product with the new name already exists in the repository.
     */
    public void edit(int id, ProductDto productDto, BindingResult result) throws ValidationException, NotFoundException, NameExistException {
        throwValidationException(result, "/edit-product-page/" + id);
        Product product = getById(id);
        throwNotFoundException(product, "Could not find any product with ID" + id, "/edit-product-page/" + id);
        throwNameExistException(isProductNameChangedAndExists(product, productDto), "A product with this name already exists. Please choose a different name.", "/edit-product-page/" + id);
        updateProductFields(product, productDto);
        save(product);
    }

    /**
     * Checks if the product name has changed and if the new name already exists in the system.
     *
     * This method compares the current product's name with the name from the provided {@link ProductDto}.
     * If the names are different, it further checks if a product with the new name exists in the system.
     *
     * @param product the current product to compare
     * @param productDto the product data transfer object containing the new name
     * @return {@code true} if the product name has changed and the new name already exists,
     *         {@code false} otherwise
     */
    private boolean isProductNameChangedAndExists(Product product, ProductDto productDto) {
        return !product.getName().equals(productDto.getName()) && existByName(productDto.getName());
    }

    /**
     * Updates the fields of an existing product with values from the provided {@link ProductDto}.
     *
     * This method updates the product's name, price, stock quantity, category, and supplier
     * based on the values from the given {@link ProductDto}.
     *
     * @param product the product to update
     * @param productDto the data transfer object containing the new values for the product
     */
    private void updateProductFields(Product product, ProductDto productDto) {
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setStockQuantity(productDto.getStockQty());
        product.setCategory(categoryService.getById(productDto.getCategoryId()));
        product.setSupplier(supplierService.getById(productDto.getSupplierId()));
    }

    /**
     * Retrieves the list of all products in JSON format.
     *
     * This method converts the list of all products returned by {@link #getAll()} into a JSON string
     * using the {@link ObjectMapper}.
     *
     * @return a JSON representation of the product list
     * @throws JsonProcessingException if there is an error during the conversion of the product list to JSON
     */
    public String getProductListInJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(getAll());
    }
}
