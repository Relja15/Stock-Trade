package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.CategoryDto;
import com.viser.StockTrade.entity.Category;
import com.viser.StockTrade.exceptions.*;
import com.viser.StockTrade.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repo;
    private final ProductService productService;
    private final FileService fileService;

    public List<Category> getAll() {
        return repo.findAll();
    }

    public Category getById(int id) {
        return repo.findById(id);
    }

    public boolean existByName(String name) {
        return repo.existsByName(name);
    }

    public boolean existById(int id) {
        return repo.existsById(id);
    }

    public long countAll(){
        return repo.count();
    }

    public void add(CategoryDto categoryDto, BindingResult result) throws ValidationException, NameExistException, IOException {
        ExceptionHelper.throwValidationException(result, "/add-category-page");
        if (existByName(categoryDto.getName())) {
            throw new NameExistException("A category with this name already exists. Please choose a different name.", "/add-category-page");
        }
        Category category = new Category();
        updateCategoryFields(category, categoryDto);
        handleCategoryIcon(category, categoryDto);
        repo.save(category);
    }

    public void delete(int id) throws NotFoundException, ForeignKeyConstraintViolationException, IOException {
        if (!existById(id)) {
            throw new NotFoundException("Could not find any category with ID " + id, "/category-page");
        }
        if (productService.existByCategoryId(id)) {
            throw new ForeignKeyConstraintViolationException("The category cannot be deleted because it has associated products.", "/category-page");
        }
        fileService.deleteFile(getById(id).getIcon());
        repo.deleteById(id);
    }

    public void edit(int id, CategoryDto categoryDto, BindingResult result) throws ValidationException, NotFoundException, NameExistException, IOException {
        ExceptionHelper.throwValidationException(result, "/edit-category-page/" + id);
        Category category = findCategoryById(id);
        if (category == null) {
            throw new NotFoundException("Could not find any category with ID" + id, "/edit-category-page/" + id);
        }
        if (category.getName().equals(categoryDto.getName()) && existByName(categoryDto.getName())) {
            throw new NameExistException("A category with this name already exists. Please choose a different name.", "/edit-category-page/" + id);
        }
        updateCategoryFields(category, categoryDto);
        handleCategoryIcon(category, categoryDto);
        repo.save(category);
    }

    private Category findCategoryById(int id) {
        return repo.findById(id);
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
