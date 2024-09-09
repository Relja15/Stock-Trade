package com.viser.StockTrade.service;

import com.viser.StockTrade.repository.PurchaseItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseItemService {
    private final PurchaseItemRepository repo;

    /**
     * Retrieves the count of purchases grouped by category.
     *
     * This method calls the repository to get the count of purchases for each category, returning
     * a list of objects where each object array contains category-related data and the corresponding
     * purchase count.
     *
     * @return a list of object arrays, where each array contains data for a category and its purchase count
     */
    public List<Object[]> countPurchasesByCategory() {
        return repo.countPurchasesByCategory();
    }

    /**
     * Retrieves the total quantity of purchases for each product.
     *
     * This method calls the repository to sum the quantities of all purchases for each product,
     * returning a list of objects where each object array contains product-related data and the
     * corresponding total quantity.
     *
     * @return a list of object arrays, where each array contains data for a product and the total quantity purchased
     */
    public List<Object[]> sumQuantityByProduct() {
        return repo.sumQuantityByProduct();
    }

    /**
     * Retrieves the total quantity of products supplied by each supplier.
     *
     * This method calls the repository to sum the quantities of products provided by each supplier,
     * returning a list of objects where each object array contains supplier-related data and the
     * corresponding total quantity of products supplied.
     *
     * @return a list of object arrays, where each array contains data for a supplier and the total quantity supplied
     */
    public List<Object[]> sumQuantotyBySupplier() {
        return repo.sumQuantotyBySupplier();
    }
}
