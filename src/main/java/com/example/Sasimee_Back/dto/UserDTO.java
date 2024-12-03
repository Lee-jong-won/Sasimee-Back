package com.example.Sasimee_Back.dto;

import com.example.Sasimee_Back.entity.Gender;
import com.example.Sasimee_Back.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

public class UserDTO {
    @Data
    public static class loginRequest {
        @NotBlank(message = "이메일은 필수 항목입니다.")
        @Email
        private String email;
        @NotBlank(message = "비밀번호는 필수 항목입니다.")
        private String password;
    }

    @Builder
    @Data
    public static class loginResponse {
        private String email;
        private String gender;
        private String name;
        private String phoneNumber;
        private String address;
    }

    @Data
    public static class registerRequest {
        @NotBlank(message = "이메일은 필수 항목입니다.")
        @Email
        private String email;
        @NotBlank(message = "비밀번호는 필수 항목입니다.")
        private String password1;
        @NotBlank(message = "확인을 위한 비밀번호는 필수 항목입니다.")
        private String password2;
        @NotBlank(message = "성별 정보는 필수 항목입니다.")
        private String gender;
        @NotBlank(message = "이름은 필수 항목입니다.")
        private String name;
        @NotBlank(message = "전화번호는 필수 항목입니다.")
        private String phoneNumber;
        @NotBlank(message = "주소는 필수 항목입니다.")
        private String address;

        public static User toEntity(UserDTO.registerRequest registerRequest)
        {
            return User.builder().name(registerRequest.name)
                    .email(registerRequest.email)
                    .gender(Gender.fromString(registerRequest.gender))
                    .address(registerRequest.address)
                    .phoneNumber(registerRequest.phoneNumber)
                    .encryptPassword(registerRequest.password1)
                    .build();
        }

    }

    @Builder
    @Data
    public static class registerResponse {
        private String message;
        private String email;
        private String name;
        private String redirectURL;
    }

}
