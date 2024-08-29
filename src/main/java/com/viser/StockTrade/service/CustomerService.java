package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.CustomerDto;
import com.viser.StockTrade.entity.Customer;
import com.viser.StockTrade.exceptions.ExceptionHelper;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repo;

    public List<Customer> getAll(){
        return repo.findAll();
    }

    public Customer getById(int id){
        return repo.findById(id);
    }

    public boolean existByName(String name){
        return repo.existsByName(name);
    }

    public boolean existById(int id){
        return repo.existsById(id);
    }

    public void add(CustomerDto customerDto, BindingResult result) throws ValidationException, NameExistException {
        ExceptionHelper.throwValidationException(result, "/add-customer-page");
        if(existByName(customerDto.getName())){
            throw new NameExistException("A customer with this name already exists. Please choose a different name.", "/add-customer-page");
        }
        Customer customer = new Customer();
        updateSupplierFields(customer, customerDto);
        repo.save(customer);
    }

    public void delete(int id) throws NotFoundException {
        if(!existById(id)){
            throw new NotFoundException("Could not find any customer with ID " + id, "/customer-page");
        }
        repo.deleteById(id);
    }

    public void edit(int id, CustomerDto customerDto, BindingResult result) throws ValidationException, NotFoundException, NameExistException{
        ExceptionHelper.throwValidationException(result, "/edit-customer-page/" + id);
        Customer customer = getById(id);
        if(customer == null){
            throw new NotFoundException("Could not find any customer with ID" + id, "/edit-customer-page/" + id);
        }
        if(customer.getName().equals(customerDto.getName()) && existByName(customerDto.getName())){
            throw new NameExistException("A customer with this name already exists. Please choose a different name.", "/edit-customer-page/" + id);
        }
        updateSupplierFields(customer, customerDto);
        repo.save(customer);
    }

    private void updateSupplierFields(Customer customer, CustomerDto customerDto) {
        customer.setName(getNonEmptyValue(customerDto.getName(), customer.getName()));
        customer.setAddress(getNonEmptyValue(customerDto.getAddress(), customer.getAddress()));
        customer.setEmail(getNonEmptyValue(customerDto.getEmail(), customer.getEmail()));
        customer.setPhone(getNonEmptyValue(customerDto.getPhone(), customer.getPhone()));
    }

    private String getNonEmptyValue(String newValue, String oldValue) {
        return (newValue != null && !newValue.isEmpty()) ? newValue : oldValue;
    }
}
