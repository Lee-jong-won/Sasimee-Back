package com.example.Sasimee_Back.controller;


import com.example.Sasimee_Back.argumentResolver.JwtAuthentication;
import com.example.Sasimee_Back.ResponseCode.UserResultCode;
import com.example.Sasimee_Back.authentication.User;
import com.example.Sasimee_Back.common.BaseResponse;
import com.example.Sasimee_Back.service.TokenProvider;
import com.example.Sasimee_Back.dto.*;
import com.example.Sasimee_Back.service.EmailAuthService;
import com.example.Sasimee_Back.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/user")
@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "로그인/회원가입, Mypage", description="로그인/회원가입 및 mypage에 필요한 api들")
public class UserController {

    private final UserService userService;
    private final EmailAuthService emailAuthService;
    private final TokenProvider tokenProvider;
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseEntity<BaseResponse<Void>> jwtExceptionHandler(JwtException e){
        log.error("message={}", e);
        return BaseResponse.toResponseEntity(UserResultCode.WRONG_JWT_TOKEN);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseEntity<BaseResponse<Void>> jwtExceptionHandler(ExpiredJwtException e){
        log.error("message={}", e);
        return BaseResponse.toResponseEntity(UserResultCode.WRONG_JWT_TOKEN);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ResponseEntity<BaseResponse<Void>> runtimeExceptionHandler(RuntimeException e){
        log.error("message={}", e);
        return BaseResponse.toResponseEntity(UserResultCode.INTERNAL_SERVER_ERROR);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "이메일 인증이 되지 않아서, 회원가입 불가"),
            @ApiResponse(responseCode = "200", description = "회원가입 성공"
            )
    })
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Void>> register(
            @RequestBody UserDTO.registerRequest registerRequest) {
        boolean isVerified = emailAuthService.verifyEmailAuthentication(registerRequest.getEmail());
        if (isVerified == false)
            return BaseResponse.toResponseEntity(UserResultCode.NOT_VERIFIED);
        userService.register(registerRequest);
        emailAuthService.deleteVerificatedEmailInfo(registerRequest.getEmail());
        return BaseResponse.toResponseEntity(UserResultCode.SUCCESS_REGISTER);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "아이디 또는 비밀번호가 달라서, 로그인 불가"),
            @ApiResponse(responseCode = "200", description = "로그인 성공"
            )
    })
    @User
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<TokenDto>> login(@RequestBody UserDTO.loginRequest loginRequest) {
        //중복 로그인 방지
        TokenDto tokenDto = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return BaseResponse.toResponseEntity(UserResultCode.SUCCESS_LOGIN, tokenDto);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "access token이 만료되었으므로, 안전한 로그아웃을 위해 토큰 재발급 권장"),
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"
            )
    })
    @User
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Void>> logout(@JwtAuthentication String memberEmail) {
        userService.logout(memberEmail);
        return BaseResponse.toResponseEntity(UserResultCode.SUCCESS_LOGOUT);
    }

    @PostMapping("/reissue")
    @User
    public ResponseEntity<BaseResponse<TokenDto>> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        if (tokenProvider.validateToken(tokenRequestDto.getAccessToken()))
            throw new JwtException("아직 만료되지 않은 토큰입니다");
        TokenDto tokenDto = tokenProvider.reissue(tokenRequestDto);
        return BaseResponse.toResponseEntity(UserResultCode.SUCCESS_REISSUE_TOKEN, tokenDto);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "조회 실패"),
            @ApiResponse(responseCode = "200", description = "회원이 회원가입 시 선택한 태그 조회 성공"
            )
    })
    @User
    @GetMapping("/mypage/tag")
    public ResponseEntity<List<TagDTO.TagResponse>> getAllTags(@JwtAuthentication String memberEmail) {
        List<TagDTO.TagResponse> tagResponses = userService.getAllUserTags(memberEmail);
        return new ResponseEntity<>(tagResponses, HttpStatus.OK);
    }

   @ApiResponses({
            @ApiResponse(responseCode = "400", description = "조회 실패"),
            @ApiResponse(responseCode = "200", description = "회원이 회원가입 시 입력한 프로필 정보 조회 성공"
            )
    })
    @User
    @GetMapping("/mypage/profile")
    public ResponseEntity<UserDTO.profileResponse> getUserProfile(@JwtAuthentication String email)
    {
        UserDTO.profileResponse profileResponse = userService.getUserProfile(email);
        return new ResponseEntity<>(profileResponse, HttpStatus.OK);
    }

  @ApiResponses({
            @ApiResponse(responseCode = "400", description = "조회 실패"),
            @ApiResponse(responseCode = "200", description = "프로필 업데이트 성공"
            )
    })
    @User
    @PatchMapping("/mypage/profile/modifiy")
    public ResponseEntity<?> modifyUserProfile(@JwtAuthentication String email, @RequestBody UserDTO.profileRequest profileRequest)
    {
        userService.modifyUserProfile(email, profileRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

  @ApiResponses({
            @ApiResponse(responseCode = "400", description = "조회 실패"),
            @ApiResponse(responseCode = "200", description = "회원이 회원가입 시 선택한 태그 업데이트 성공"
            )
    })
    @User
    @PatchMapping("/mypage/tag/modify")
    public ResponseEntity<?> modifyUserTag(@JwtAuthentication String email, @RequestBody
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "List of items",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                    array = @ArraySchema(schema = @Schema(implementation = TagDTO.TagRequest.class))
            )
    ) List<TagDTO.TagRequest> tagRequests)
    {
        userService.modifyUserTag(email, tagRequests);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
