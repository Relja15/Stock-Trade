package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.CategoryDto;
import com.viser.StockTrade.dto.SupplierDto;
import com.viser.StockTrade.entity.Category;
import com.viser.StockTrade.entity.Product;
import com.viser.StockTrade.entity.Supplier;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ProductService productService;

    public List<Supplier> getAll(){
        return supplierRepository.findAll();
    }

    public Supplier getById(int id) { return supplierRepository.findById(id); }

    public boolean existByName(String name){
        return supplierRepository.existsByName(name);
    }

    public void add(SupplierDto supplierDto){
        Supplier supplier = new Supplier();
        updateSupplierFields(supplier, supplierDto);
        supplierRepository.save(supplier);
    }

    public boolean delete(int id) throws NotFoundException {
        Supplier supplier = supplierRepository.findById(id);
        if(supplier == null){
            throw new NotFoundException("Could not find any supplier with ID" + id);
        }
        List<Product> products = productService.getProductsBySupplierId(id);
        if(products.isEmpty()){
            supplierRepository.delete(supplier);
            return true;
        }
        return false;
    }

    public void edit(int id, SupplierDto supplierDto) throws NotFoundException {
        Supplier supplier = getById(id);
        if(supplier == null){
            throw new NotFoundException("Could not find any supplier with ID" + id);
        }
        updateSupplierFields(supplier, supplierDto);
        supplierRepository.save(supplier);
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
