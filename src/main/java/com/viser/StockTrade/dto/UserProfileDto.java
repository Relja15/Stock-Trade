package com.viser.StockTrade.dto;

import com.viser.StockTrade.enums.Gender;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class UserProfileDto {
    private MultipartFile profilePicture;
    private String firstName;
    private String lastName;
    private String address;
    private Gender gender;
    private LocalDate dateOfBirth;
    private int userId;
}
