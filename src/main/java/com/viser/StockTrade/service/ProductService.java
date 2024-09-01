package com.viser.StockTrade.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ProductRepository repo;
    @Lazy
    @Autowired
    private CategoryService categoryService;
    @Lazy
    @Autowired
    private SupplierService supplierService;

    public void save(Product product){
        repo.save(product);
    }

    public List<Product> getAll() {
        return repo.findAll();
    }

    public Product getById(int id) {
        return repo.findById(id);
    }

    public Product getByName(String name){
        return repo.findByName(name);
    }

    public boolean existByName(String name) {
        return repo.existsByName(name);
    }

    public boolean existById(int id){
        return repo.existsById(id);
    }

    public boolean existByCategoryId(int id) {
        return repo.existsByCategoryId(id);
    }

    public boolean existBySupplierId(int id) {
        return repo.existsBySupplierId(id);
    }

    public  long countAll(){
        return repo.count();
    }

    public void add(ProductDto productDto, BindingResult result) throws ValidationException, NameExistException {
        ExceptionHelper.throwValidationException(result, "/add-product-page");
        if (existByName(productDto.getName())) {
            throw new NameExistException("A product with this name already exists. Please choose a different name.", "/add-product-page");
        }
        Product product = new Product();
        updateProductFields(product, productDto);
        save(product);
    }

    public void delete(int id) throws NotFoundException {
        if(!existById(id)){
            throw new NotFoundException("Could not find any customer with ID " + id, "/product-page");
        }
        repo.deleteById(id);
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
        save(product);
    }

    private void updateProductFields(Product product, ProductDto productDto) {
        product.setName(ValueUtilityService.getNonEmptyValue(productDto.getName(), product.getName()));
        product.setPrice(ValueUtilityService.getNonEmptyValue(ValueUtilityService.getStringFromDtoToDouble(productDto.getPrice()), product.getPrice()));
        product.setStockQuantity(ValueUtilityService.getNonEmptyValue(ValueUtilityService.getStringFromDtoToInt(productDto.getStockQty()), product.getStockQuantity()));
        product.setCategory(ValueUtilityService.getNonEmptyValue(categoryService.getById(ValueUtilityService.getStringFromDtoToInt(productDto.getCategoryId())), product.getCategory()));
        product.setSupplier(ValueUtilityService.getNonEmptyValue(supplierService.getById(ValueUtilityService.getStringFromDtoToInt(productDto.getSupplierId())), product.getSupplier()));
    }
    public String getProductListInJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(getAll());
    }
}
