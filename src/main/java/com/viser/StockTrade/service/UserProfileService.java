package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.UserProfileDto;
import com.viser.StockTrade.entity.User;
import com.viser.StockTrade.entity.UserProfile;
import com.viser.StockTrade.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public void save(UserProfile userProfile){
        userProfileRepository.save(userProfile);
    }

    public UserProfile getUserProfileByUsername(String username){
        User user = userService.getByUsername(username);
        if(user != null){
            return userProfileRepository.findByUserId(user.getId());
        }
        return null;
    }


    public void delete(UserProfile userProfile){
        userProfileRepository.delete(userProfile);
    }

    public UserProfile getUserProfileByUserId(int userId){
        return userProfileRepository.findByUserId(userId);
    }

    public void updateUserProfile(UserProfileDto userProfileDto, String username) throws IOException {
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        UserProfile userProfile = userProfileRepository.findByUserId(user.getId());
        if (userProfile == null) {
            throw new RuntimeException("User not found");
        }

        userProfile.setFirstName(!userProfileDto.getFirstName().isEmpty() ? userProfileDto.getFirstName() : userProfile.getFirstName());
        userProfile.setLastName(!userProfileDto.getLastName().isEmpty() ? userProfileDto.getLastName() : userProfile.getLastName());
        userProfile.setAddress(!userProfileDto.getAddress().isEmpty() ? userProfileDto.getAddress() : userProfile.getAddress());
        userProfile.setGender(userProfileDto.getGender());
        userProfile.setDateOfBirth(userProfileDto.getDateOfBirth() != null ?  userProfileDto.getDateOfBirth() : userProfile.getDateOfBirth());

        if (userProfileDto.getProfilePicture() != null && !userProfileDto.getProfilePicture().isEmpty()) {
            String filename = user.getId() + "_" + userProfileDto.getProfilePicture().getOriginalFilename();
            fileService.uploadFile(userProfileDto.getProfilePicture(), filename);
            userProfile.setProfilePictureUrl("/uploads/" + filename);
        }

        userProfileRepository.save(userProfile);
    }
}
