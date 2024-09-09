package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.UserProfileDto;
import com.viser.StockTrade.entity.UserProfile;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.io.IOException;

import static com.viser.StockTrade.exceptions.ExceptionHelper.throwValidationException;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository repo;
    private final FileService fileService;

    /**
     * Saves a {@link UserProfile} entity to the database.
     *
     * This method persists the provided {@link UserProfile} entity by calling the repository's
     * save method.
     *
     * @param userProfile the {@link UserProfile} entity to be saved
     */
    public void save(UserProfile userProfile) {
        repo.save(userProfile);
    }

    /**
     * Deletes a {@link UserProfile} entity from the database.
     *
     * This method removes the provided {@link UserProfile} entity from the database
     * by calling the repository's delete method.
     *
     * @param userProfile the {@link UserProfile} entity to be deleted
     */
    public void delete(UserProfile userProfile) {
        repo.delete(userProfile);
    }

    /**
     * Retrieves a {@link UserProfile} entity by the username of the associated user.
     *
     * This method fetches a {@link UserProfile} from the database using the provided username
     * by calling the repository's findByUser_Username method.
     *
     * @param username the username of the user whose profile is to be retrieved
     * @return the {@link UserProfile} entity associated with the specified username,
     *         or {@code null} if no such profile exists
     */
    public UserProfile getByUser_username(String username) {
        return repo.findByUser_Username(username);
    }

    /**
     * Updates the {@link UserProfile} based on the provided {@link UserProfileDto}.
     *
     * @param userProfileDto the data transfer object containing the new details for the user profile
     * @param username the username of the user whose profile is to be updated
     * @param result the binding result containing any validation errors
     * @throws ValidationException if there are validation errors in the {@link BindingResult}
     * @throws IOException if an I/O error occurs while handling the profile picture
     */
    public void updateUserProfile(UserProfileDto userProfileDto, String username, BindingResult result) throws ValidationException, IOException {
        throwValidationException(result, "/edit-profile");
        UserProfile userProfile = getByUser_username(username);
        updateUserProfileFields(userProfileDto, userProfile);
        handleProfilePicture(userProfileDto, userProfile, userProfile.getUser().getId());
        save(userProfile);
    }

    /**
     * Retrieves a {@link UserProfile} entity by the user's ID.
     *
     * This method fetches a {@link UserProfile} from the database using the provided user ID
     * by calling the repository's findByUserId method.
     *
     * @param userId the ID of the user whose profile is to be retrieved
     * @return the {@link UserProfile} entity associated with the specified user ID,
     *         or {@code null} if no such profile exists
     */
    public UserProfile getUserProfileByUserId(int userId) {
        return repo.findByUserId(userId);
    }

    /**
     * Updates the fields of a {@link UserProfile} entity with values from the provided {@link UserProfileDto}.
     *
     * This method sets the first name, last name, address, gender, and date of birth of the given
     * {@link UserProfile} entity using the corresponding values from the {@link UserProfileDto}.
     *
     * @param userProfileDto the data transfer object containing the new values for the user profile
     * @param userProfile the {@link UserProfile} entity to be updated
     */
    private void updateUserProfileFields(UserProfileDto userProfileDto, UserProfile userProfile) {
        userProfile.setFirstName(userProfileDto.getFirstName());
        userProfile.setLastName(userProfileDto.getLastName());
        userProfile.setAddress(userProfileDto.getAddress());
        userProfile.setGender(userProfileDto.getGender());
        userProfile.setDateOfBirth(userProfileDto.getDateOfBirth());
    }

    /**
     * Handles the profile picture for the {@link UserProfile}.
     *
     * This method updates the profile picture of the given {@link UserProfile} entity based on
     * the data in the provided {@link UserProfileDto}. If a new profile picture is provided, it
     * replaces any existing profile picture and updates the URL for the new picture.
     *
     * @param userProfileDto the data transfer object containing the new profile picture
     * @param userProfile the {@link UserProfile} entity to be updated
     * @param userId the ID of the user, used to generate the filename for the profile picture
     * @throws IOException if an I/O error occurs while handling the profile picture
     */
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
