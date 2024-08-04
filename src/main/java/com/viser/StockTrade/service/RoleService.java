package com.viser.StockTrade.service;

import com.viser.StockTrade.entity.Role;
import com.viser.StockTrade.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository repo;

    public Role getByName(String name) {
        return repo.findByName(name);
    }
}
