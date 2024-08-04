package com.viser.StockTrade.service;

import com.viser.StockTrade.entity.User;
import com.viser.StockTrade.entity.UserProfile;
import com.viser.StockTrade.exceptions.UserNotFoundException;
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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getById(int id) throws UserNotFoundException {
        User user = userRepository.findById(id);
        if(user != null) {
            return userRepository.findById(id);
        }
        throw new UserNotFoundException("Could not found user with ID " + id);
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

    public void save(User user) {
        userRepository.save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void delete(int id) throws UserNotFoundException, IOException {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new UserNotFoundException("Could not find any user with ID: " + id);
        }
        UserProfile userProfile = userProfileService.getUserProfileByUserId(id);
        fileService.deleteFile(userProfile.getProfilePictureUrl());
        user.getRoles().clear();
        userRepository.deleteById(id);
    }

    public void edit(int id, String username, String password) throws UserNotFoundException {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
