package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.PurchasesItemDto;
import com.viser.StockTrade.entity.Purchase;
import com.viser.StockTrade.entity.PurchaseItem;
import com.viser.StockTrade.repository.PurchaseItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseItemService {
    private final PurchaseItemRepository repo;

    public List<PurchaseItem> getAll(){
        return repo.findAll();
    }
}
