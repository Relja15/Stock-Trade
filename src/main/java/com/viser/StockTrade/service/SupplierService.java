package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.SupplierDto;
import com.viser.StockTrade.entity.Supplier;
import com.viser.StockTrade.exceptions.*;
import com.viser.StockTrade.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {
    private final SupplierRepository repo;
    private final ProductService productService;

    public List<Supplier> getAll(){
        return repo.findAll();
    }

    public Supplier getById(int id) { return repo.findById(id); }

    public Supplier getByName(String name){
        return repo.findByName(name);
    }

    public boolean existByName(String name){
        return repo.existsByName(name);
    }

    public boolean existById(int id){
        return repo.existsById(id);
    }

    public long countAll(){
        return repo.count();
    }



    public void add(SupplierDto supplierDto, BindingResult result) throws ValidationException, NameExistException {
        ExceptionHelper.throwValidationException(result, "/add-supplier-page");
        if(existByName(supplierDto.getName())){
            throw new NameExistException("A supplier with this name already exists. Please choose a different name.", "/add-supplier-page");
        }
        Supplier supplier = new Supplier();
        updateSupplierFields(supplier, supplierDto);
        repo.save(supplier);
    }

    public void delete(int id) throws NotFoundException, ForeignKeyConstraintViolationException {
        if(!existById(id)){
            throw new NotFoundException("Could not find any supplier with ID " + id, "/supplier-page");
        }
        if(productService.existBySupplierId(id)){
            throw new ForeignKeyConstraintViolationException("The supplier cannot be deleted because it has associated products.", "/supplier-page");
        }
        repo.deleteById(id);
    }

    public void edit(int id, SupplierDto supplierDto, BindingResult result) throws ValidationException, NotFoundException, NameExistException {
        ExceptionHelper.throwValidationException(result, "/edit-supplier-page/" + id);
        Supplier supplier = getById(id);
        if(supplier == null){
            throw new NotFoundException("Could not find any supplier with ID" + id, "/edit-supplier-page/" + id);
        }
        if(supplier.getName().equals(supplierDto.getName()) && existByName(supplierDto.getName())){
            throw new NameExistException("A supplier with this name already exists. Please choose a different name.", "/edit-supplier-page/" + id);
        }
        updateSupplierFields(supplier, supplierDto);
        repo.save(supplier);
    }

    private void updateSupplierFields(Supplier supplier, SupplierDto supplierDto) {
        supplier.setName(getNonEmptyValue(supplierDto.getName(), supplier.getName()));
        supplier.setAddress(getNonEmptyValue(supplierDto.getAddress(), supplier.getAddress()));
        supplier.setEmail(getNonEmptyValue(supplierDto.getEmail(), supplier.getEmail()));
        supplier.setPhone(getNonEmptyValue(supplierDto.getPhone(), supplier.getPhone()));
    }

    private String getNonEmptyValue(String newValue, String oldValue) {
        return (newValue != null && !newValue.isEmpty()) ? newValue : oldValue;
    }
}
