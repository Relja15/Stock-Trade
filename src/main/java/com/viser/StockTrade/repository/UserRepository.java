package com.viser.StockTrade.repository;

import com.viser.StockTrade.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(int id);
    User findByUsername(String username);
    Boolean existsByUsername(String username);
    Integer countById(int id);
}
