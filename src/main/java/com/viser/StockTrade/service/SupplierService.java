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

    public void save(Supplier supplier) {
        repo.save(supplier);
    }

    public void deleteById(int id) {
        repo.deleteById(id);
    }

    public List<Supplier> getAll() {
        return repo.findAll();
    }

    public Supplier getById(int id) {
        return repo.findById(id);
    }

    public Supplier getByName(String name) {
        return repo.findByName(name);
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


    public void add(SupplierDto supplierDto, BindingResult result) throws ValidationException, NameExistException {
        throwValidationException(result, "/add-supplier-page");
        throwNameExistException(existByName(supplierDto.getName()), "A supplier with this name already exists. Please choose a different name.", "/add-supplier-page");
        Supplier supplier = new Supplier();
        updateSupplierFields(supplier, supplierDto);
        save(supplier);
    }

    public void delete(int id) throws NotFoundException, ForeignKeyConstraintViolationException {
        throwNotFoundException(existById(id), "Could not find any supplier with ID " + id, "/supplier-page");
        try {
            deleteById(id);
        } catch (Exception e) {
            throw new ForeignKeyConstraintViolationException("The supplier cannot be deleted because it has associated products.", "/supplier-page");
        }
    }

    public void edit(int id, SupplierDto supplierDto, BindingResult result) throws ValidationException, NotFoundException, NameExistException {
        throwValidationException(result, "/edit-supplier-page/" + id);
        Supplier supplier = getById(id);
        throwNotFoundException(supplier, "Could not find any supplier with ID" + id, "/edit-supplier-page/" + id);
        throwNameExistException(isSupplierNameChangedAndExists(supplier, supplierDto), "A supplier with this name already exists. Please choose a different name.", "/edit-supplier-page/" + id);
        updateSupplierFields(supplier, supplierDto);
        save(supplier);
    }

    private boolean isSupplierNameChangedAndExists(Supplier supplier, SupplierDto supplierDto) {
        return !supplier.getName().equals(supplierDto.getName()) && existByName(supplierDto.getName());
    }

    private void updateSupplierFields(Supplier supplier, SupplierDto supplierDto) {
        supplier.setName(supplierDto.getName());
        supplier.setAddress(supplierDto.getAddress());
        supplier.setEmail(supplierDto.getEmail());
        supplier.setPhone(supplierDto.getPhone());
    }
}
