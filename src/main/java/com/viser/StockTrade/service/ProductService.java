package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.ProductDto;
import com.viser.StockTrade.entity.Category;
import com.viser.StockTrade.entity.Product;
import com.viser.StockTrade.entity.Supplier;
import com.viser.StockTrade.exceptions.ExceptionHelper;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    @Lazy
    @Autowired
    private CategoryService categoryService;
    @Lazy
    @Autowired
    private SupplierService supplierService;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(int id) {
        return productRepository.findById(id);
    }

    public boolean existByName(String name) {
        return productRepository.existsByName(name);
    }

    public boolean existById(int id){
        return productRepository.existsById(id);
    }

    public boolean existByCategoryId(int id) {
        return productRepository.existsByCategoryId(id);
    }

    public boolean existBySupplierId(int id) {
        return productRepository.existsBySupplierId(id);
    }

    public void add(ProductDto productDto, BindingResult result) throws ValidationException, NameExistException {
        ExceptionHelper.throwValidationException(result, "/add-product-page");
        if (existByName(productDto.getName())) {
            throw new NameExistException("A product with this name already exists. Please choose a different name.", "/add-product-page");
        }
        Product product = new Product();
        updateProductFields(product, productDto);
        productRepository.save(product);
    }

    public void delete(int id) throws NotFoundException {
        if(!existById(id)){
            throw new NotFoundException("Could not find any customer with ID " + id, "/product-page");
        }
        productRepository.deleteById(id);
    }

    public void edit(int id, ProductDto productDto, BindingResult result) throws ValidationException, NotFoundException, NameExistException {
        ExceptionHelper.throwValidationException(result, "/edit-product-page/" + id);
        Product product = getById(id);
        if (product == null) {
            throw new NotFoundException("Could not find any product with ID" + id, "/edit-product-page/" + id);
        }
        if(product.getName().equals(productDto.getName()) && existByName(productDto.getName())){
            throw new NameExistException("A product with this name already exists. Please choose a different name.", "/edit-product-page/" + id);
        }
        updateProductFields(product, productDto);
        productRepository.save(product);
    }

    private void updateProductFields(Product product, ProductDto productDto) {
        product.setName(getNonEmptyString(productDto.getName(), product.getName()));
        product.setPrice(getNonEmptyInt(getStringFromDtoToInt(productDto.getPrice()), product.getPrice()));
        product.setStockQuantity(getNonEmptyInt(getStringFromDtoToInt(productDto.getStockQty()), product.getStockQuantity()));
        product.setCategory(getNonEmptyCategory(categoryService.getById(getStringFromDtoToInt(productDto.getCategoryId())), product.getCategory()));
        product.setSupplier(getNonEmptySupplier(supplierService.getById(getStringFromDtoToInt(productDto.getCategoryId())), product.getSupplier()));
    }

    private String getNonEmptyString(String newValue, String oldValue) {
        return (newValue != null && !newValue.isEmpty()) ? newValue : oldValue;
    }

    private int getNonEmptyInt(int newValue, int oldValue) {
        return newValue != 0 ? newValue : oldValue;
    }

    private Category getNonEmptyCategory(Category newValue, Category oldValue) {
        return newValue != null ? newValue : oldValue;
    }

    private Supplier getNonEmptySupplier(Supplier newValue, Supplier oldValue) {
        return newValue != null ? newValue : oldValue;
    }

    private int getStringFromDtoToInt(String stringFromDto){
        return !stringFromDto.isEmpty() ? Integer.parseInt(stringFromDto) : 0;
    }
}
