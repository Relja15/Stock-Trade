package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.SupplierDto;
import com.viser.StockTrade.entity.Supplier;
import com.viser.StockTrade.exceptions.ForeignKeyConstraintViolationException;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.viser.StockTrade.exceptions.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository repo;

    /**
     * Saves a {@link Supplier} entity to the database.
     *
     * This method persists the provided {@link Supplier} entity by calling the repository's save method.
     *
     * @param supplier the {@link Supplier} entity to be saved
     */
    public void save(Supplier supplier) {
        repo.save(supplier);
    }

    /**
     * Deletes a {@link Supplier} entity from the database by its ID.
     *
     * This method removes the {@link Supplier} entity with the specified ID from the database
     * by calling the repository's deleteById method.
     *
     * @param id the ID of the supplier to be deleted
     */
    public void deleteById(int id) {
        repo.deleteById(id);
    }

    /**
     * Retrieves all {@link Supplier} entities from the database.
     *
     * This method returns a list of all {@link Supplier} entities by calling the repository's
     * findAll method.
     *
     * @return a list of all suppliers
     */
    public List<Supplier> getAll() {
        return repo.findAll();
    }

    /**
     * Retrieves a {@link Supplier} entity by its ID.
     *
     * This method fetches a {@link Supplier} from the database using the provided ID.
     *
     * @param id the ID of the supplier to retrieve
     * @return the {@link Supplier} entity with the specified ID, or {@code null} if no such entity exists
     */
    public Supplier getById(int id) {
        return repo.findById(id);
    }

    /**
     * Retrieves a {@link Supplier} entity by its name.
     *
     * This method fetches a {@link Supplier} from the database using the provided name.
     *
     * @param name the name of the supplier to retrieve
     * @return the {@link Supplier} entity with the specified name, or {@code null} if no such supplier exists
     */
    public Supplier getByName(String name) {
        return repo.findByName(name);
    }

    /**
     * Checks if a {@link Supplier} entity exists with the given name.
     *
     * This method verifies whether a {@link Supplier} with the specified name exists in the database
     * by calling the repository's existsByName method.
     *
     * @param name the name of the supplier to check for existence
     * @return {@code true} if a supplier with the specified name exists, {@code false} otherwise
     */
    public boolean existByName(String name) {
        return repo.existsByName(name);
    }

    /**
     * Checks if a {@link Supplier} entity exists with the given ID.
     *
     * This method verifies whether a {@link Supplier} with the specified ID exists in the database
     * by calling the repository's existsById method.
     *
     * @param id the ID of the supplier to check for existence
     * @return {@code true} if a supplier with the specified ID exists, {@code false} otherwise
     */
    public boolean existById(int id) {
        return repo.existsById(id);
    }

    /**
     * Counts the total number of {@link Supplier} entities in the database.
     *
     * This method returns the count of all {@link Supplier} entities by calling the repository's
     * count method.
     *
     * @return the total number of suppliers
     */
    public long countAll() {
        return repo.count();
    }

    /**
     * Adds a new {@link Supplier} based on the provided {@link SupplierDto}.
     *
     * @param supplierDto the data transfer object containing the details of the supplier to be added
     * @param result the binding result containing any validation errors
     * @throws ValidationException if there are validation errors in the {@link BindingResult}
     * @throws NameExistException if a supplier with the same name already exists
     */
    public void add(SupplierDto supplierDto, BindingResult result) throws ValidationException, NameExistException {
        throwValidationException(result, "/add-supplier-page");
        throwNameExistException(existByName(supplierDto.getName()), "A supplier with this name already exists. Please choose a different name.", "/add-supplier-page");
        Supplier supplier = new Supplier();
        updateSupplierFields(supplier, supplierDto);
        save(supplier);
    }

    /**
     * Deletes a {@link Supplier} entity by its ID.
     *
     * @param id the ID of the supplier to be deleted
     * @throws NotFoundException if no supplier with the specified ID is found
     * @throws ForeignKeyConstraintViolationException if the supplier cannot be deleted due to associated products
     */
    public void delete(int id) throws NotFoundException, ForeignKeyConstraintViolationException {
        throwNotFoundException(existById(id), "Could not find any supplier with ID " + id, "/supplier-page");
        try {
            deleteById(id);
        } catch (Exception e) {
            throw new ForeignKeyConstraintViolationException("The supplier cannot be deleted because it has associated products.", "/supplier-page");
        }
    }

    /**
     * Edits an existing {@link Supplier} based on the provided {@link SupplierDto}.
     *
     * @param id the ID of the supplier to be edited
     * @param supplierDto the data transfer object containing the new details for the supplier
     * @param result the binding result containing any validation errors
     * @throws ValidationException if there are validation errors in the {@link BindingResult}
     * @throws NotFoundException if no supplier with the specified ID is found
     * @throws NameExistException if a supplier with the new name already exists
     */
    public void edit(int id, SupplierDto supplierDto, BindingResult result) throws ValidationException, NotFoundException, NameExistException {
        throwValidationException(result, "/edit-supplier-page/" + id);
        Supplier supplier = getById(id);
        throwNotFoundException(supplier, "Could not find any supplier with ID" + id, "/edit-supplier-page/" + id);
        throwNameExistException(isSupplierNameChangedAndExists(supplier, supplierDto), "A supplier with this name already exists. Please choose a different name.", "/edit-supplier-page/" + id);
        updateSupplierFields(supplier, supplierDto);
        save(supplier);
    }

    /**
     * Checks if the supplier's name has changed and if the new name already exists.
     *
     * This method compares the current name of the provided {@link Supplier} with the name in the
     * {@link SupplierDto}. If the name has changed and a supplier with the new name already exists
     * in the database, it returns {@code true}. Otherwise, it returns {@code false}.
     *
     * @param supplier the existing {@link Supplier} entity to compare
     * @param supplierDto the data transfer object containing the new supplier name
     * @return {@code true} if the supplier's name has changed and the new name already exists,
     *         {@code false} otherwise
     */
    private boolean isSupplierNameChangedAndExists(Supplier supplier, SupplierDto supplierDto) {
        return !supplier.getName().equals(supplierDto.getName()) && existByName(supplierDto.getName());
    }

    /**
     * Updates the fields of a {@link Supplier} entity with values from the provided {@link SupplierDto}.
     *
     * This method sets the name, address, email, and phone of the given {@link Supplier} entity
     * using the corresponding values from the {@link SupplierDto}.
     *
     * @param supplier the {@link Supplier} entity to be updated
     * @param supplierDto the data transfer object containing the new values for the supplier
     */
    private void updateSupplierFields(Supplier supplier, SupplierDto supplierDto) {
        supplier.setName(supplierDto.getName());
        supplier.setAddress(supplierDto.getAddress());
        supplier.setEmail(supplierDto.getEmail());
        supplier.setPhone(supplierDto.getPhone());
    }
}
