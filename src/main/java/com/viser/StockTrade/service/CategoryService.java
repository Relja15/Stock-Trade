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

    /**
     * Saves the given {@link Category} entity to the repository.
     *
     * This method persists the provided {@code Category} object in the database using the repository.
     * If the {@code Category} entity already exists (based on its identifier), it will be updated;
     * otherwise, a new entity will be created.
     *
     * @param category the {@link Category} entity to be saved
     */
    public void save(Category category) {
        repo.save(category);
    }

    /**
     * Deletes the {@link Category} entity with the specified {@code id} from the repository.
     *
     * This method removes the {@code Category} entity identified by the given {@code id} from
     * the repository. If no entity with the provided {@code id} exists, the repository will
     * be unaffected.
     *
     * @param id the identifier of the {@link Category} entity to be deleted
     */
    public void deleteById(int id) {
        repo.deleteById(id);
    }

    /**
     * Retrieves all {@link Category} entities from the repository.
     *
     * This method fetches and returns a list of all {@code Category} entities stored in the repository.
     * If there are no entities in the repository, an empty list is returned.
     *
     * @return a {@link List} of {@link Category} entities
     */
    public List<Category> getAll() {
        return repo.findAll();
    }

    /**
     * Retrieves a {@link Category} entity by its identifier.
     *
     * This method searches for a {@code Category} entity in the repository using the provided unique identifier.
     * If an entity with the given identifier is found, it is returned. If no such entity is found,
     * this method returns {@code null}.
     *
     * @param id the unique identifier of the {@link Category} to retrieve
     * @return the {@link Category} entity with the specified identifier, or {@code null} if not found
     */
    public Category getById(int id) {
        return repo.findById(id);
    }

    /**
     * Checks if a {@link Category} entity with the specified name exists in the repository.
     *
     * This method queries the repository to determine if there is at least one {@code Category} entity
     * that has the given name. It returns {@code true} if such an entity exists, otherwise it returns {@code false}.
     *
     * @param name the name of the {@link Category} to check for existence
     * @return {@code true} if a {@link Category} with the specified name exists; {@code false} otherwise
     */
    public boolean existByName(String name) {
        return repo.existsByName(name);
    }

    /**
     * Checks if a {@link Category} entity with the specified ID exists in the repository.
     *
     * This method queries the repository to determine if there is at least one {@code Category} entity
     * with the given ID. It returns {@code true} if such an entity exists, otherwise it returns {@code false}.
     *
     * @param id the ID of the {@link Category} to check for existence
     * @return {@code true} if a {@link Category} with the specified ID exists; {@code false} otherwise
     */
    public boolean existById(int id) {
        return repo.existsById(id);
    }

    /**
     * Returns the total number of {@link Category} entities in the repository.
     *
     * This method queries the repository to count all existing {@code Category} entities.
     *
     * @return the total count of {@link Category} entities
     */
    public long countAll() {
        return repo.count();
    }

    /**
     * Adds a new {@link Category} entity based on the provided {@link CategoryDto}.
     *
     * @param categoryDto the data transfer object containing the information for the new category
     * @param result the binding result that contains validation errors, if any
     * @throws ValidationException if there are validation errors in the {@code categoryDto}
     * @throws NameExistException if a category with the same name already exists
     * @throws IOException if an I/O error occurs while handling the category icon
     */
    public void add(CategoryDto categoryDto, BindingResult result) throws ValidationException, NameExistException, IOException {
        throwValidationException(result, "/add-category-page");
        throwNameExistException(existByName(categoryDto.getName()), "A category with this name already exists. Please choose a different name.", "/add-category-page");
        Category category = new Category();
        updateCategoryFields(category, categoryDto);
        handleCategoryIcon(category, categoryDto);
        save(category);
    }

    /**
     * Deletes a {@link Category} entity by its ID.
     *
     * @param id the ID of the category to be deleted
     * @throws NotFoundException if no category with the specified ID is found
     * @throws IOException if an error occurs while deleting the icon file
     * @throws ForeignKeyConstraintViolationException if the category cannot be deleted due to foreign key constraints
     */
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

    /**
     * Edits an existing {@link Category} entity based on the provided data.
     *
     * @param id the ID of the category to be edited
     * @param categoryDto the DTO containing the updated category data
     * @param result the binding result that holds validation errors, if any
     * @throws ValidationException if there are validation errors in the provided DTO
     * @throws NotFoundException if no category with the specified ID is found
     * @throws NameExistException if a category with the new name already exists
     * @throws IOException if an error occurs while handling the category icon
     */
    public void edit(int id, CategoryDto categoryDto, BindingResult result) throws ValidationException, NotFoundException, NameExistException, IOException {
        throwValidationException(result, "/edit-category-page/" + id);
        Category category = getById(id);
        throwNotFoundException(category, "Could not find any category with ID" + id, "/edit-category-page/" + id);
        throwNameExistException(isCategoryNameChangedAndExists(category, categoryDto), "A category with this name already exists. Please choose a different name.", "/edit-category-page/" + id);
        updateCategoryFields(category, categoryDto);
        handleCategoryIcon(category, categoryDto);
        save(category);
    }

    /**
     * Determines if the category name has been changed and if a category with the new name already exists.
     * If both conditions are true, it means the category name has been changed and a category with the new name already exists.
     *
     * @param category the existing {@link Category} entity being checked
     * @param categoryDto the {@link CategoryDto} containing the updated category name
     * @return {@code true} if the category name has changed and the new name already exists; {@code false} otherwise
     */
    private boolean isCategoryNameChangedAndExists(Category category, CategoryDto categoryDto) {
        return !category.getName().equals(categoryDto.getName()) && existByName(categoryDto.getName());
    }

    /**
     * Updates the fields of the provided {@link Category} entity with values from the given {@link CategoryDto}.
     *
     * This method sets the name and description of the {@link Category} entity using the values provided in the {@link CategoryDto}.
     * It does not perform any validation or null checks; the calling code should ensure that the {@link Category} and
     * {@link CategoryDto} are not null before invoking this method.
     *
     * @param category the {@link Category} entity to update
     * @param categoryDto the {@link CategoryDto} containing the new values for the {@link Category}
     */
    private void updateCategoryFields(Category category, CategoryDto categoryDto) {
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
    }

    /**
     * Handles the upload and assignment of a category icon based on the provided {@link CategoryDto}.
     *
     * @param category the {@link Category} entity to update with the new icon
     * @param categoryDto the {@link CategoryDto} containing the new icon file and category name
     * @throws IOException if an I/O error occurs during file upload
     */
    private void handleCategoryIcon(Category category, CategoryDto categoryDto) throws IOException {
        if (categoryDto.getIcon() != null && !categoryDto.getIcon().isEmpty()) {
            deleteOldIconIfNecessary(category);
            String filename = categoryDto.getName() + "_" + categoryDto.getIcon().getOriginalFilename();
            fileService.uploadFile(categoryDto.getIcon(), filename);
            category.setIcon("/uploads/" + filename);
        }
    }

    /**
     * Deletes the old icon file associated with the given {@link Category}, if it exists.
     *
     * This method checks if the {@link Category} has an existing icon. If so, it delegates the
     * deletion of the old icon file to the {@link FileService}. This is useful to ensure that old
     * icon files are removed from the server before a new icon is uploaded.
     *
     * @param category the {@link Category} entity for which the old icon file needs to be deleted
     * @throws IOException if an I/O error occurs during the deletion of the file
     */
    private void deleteOldIconIfNecessary(Category category) throws IOException {
        if (category.getIcon() != null && !category.getIcon().isEmpty()) {
            fileService.deleteFile(category.getIcon());
        }
    }
}
