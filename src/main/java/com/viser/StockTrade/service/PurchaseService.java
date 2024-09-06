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

    public void save(Purchase purchase) {
        repo.save(purchase);
    }

    public List<Purchase> getAll() {
        return repo.findAll();
    }

    public Purchase getById(int id) {
        return repo.findById(id);
    }

    public void add(PurchaseDto purchaseDto, BindingResult result) throws ValidationException {
        throwValidationException(result, "/add-purchase-page");
        Purchase purchase = new Purchase();
        updatePurchaseFields(purchase, purchaseDto);
        updateQuantityInProduct(purchase.getPurchaseItems());
        save(purchase);
    }

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

    private void updateQuantityInProduct(List<PurchaseItem> purchaseItems) {
        for (PurchaseItem purchaseItem : purchaseItems) {
            Product product = productService.getByName(purchaseItem.getProductName());
            product.setStockQuantity(product.getStockQuantity() + purchaseItem.getQuantity());
            productService.save(product);
        }
    }

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
