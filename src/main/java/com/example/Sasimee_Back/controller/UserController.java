package com.example.Sasimee_Back.controller;


import com.example.Sasimee_Back.dto.*;
import com.example.Sasimee_Back.entity.User;
import com.example.Sasimee_Back.service.EmailAuthService;
import com.example.Sasimee_Back.service.UserAuthService;
import com.example.Sasimee_Back.service.UserService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user")
@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "로그인/회원가입, Mypage", description="로그인/회원가입 및 mypage에 필요한 api들")
public class UserController {

    private final UserService userService;
    private final UserAuthService userAuthService;
    private final EmailAuthService emailAuthService;

    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "이메일 인증이 되지 않아서, 회원가입 불가"),
            @ApiResponse(responseCode = "200", description = "회원가입 성공"
            )
    })
    @PostMapping("/register")
    public ResponseEntity<UserDTO.registerResponse> registerController(
            @RequestBody UserDTO.registerRequest registerRequest) {
        boolean isVerified = emailAuthService.verifyEmailAuthentication(registerRequest.getEmail());

        if (isVerified == false) return ResponseEntity.badRequest().
                body(UserDTO.registerResponse.builder()
                        .status(false)
                        .message("이메일 인증이 되지 않았습니다. 이메일 인증을 완료한 후 회원가입이 가능합니다.")
                        .build());

        UserDTO.registerResponse registerResponse = userService.register(registerRequest);
        emailAuthService.deleteVerificatedEmailInfo(registerRequest.getEmail());


        return ResponseEntity.ok(registerResponse);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호가 달라서, 로그인 불가"),
            @ApiResponse(responseCode = "200", description = "로그인 성공"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<TokenDto> loginController(@RequestBody UserDTO.loginRequest loginRequest) {
        //중복 로그인 방지

        //아이디 또는 비밀번호가 틀린 경우 로그인 불가능
        TokenDto tokenDto = userAuthService.login(loginRequest);
        if (tokenDto == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(tokenDto);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "access token이 만료되었으므로, 안전한 로그아웃을 위해 토큰 재발급 권장"),
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logoutController(HttpServletRequest request) {
        // 1. request header에서 accessToken을 가져옴
        String accessToken = request.getHeader("Authorization");

        // "Bearer " 부분을 제거하고 실제 토큰만 추출
        accessToken = accessToken.substring(7);

        // 3. emailAuthService.logout 메소드에 토큰 전달하여 로그아웃 처리
        try {
            userAuthService.logout(accessToken);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Logout failed: " + e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(userAuthService.reissue(tokenRequestDto));
    }



    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "조회 실패"),
            @ApiResponse(responseCode = "200", description = "회원이 회원가입 시 선택한 태그 조회 성공"
            )
    })
    @GetMapping("/mypage/tag")
    public ResponseEntity<List<TagDTO.TagResponse>> getAllTags(@AuthenticationPrincipal SasimeePrincipal sasimeePrincipal) {
        String email = sasimeePrincipal.getUseremail();
        List<TagDTO.TagResponse> tagResponses = userService.getAllUserTags(email);
        return new ResponseEntity<>(tagResponses, HttpStatus.OK);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "조회 실패"),
            @ApiResponse(responseCode = "200", description = "회원이 회원가입 시 입력한 프로필 정보 조회 성공"
            )
    })
    @GetMapping("/mypage/profile")
    public ResponseEntity<UserDTO.profileResponse> getUserProfile(@AuthenticationPrincipal SasimeePrincipal sasimeePrincipal)
    {
        String email = sasimeePrincipal.getUseremail();
        UserDTO.profileResponse profileResponse = userService.getUserProfile(email);
        return new ResponseEntity<>(profileResponse, HttpStatus.OK);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "조회 실패"),
            @ApiResponse(responseCode = "200", description = "프로필 업데이트 성공"
            )
    })
    @PatchMapping("/mypage/profile/modifiy")
    public ResponseEntity<?> modifyUserProfile(@AuthenticationPrincipal SasimeePrincipal sasimeePrincipal, @RequestBody UserDTO.profileRequest profileRequest)
    {
        String email = sasimeePrincipal.getUseremail();
        userService.modifyUserProfile(email, profileRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "조회 실패"),
            @ApiResponse(responseCode = "200", description = "회원이 회원가입 시 선택한 태그 업데이트 성공"
            )
    })
    @PatchMapping("/mypage/tag/modify")
    public ResponseEntity<?> modifyUserTag(@AuthenticationPrincipal SasimeePrincipal sasimeePrincipal, @RequestBody
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "List of items",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    array = @ArraySchema(schema = @Schema(implementation = TagDTO.TagRequest.class))
            )
    ) List<TagDTO.TagRequest> tagRequests)
    {
        String email = sasimeePrincipal.getUseremail();
        userService.modifyUserTag(email, tagRequests);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
