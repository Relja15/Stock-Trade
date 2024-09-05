package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.UserProfileDto;
import com.viser.StockTrade.entity.User;
import com.viser.StockTrade.entity.UserProfile;
import com.viser.StockTrade.exceptions.ExceptionHelper;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository repo;
    @Lazy
    @Autowired
    private UserService userService;
    private final FileService fileService;

    public void save(UserProfile userProfile) {
        repo.save(userProfile);
    }

    public void delete(UserProfile userProfile) {
        repo.delete(userProfile);
    }

    public void updateUserProfile(UserProfileDto userProfileDto, String username, BindingResult result) throws ValidationException, IOException {
        ExceptionHelper.throwValidationException(result, "/edit-profile");
        User user = userService.getByUsername(username);
        UserProfile userProfile = getUserProfileByUserId(user.getId());
        updateUserProfileFields(userProfileDto, userProfile);
        handleProfilePicture(userProfileDto, userProfile, user.getId());

        save(userProfile);
    }

    public UserProfile getUserProfileByUserId(int userId) {
        return repo.findByUserId(userId);
    }

    private void updateUserProfileFields(UserProfileDto userProfileDto, UserProfile userProfile) {
        userProfile.setFirstName(userProfileDto.getFirstName());
        userProfile.setLastName(userProfileDto.getLastName());
        userProfile.setAddress(userProfileDto.getAddress());
        userProfile.setGender(userProfileDto.getGender());
        userProfile.setDateOfBirth(userProfileDto.getDateOfBirth());
    }

    private void handleProfilePicture(UserProfileDto userProfileDto, UserProfile userProfile, int userId) throws IOException {
        if (userProfileDto.getProfilePicture() != null && !userProfileDto.getProfilePicture().isEmpty()) {
            if (userProfile.getProfilePictureUrl() != null && !userProfile.getProfilePictureUrl().isEmpty()) {
                fileService.deleteFile(userProfile.getProfilePictureUrl());
            }
            String filename = userId + "_" + userProfileDto.getProfilePicture().getOriginalFilename();
            fileService.uploadFile(userProfileDto.getProfilePicture(), filename);
            userProfile.setProfilePictureUrl("/uploads/" + filename);
        }
    }
}
