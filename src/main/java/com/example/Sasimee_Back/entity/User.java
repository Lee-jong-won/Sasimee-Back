package com.example.Sasimee_Back.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Setter
    private String encryptPassword;

    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    @Column(nullable = false)
    private Gender gender;

    private String name;
    private String phoneNumber;
    private String address;

    /*public static UserDTO.loginResponse toLoginResponseDTO(User user)
    {

    }*/

}
