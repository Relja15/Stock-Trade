package com.viser.StockTrade.service;

import com.viser.StockTrade.entity.Role;
import com.viser.StockTrade.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository repo;

    /**
     * Retrieves a {@link Role} entity by its name.
     *
     * This method fetches a {@link Role} from the database using the provided name.
     *
     * @param name the name of the role to retrieve
     * @return the {@link Role} entity with the specified name, or {@code null} if no such role exists
     */
    public Role getByName(String name) {
        return repo.findByName(name);
    }
}
