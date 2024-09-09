package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.PurchaseDto;
import com.viser.StockTrade.dto.PurchasesItemDto;
import com.viser.StockTrade.entity.Product;
import com.viser.StockTrade.entity.Purchase;
import com.viser.StockTrade.entity.PurchaseItem;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

import static com.viser.StockTrade.exceptions.ExceptionHelper.throwValidationException;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository repo;
    private final ProductService productService;

    /**
     * Saves a purchase record to the database.
     *
     * This method persists the provided {@link Purchase} entity by calling the repository's save method.
     *
     * @param purchase the purchase entity to be saved
     */
    public void save(Purchase purchase) {
        repo.save(purchase);
    }

    /**
     * Retrieves all purchase records from the database.
     *
     * This method returns a list of all {@link Purchase} entities by calling the repository's findAll method.
     *
     * @return a list of all purchases
     */
    public List<Purchase> getAll() {
        return repo.findAll();
    }

    /**
     * Retrieves a purchase record by its ID.
     *
     * This method fetches a {@link Purchase} entity from the database based on the provided ID.
     *
     * @param id the ID of the purchase to retrieve
     * @return the purchase entity with the specified ID, or {@code null} if no such entity exists
     */
    public Purchase getById(int id) {
        return repo.findById(id);
    }

    /**
     * Adds a new purchase based on the provided {@link PurchaseDto}.
     *
     * @param purchaseDto the data transfer object containing the details of the purchase to be added
     * @param result the binding result containing any validation errors
     * @throws ValidationException if there are validation errors in the {@link BindingResult}
     */
    public void add(PurchaseDto purchaseDto, BindingResult result) throws ValidationException {
        throwValidationException(result, "/add-purchase-page");
        Purchase purchase = new Purchase();
        updatePurchaseFields(purchase, purchaseDto);
        updateQuantityInProduct(purchase.getPurchaseItems());
        save(purchase);
    }

    /**
     * Retrieves a {@link PurchaseDto} based on the provided purchase ID.
     *
     * This method fetches a {@link Purchase} entity by its ID using {@link #getById(int)},
     * and then maps it to a {@link PurchaseDto} containing relevant purchase details including
     * the supplier name, date, total amount, and a list of purchase items.
     *
     * @param id the ID of the purchase to retrieve
     * @return a {@link PurchaseDto} containing the details of the purchase
     */
    public PurchaseDto getPurchaseInDto(int id) {
        Purchase purchase = getById(id);
        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.setId(purchase.getId());
        purchaseDto.setSupplier(purchase.getSupplierName());
        purchaseDto.setDate(purchase.getDate());
        purchaseDto.setTotalAmount(purchase.getTotalAmount());
        purchaseDto.setPurchaseItems(purchase.getPurchaseItems().stream()
                .map(item -> new PurchasesItemDto(item.getProductName(), item.getQuantity(), item.getPrice()))
                .collect(Collectors.toList()));

        return purchaseDto;
    }

    /**
     * Updates the stock quantity of products based on the provided purchase items.
     *
     * This method iterates over the given list of {@link PurchaseItem} entities, retrieves the corresponding
     * {@link Product} for each item, updates the product's
     * stock quantity by adding the quantity from the purchase item, and then saves the updated product
     *
     * @param purchaseItems a list of {@link PurchaseItem} entities containing the products and their quantities
     */
    private void updateQuantityInProduct(List<PurchaseItem> purchaseItems) {
        for (PurchaseItem purchaseItem : purchaseItems) {
            Product product = productService.getByName(purchaseItem.getProductName());
            product.setStockQuantity(product.getStockQuantity() + purchaseItem.getQuantity());
            productService.save(product);
        }
    }

    /**
     * Updates the fields of a {@link Purchase} entity with values from the provided {@link PurchaseDto}.
     *
     * This method sets the supplier name, date, and total amount of the purchase. It also maps
     * the list of {@link PurchasesItemDto} from the DTO to a list of {@link PurchaseItem} entities,
     * setting their product information, category, quantity, and price. The {@link Product} is retrieved
     * by its name, and each {@link PurchaseItem} is associated
     * with the given {@link Purchase}.
     *
     * @param purchase the {@link Purchase} entity to be updated
     * @param purchaseDto the data transfer object containing the new values for the purchase
     */
    private void updatePurchaseFields(Purchase purchase, PurchaseDto purchaseDto) {
        purchase.setSupplierName(purchaseDto.getSupplier());
        purchase.setDate(purchaseDto.getDate());
        purchase.setTotalAmount(purchaseDto.getTotalAmount());

        List<PurchaseItem> purchaseItems = purchaseDto.getPurchaseItems().stream().map(dto -> {
            PurchaseItem item = new PurchaseItem();
            Product product = productService.getByName(dto.getProduct());
            item.setProductName(dto.getProduct());
            item.setProductCategory(product.getCategory().getName());
            item.setQuantity(dto.getQuantity());
            item.setPrice(dto.getPrice());
            item.setPurchase(purchase);
            return item;
        }).collect(Collectors.toList());

        purchase.setPurchaseItems(purchaseItems);
    }
}
