package com.viser.StockTrade.dto;

import com.viser.StockTrade.enums.Gender;
import com.viser.StockTrade.validation.BasicValidation;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Validated
public class UserProfileDto {
    private MultipartFile profilePicture;
    @BasicValidation(message = "First name must be at least {minLength} characters long and cannot be just spaces.")
    private String firstName;
    @BasicValidation(message = "Last name must be at least {minLength} characters long and cannot be just spaces.")
    private String lastName;
    @BasicValidation(minLength = 5, message = "Address must be at least {minLength} characters long and cannot be just spaces.")
    private String address;
    private Gender gender;
    private LocalDate dateOfBirth;
    private int userId;
}
