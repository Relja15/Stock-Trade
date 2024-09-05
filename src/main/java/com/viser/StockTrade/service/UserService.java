package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.UserDto;
import com.viser.StockTrade.entity.User;
import com.viser.StockTrade.entity.UserProfile;
import com.viser.StockTrade.exceptions.ExceptionHelper;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    @Lazy
    @Autowired
    private UserProfileService userProfileService;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    public List<User> getAll() {
        return repo.findAll();
    }

    public User getById(int id) {
        return repo.findById(id);
    }

    public void save(User user) {
        repo.save(user);
    }

    public void deleteById(int id){
        repo.deleteById(id);
    }

    public User getByUsername(String username) {
        return repo.findByUsername(username);
    }

    public Boolean existByUsername(String username) {
        return repo.existsByUsername(username);
    }

    public void getAllUsersDataInModel(Model model, String username) {
        User user = repo.findByUsername(username);
        if (user != null) {
            UserProfile userProfile = userProfileService.getUserProfileByUserId(user.getId());
            model.addAttribute("userProfile", userProfile);
            model.addAttribute("username", user.getUsername());
        } else {
            model.addAttribute("error", "User not found");
        }
    }

    public void delete(int id) throws NotFoundException, IOException {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Could not find any user with ID " + id, "/users-page");
        }
        UserProfile userProfile = userProfileService.getUserProfileByUserId(id);
        if (userProfile.getProfilePictureUrl() != null) {
            fileService.deleteFile(userProfile.getProfilePictureUrl());
        }
        getById(id).getRoles().clear();
        deleteById(id);
    }

    public void edit(int id, UserDto userDto, BindingResult result) throws ValidationException, NotFoundException, NameExistException{
        ExceptionHelper.throwValidationException(result, "/edit-user-page/" + id);
        User user = repo.findById(id);
        if (user == null) {
            throw new NotFoundException("Could not find any user with ID " + id, "/edit-user-page/" + id);
        }
        if(!user.getUsername().equals(userDto.getUsername()) && existByUsername(userDto.getUsername())) {
            throw new NameExistException("A user with this username already exists. Please choose a different username.", "/edit-user-page/" + id);
        }
        updateUserFields(user, userDto);
        save(user);
    }

    private void updateUserFields(User user, UserDto userDto) {
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    }
}
