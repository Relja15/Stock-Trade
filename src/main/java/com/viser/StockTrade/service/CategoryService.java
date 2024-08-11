package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.CategoryDto;
import com.viser.StockTrade.entity.Category;
import com.viser.StockTrade.entity.Product;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(int id) {
        return categoryRepository.findById(id);
    }

    public boolean delete(int id) throws NotFoundException, IOException {
        Category category = categoryRepository.findById(id);
        if (category == null) {
            throw new NotFoundException("Could not find any category with ID" + id);
        }
        List<Product> products = productService.getProductsByCategoryId(id);
        if (products.isEmpty()) {
            fileService.deleteFile(category.getIcon());
            categoryRepository.delete(category);
            return true;
        }
        return false;
    }

    public boolean existByName(String name) {
        return categoryRepository.existsByName(name);
    }

    public void add(CategoryDto categoryDto) throws IOException {
        Category category = new Category();
        updateCategoryFields(category, categoryDto);
        handleCategoryIcon(category, categoryDto);

        categoryRepository.save(category);
    }

    public void edit(int id, CategoryDto categoryDto) throws IOException {
        Category category = findCategoryById(id);
        updateCategoryFields(category, categoryDto);
        handleCategoryIcon(category, categoryDto);
        categoryRepository.save(category);
    }

    private Category findCategoryById(int id) {
        return categoryRepository.findById(id);
    }

    private void updateCategoryFields(Category category, CategoryDto categoryDto) {
        category.setName(getNonEmptyValue(categoryDto.getName(), category.getName()));
        category.setDescription(getNonEmptyValue(categoryDto.getDescription(), category.getDescription()));
    }

    private void handleCategoryIcon(Category category, CategoryDto categoryDto) throws IOException {
        if (categoryDto.getIcon() != null && !categoryDto.getIcon().isEmpty()) {
            deleteOldIconIfNecessary(category);
            String filename = categoryDto.getName() + "_" + categoryDto.getIcon().getOriginalFilename();
            fileService.uploadFile(categoryDto.getIcon(), filename);
            category.setIcon("/uploads/" + filename);
        }
    }

    private void deleteOldIconIfNecessary(Category category) throws IOException {
        if (category.getIcon() != null && !category.getIcon().isEmpty()) {
            fileService.deleteFile(category.getIcon());
        }
    }

    private String getNonEmptyValue(String newValue, String oldValue) {
        return (newValue != null && !newValue.isEmpty()) ? newValue : oldValue;
    }
}
