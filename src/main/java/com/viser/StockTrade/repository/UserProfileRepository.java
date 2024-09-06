package com.viser.StockTrade.repository;

import com.viser.StockTrade.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    UserProfile findByUserId(int userId);

    UserProfile findByUser_Username(String username);
}
