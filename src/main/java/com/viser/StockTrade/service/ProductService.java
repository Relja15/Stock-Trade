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

    public void save(Product product) {
        repo.save(product);
    }

    public void deleteById(int id) {
        repo.deleteById(id);
    }

    public List<Product> getAll() {
        return repo.findAll();
    }

    public Product getById(int id) {
        return repo.findById(id);
    }

    public Product getByName(String name) {
        return repo.findByName(name);
    }

    public boolean existByName(String name) {
        return repo.existsByName(name);
    }

    public boolean existById(int id) {
        return repo.existsById(id);
    }

    public boolean existBySupplierId(int id) {
        return repo.existsBySupplierId(id);
    }

    public long countAll() {
        return repo.count();
    }

    public void add(ProductDto productDto, BindingResult result) throws ValidationException, NameExistException {
        throwValidationException(result, "/add-product-page");
        throwNameExistException(existByName(productDto.getName()), "A product with this name already exists. Please choose a different name.", "/add-product-page");
        Product product = new Product();
        updateProductFields(product, productDto);
        save(product);
    }

    public void delete(int id) throws NotFoundException {
        throwNotFoundException(existById(id), "Could not find any customer with ID " + id, "/product-page");
        deleteById(id);
    }

    public void edit(int id, ProductDto productDto, BindingResult result) throws ValidationException, NotFoundException, NameExistException {
        throwValidationException(result, "/edit-product-page/" + id);
        Product product = getById(id);
        throwNotFoundException(product, "Could not find any product with ID" + id, "/edit-product-page/" + id);
        throwNameExistException(isProductNameChangedAndExists(product, productDto), "A product with this name already exists. Please choose a different name.", "/edit-product-page/" + id);
        updateProductFields(product, productDto);
        save(product);
    }

    private boolean isProductNameChangedAndExists(Product product, ProductDto productDto) {
        return !product.getName().equals(productDto.getName()) && existByName(productDto.getName());
    }

    private void updateProductFields(Product product, ProductDto productDto) {
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setStockQuantity(productDto.getStockQty());
        product.setCategory(categoryService.getById(productDto.getCategoryId()));
        product.setSupplier(supplierService.getById(productDto.getSupplierId()));
    }

    public String getProductListInJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(getAll());
    }
}
