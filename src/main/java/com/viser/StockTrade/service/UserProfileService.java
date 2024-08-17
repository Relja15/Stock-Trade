package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.UserProfileDto;
import com.viser.StockTrade.entity.User;
import com.viser.StockTrade.entity.UserProfile;
import com.viser.StockTrade.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserProfileService {
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    @Lazy
    private UserService userService;
    @Autowired
    private FileService fileService;

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

    public void updateUserProfile(UserProfileDto userProfileDto, String username) throws IOException {
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
        UserProfile userProfile = userProfileRepository.findByUserId(userId);
        if (userProfile == null) {
            throw new RuntimeException("User not found");
        }
        return userProfile;
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
            if (!userProfile.getProfilePictureUrl().isEmpty()) {
                fileService.deleteFile(userProfile.getProfilePictureUrl());
            }
            String filename = userId + "_" + userProfileDto.getProfilePicture().getOriginalFilename();
            fileService.uploadFile(userProfileDto.getProfilePicture(), filename);
            userProfile.setProfilePictureUrl("/uploads/" + filename);
        }
    }
}
