package com.viser.StockTrade.listeners;

import com.viser.StockTrade.entity.User;
import com.viser.StockTrade.entity.UserProfile;
import com.viser.StockTrade.repository.UserProfileRepository;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PreRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserListener {

    private static UserProfileRepository repo;

    @Autowired
    public void setUserProfileRepository(UserProfileRepository repo) {
        UserListener.repo = repo;
    }

    /**
     * Post-persistence callback method that is invoked after a new {@link User} entity has been persisted.
     *
     * This method is automatically called by the persistence context once a {@link User} entity is saved.
     * It creates a new {@link UserProfile} associated with the persisted {@link User} and saves the {@link UserProfile}
     * to the repository.
     *
     * @param user the {@link User} entity that has just been persisted
     */
    @PostPersist
    public void postPersist(User user) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        repo.save(userProfile);
    }

    /**
     * Pre-removal callback method that is invoked before a {@link User} entity is removed.
     *
     * This method is automatically called by the persistence context before a {@link User} entity is deleted.
     * It attempts to find and delete the associated {@link UserProfile} for the given {@link User}.
     *
     * @param user the {@link User} entity that is about to be removed
     */
    @PreRemove
    public void preRemove(User user) {
        UserProfile userProfile = repo.findByUserId(user.getId());
        if (userProfile != null) {
            repo.delete(userProfile);
        }
    }
}
