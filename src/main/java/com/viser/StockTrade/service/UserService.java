package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.UserDto;
import com.viser.StockTrade.entity.User;
import com.viser.StockTrade.entity.UserProfile;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    @Lazy
    private UserProfileService userProfileService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FileService fileService;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(int id) {
        return userRepository.findById(id);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void getAllUsersDataInModel(Model model, String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            UserProfile userProfile = userProfileService.getUserProfileByUserId(user.getId());
            model.addAttribute("userProfile", userProfile);
            model.addAttribute("username", user.getUsername());
        } else {
            model.addAttribute("errorMessage", "User not found");
        }
    }

    public void delete(int id) throws NotFoundException, IOException {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Could not find any user with ID " + id);
        }
        UserProfile userProfile = userProfileService.getUserProfileByUserId(id);
        if (userProfile.getProfilePictureUrl() != null) {
            fileService.deleteFile(userProfile.getProfilePictureUrl());
        }
        userRepository.findById(id).getRoles().clear();
        userRepository.deleteById(id);
    }

    public void edit(int id, UserDto userDto) throws NotFoundException {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new NotFoundException("Could not find any user with ID " + id);
        }
        updateUserFields(user, userDto);
        userRepository.save(user);
    }

    private void updateUserFields(User user, UserDto userDto) {
        user.setUsername(getNonEmptyValue(userDto.getUsername(), user.getUsername()));
        user.setPassword(passwordEncoder.encode(getNonEmptyValue(userDto.getPassword(), user.getPassword())));
    }

    private String getNonEmptyValue(String newValue, String oldValue) {
        return (newValue != null && !newValue.isEmpty()) ? newValue : oldValue;
    }
}
