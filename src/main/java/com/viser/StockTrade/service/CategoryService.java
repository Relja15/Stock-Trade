package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.CategoryDto;
import com.viser.StockTrade.entity.Category;
import com.viser.StockTrade.exceptions.ForeignKeyConstraintViolationException;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.util.List;

import static com.viser.StockTrade.exceptions.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repo;
    private final FileService fileService;

    public void save(Category category) {
        repo.save(category);
    }

    public void deleteById(int id) {
        repo.deleteById(id);
    }

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

    public long countAll() {
        return repo.count();
    }

    public void add(CategoryDto categoryDto, BindingResult result) throws ValidationException, NameExistException, IOException {
        throwValidationException(result, "/add-category-page");
        throwNameExistException(existByName(categoryDto.getName()), "A category with this name already exists. Please choose a different name.", "/add-category-page");
        Category category = new Category();
        updateCategoryFields(category, categoryDto);
        handleCategoryIcon(category, categoryDto);
        save(category);
    }

    public void delete(int id) throws NotFoundException, IOException, ForeignKeyConstraintViolationException {
        throwNotFoundException(existById(id), "Could not find any category with ID " + id, "/category-page");
        String icon = getById(id).getIcon();
        try {
            deleteById(id);
        } catch (Exception e) {
            throw new ForeignKeyConstraintViolationException("The category cannot be deleted because it has associated products.", "/category-page");
        }
        fileService.deleteFile(icon);
    }

    public void edit(int id, CategoryDto categoryDto, BindingResult result) throws ValidationException, NotFoundException, NameExistException, IOException {
        throwValidationException(result, "/edit-category-page/" + id);
        Category category = findCategoryById(id);
        throwNotFoundException(category, "Could not find any category with ID" + id, "/edit-category-page/" + id);
        throwNameExistException(isCategoryNameChangedAndExists(category, categoryDto), "A category with this name already exists. Please choose a different name.", "/edit-category-page/" + id);
        updateCategoryFields(category, categoryDto);
        handleCategoryIcon(category, categoryDto);
        save(category);
    }

    private boolean isCategoryNameChangedAndExists(Category category, CategoryDto categoryDto) {
        return !category.getName().equals(categoryDto.getName()) && existByName(categoryDto.getName());
    }

    private Category findCategoryById(int id) {
        return repo.findById(id);
    }

    private void updateCategoryFields(Category category, CategoryDto categoryDto) {
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
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
}
