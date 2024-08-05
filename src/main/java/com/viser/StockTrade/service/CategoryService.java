package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.CategoryDto;
import com.viser.StockTrade.entity.Category;
import com.viser.StockTrade.entity.Product;
import com.viser.StockTrade.exceptions.CategoryNotFoundException;
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

    public boolean delete(int id) throws CategoryNotFoundException, IOException {
        Category category = categoryRepository.findById(id);
        if (category == null) {
            throw new CategoryNotFoundException("Could not find any category with ID" + id);
        }
        List<Product> products = productService.findProductsForCategory(id);
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
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        if (categoryDto.getCategoryIcon() != null && !categoryDto.getCategoryIcon().isEmpty()) {
            String filename = categoryDto.getName() + "_" + categoryDto.getCategoryIcon().getOriginalFilename();
            fileService.uploadFile(categoryDto.getCategoryIcon(), filename);
            category.setIcon("/uploads/" + filename);
        }

        categoryRepository.save(category);
    }

    public void edit(int id, CategoryDto categoryDto) throws CategoryNotFoundException, IOException {
        Category category = categoryRepository.findById(id);
        if (category == null) {
            throw new CategoryNotFoundException("Category not found.");
        }
        category.setName(!categoryDto.getName().isEmpty() ? categoryDto.getName() : category.getName());
        category.setDescription(!categoryDto.getDescription().isEmpty() ? categoryDto.getDescription() : category.getDescription());
        if (categoryDto.getCategoryIcon() != null && !categoryDto.getCategoryIcon().isEmpty()) {
            if (category.getIcon() != null && !category.getIcon().isEmpty()) {
                fileService.deleteFile(category.getIcon());
            }
            String filename = categoryDto.getName() + "_" + categoryDto.getCategoryIcon().getOriginalFilename();
            fileService.uploadFile(categoryDto.getCategoryIcon(), filename);
            category.setIcon("/uploads/" + filename);
        }
        categoryRepository.save(category);
    }
}
