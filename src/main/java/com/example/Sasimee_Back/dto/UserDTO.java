package com.example.Sasimee_Back.dto;

import com.example.Sasimee_Back.entity.Gender;
import com.example.Sasimee_Back.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


import java.util.List;

public class UserDTO {
    @Data
    @Schema(description = "로그인 요청을 위한 요청 정보")
    public static class loginRequest {
        @NotBlank(message = "이메일은 필수 항목입니다.")
        @Email
        @Schema(example = "로그인을 위한 email")
        private String email;
        @Schema(example = "로그인을 위한 password")
        @NotBlank(message = "비밀번호는 필수 항목입니다.")
        private String password;
    }

    @Data
    @Schema(description = "회원가입을 위한 요청 정보")
    public static class registerRequest {
        @NotBlank(message = "이메일은 필수 항목입니다.")
        @Email
        @Schema(example = "이메일")
        private String email;

        @NotBlank(message = "비밀번호는 필수 항목입니다.")
        @Schema(example = "패스워드")
        private String password1;

        @Schema(example = "확인용 패스워드")
        @NotBlank(message = "확인을 위한 비밀번호는 필수 항목입니다.")
        private String password2;

        //@Schema(example = "성별")
        //@NotBlank(message = "성별 정보는 필수 항목입니다.")
        //private String gender;

        @Schema(example = "이름")
        @NotBlank(message = "이름은 필수 항목입니다.")
        private String name;

        @Schema(example = "전화번호")
        @NotBlank(message = "전화번호는 필수 항목입니다.")
        private String phoneNumber;

        //@Schema(example = "주소")
        //@NotBlank(message = "주소는 필수 항목입니다.")
        //private String address;

        //@Schema(example = "생년월일")
        //@NotBlank(message = "주소는 필수 항목입니다.")
        //private String birth;

        @Schema(example="태그")
        @NotBlank(message = "주소는 태그는 필수 항목입니다.")
        private List<TagDTO.TagRequest> tags;

        public static User toEntity(UserDTO.registerRequest registerRequest)
        {
            return User.builder().name(registerRequest.name)
                    .email(registerRequest.email)
                    //gender(Gender.fromString(registerRequest.gender))
                    //.address(registerRequest.address)
                    .phoneNumber(registerRequest.phoneNumber)
                    //.birth(registerRequest.birth)
                    .encryptPassword(registerRequest.password1)
                    .build();
        }

    }

    @Builder
    @Data
    @Schema(description = "회원가입 성공 시 프론트에게 넘겨주는 정보")
    public static class registerResponse {
        @Schema(example = "회원가입 성공 여부")
        private boolean status;

        @Schema(example = "회원가입 성공 여부에 따른 메시지")
        private String message;
    }

    @Builder
    @Data
    @Schema(description = "프로필 정보 변경을 위한 정보")
    public static class profileRequest {
        @Schema(example = "이름")
        private String name;

        @Schema(example = "전화번호")
        private String phonenumber;
    }

    @Builder
    @Data
    @Schema(description = "프로필 조회 시 프론트에게 넘겨주는 정보")
    public static class profileResponse {
        @Schema(example ="이메일")
        private String email;

        @Schema(example = "이름")
        private String name;

        @Schema(example = "전화번호")
        private String phonenumber;
    }

}
