package com.viser.StockTrade.entity;

import com.viser.StockTrade.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "user_profile")
@Data
@NoArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "profile_picture")
    private String profilePictureUrl;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String address;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "dateOfBirth")
    private LocalDate dateOfBirth;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
