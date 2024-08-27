package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.UserProfileDto;
import com.viser.StockTrade.entity.User;
import com.viser.StockTrade.entity.UserProfile;
import com.viser.StockTrade.exceptions.ExceptionHelper;
import com.viser.StockTrade.exceptions.NotFoundException;
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
    private final UserProfileRepository userProfileRepository;
    @Lazy
    @Autowired
    private UserService userService;
    private final FileService fileService;

    public void save(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    public UserProfile getUserProfileByUsername(String username) {
        User user = userService.getByUsername(username);
        if (user != null) {
            return userProfileRepository.findByUserId(user.getId());
        }
        return null;
    }


    public void delete(UserProfile userProfile) {
        userProfileRepository.delete(userProfile);
    }

    public void updateUserProfile(UserProfileDto userProfileDto, String username, BindingResult result) throws ValidationException, IOException {
        ExceptionHelper.throwValidationException(result, "/edit-profile");
        User user = getUserByUsername(username);
        UserProfile userProfile = getUserProfileByUserId(user.getId());
        updateUserProfileFields(userProfileDto, userProfile);
        handleProfilePicture(userProfileDto, userProfile, user.getId());

        userProfileRepository.save(userProfile);
    }

    private User getUserByUsername(String username) {
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        return user;
    }

    public UserProfile getUserProfileByUserId(int userId) {
        return userProfileRepository.findByUserId(userId);
    }

    private void updateUserProfileFields(UserProfileDto userProfileDto, UserProfile userProfile) {
        userProfile.setFirstName(getNonEmptyValue(userProfileDto.getFirstName(), userProfile.getFirstName()));
        userProfile.setLastName(getNonEmptyValue(userProfileDto.getLastName(), userProfile.getLastName()));
        userProfile.setAddress(getNonEmptyValue(userProfileDto.getAddress(), userProfile.getAddress()));
        userProfile.setGender(userProfileDto.getGender());
        userProfile.setDateOfBirth(userProfileDto.getDateOfBirth() != null ? userProfileDto.getDateOfBirth() : userProfile.getDateOfBirth());
    }

    private String getNonEmptyValue(String newValue, String oldValue) {
        return (newValue != null && !newValue.isEmpty()) ? newValue : oldValue;
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
