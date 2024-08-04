package com.viser.StockTrade.listeners;

import com.viser.StockTrade.entity.User;
import com.viser.StockTrade.entity.UserProfile;
import com.viser.StockTrade.repository.UserProfileRepository;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PreRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserListener {

    private static UserProfileRepository repo;

    @Autowired
    public void setUserProfileRepository (UserProfileRepository repo){
        UserListener.repo = repo;
    }

    @PostPersist
    public void postPersist(User user){
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        repo.save(userProfile);
    }

    @PreRemove
    public void preRemove(User user){
        UserProfile userProfile = repo.findByUserId(user.getId());
        if (userProfile != null) {
            repo.delete(userProfile);
        }
    }
}
