package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.UserDto;
import com.viser.StockTrade.entity.User;
import com.viser.StockTrade.entity.UserProfile;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.util.List;

import static com.viser.StockTrade.exceptions.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    private final UserProfileService userProfileService;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    /**
     * Retrieves a list of all {@link User} entities from the database.
     *
     * This method fetches all {@link User} entities by calling the repository's findAll method.
     *
     * @return a {@link List} of all {@link User} entities
     */
    public List<User> getAll() {
        return repo.findAll();
    }

    /**
     * Retrieves a {@link User} entity by its ID.
     *
     * This method fetches a {@link User} from the database using the provided ID by calling the repository's
     * findById method.
     *
     * @param id the ID of the user to be retrieved
     * @return the {@link User} entity with the specified ID, or {@code null} if no such user exists
     */
    public User getById(int id) {
        return repo.findById(id);
    }

    /**
     * Saves a {@link User} entity to the database.
     *
     * This method persists the provided {@link User} entity by calling the repository's
     * save method.
     *
     * @param user the {@link User} entity to be saved
     */
    public void save(User user) {
        repo.save(user);
    }

    /**
     * Deletes a {@link User} entity by its ID.
     *
     * This method removes the {@link User} entity with the specified ID from the database
     * by calling the repository's deleteById method.
     *
     * @param id the ID of the user to be deleted
     */
    public void deleteById(int id) {
        repo.deleteById(id);
    }

    /**
     * Checks if a {@link User} entity exists in the database by its ID.
     *
     * This method determines whether a {@link User} with the specified ID exists by
     * calling the repository's existsById method.
     *
     * @param id the ID of the user to check for existence
     * @return {@code true} if a user with the specified ID exists, {@code false} otherwise
     */
    public boolean existById(int id) {
        return repo.existsById(id);
    }

    /**
     * Retrieves a {@link User} entity by its username.
     *
     * This method fetches a {@link User} from the database using the provided username
     * by calling the repository's findByUsername method.
     *
     * @param username the username of the user to be retrieved
     * @return the {@link User} entity with the specified username, or {@code null} if no such user exists
     */
    public User getByUsername(String username) {
        return repo.findByUsername(username);
    }

    /**
     * Checks if a {@link User} entity exists in the database by its username.
     *
     * This method determines whether a {@link User} with the specified username exists
     * by calling the repository's existsByUsername method.
     *
     * @param username the username of the user to check for existence
     * @return {@code true} if a user with the specified username exists, {@code false} otherwise
     */
    public Boolean existByUsername(String username) {
        return repo.existsByUsername(username);
    }

    /**
     * Retrieves and adds user data to the {@link Model} for the specified username.
     *
     * @param model the {@link Model} object to which the user data and any error messages are added
     * @param username the username of the user whose data is to be retrieved
     */
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

    /**
     * Deletes a {@link User} entity by its ID.
     *
     * @param id the ID of the user to be deleted
     * @throws NotFoundException if no user with the specified ID exists
     * @throws IOException if an I/O error occurs while deleting the profile picture file
     */
    public void delete(int id) throws NotFoundException, IOException {
        throwNotFoundException(existById(id), "Could not find any user with ID " + id, "/users-page");
        UserProfile userProfile = userProfileService.getUserProfileByUserId(id);
        if (userProfile.getProfilePictureUrl() != null) {
            fileService.deleteFile(userProfile.getProfilePictureUrl());
        }
        getById(id).getRoles().clear();
        deleteById(id);
    }

    /**
     * Edits a {@link User} entity based on the provided {@link UserDto}.
     *
     * @param id the ID of the user to be edited
     * @param userDto the data transfer object containing the new user details
     * @param result the binding result containing any validation errors
     * @throws ValidationException if there are validation errors in the {@link BindingResult}
     * @throws NotFoundException if no user with the specified ID exists
     * @throws NameExistException if a user with the new username already exists
     */
    public void edit(int id, UserDto userDto, BindingResult result) throws ValidationException, NotFoundException, NameExistException {
        throwValidationException(result, "/edit-user-page/" + id);
        User user = repo.findById(id);
        throwNotFoundException(user, "Could not find any user with ID " + id, "/edit-user-page/" + id);
        throwNameExistException(isUsernameNameChangedAndExists(user, userDto), "A user with this username already exists. Please choose a different username.", "/edit-user-page/" + id);
        updateUserFields(user, userDto);
        save(user);
    }

    /**
     * Checks if the username has changed and if a user with the new username already exists.
     *
     * This method compares the current username of the provided {@link User} entity with the username
     * in the provided {@link UserDto}. If the usernames are different and a user with the new username
     * already exists in the system, it returns {@code true}; otherwise, it returns {@code false}.
     *
     * @param user the {@link User} entity whose username is being checked
     * @param userDto the data transfer object containing the new username
     * @return {@code true} if the username has changed and a user with the new username already exists,
     *         {@code false} otherwise
     */
    private boolean isUsernameNameChangedAndExists(User user, UserDto userDto) {
        return !user.getUsername().equals(userDto.getUsername()) && existByUsername(userDto.getUsername());
    }

    /**
     * Updates the fields of a {@link User} entity with values from the provided {@link UserDto}.
     *
     * This method sets the username and encoded password of the given {@link User} entity using
     * the corresponding values from the {@link UserDto}.
     *
     * @param user the {@link User} entity to be updated
     * @param userDto the data transfer object containing the new user details
     */
    private void updateUserFields(User user, UserDto userDto) {
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    }
}
