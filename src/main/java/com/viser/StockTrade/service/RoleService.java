package com.viser.StockTrade.service;

import com.viser.StockTrade.entity.Role;
import com.viser.StockTrade.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository repo;

    public Role getByName(String name) {
        return repo.findByName(name);
    }
}
