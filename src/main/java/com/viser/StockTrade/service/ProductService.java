package com.viser.StockTrade.service;

import com.viser.StockTrade.entity.Product;
import com.viser.StockTrade.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAll(){
        return  productRepository.findAll();
    }

    public List<Product> findProductsForCategory(int categoryId){
        return productRepository.findAllByCategoryId(categoryId);
    }
}
