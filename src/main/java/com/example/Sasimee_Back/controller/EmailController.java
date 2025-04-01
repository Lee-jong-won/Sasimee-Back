package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.ResponseCode.EmailResultCode;
import com.example.Sasimee_Back.common.BaseResponse;
import com.example.Sasimee_Back.ResponseCode.BasicResultCode;
import com.example.Sasimee_Back.dto.EmailDTO;
import com.example.Sasimee_Back.service.EmailAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessageRemovedException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/email")
@RestController
@RequiredArgsConstructor
@Tag(name = "email 인증", description="email 인증을 위한 api들")
@Slf4j
public class EmailController {

    private final EmailAuthService emailAuthService;

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<BaseResponse<Void>> messagingExHandler(MessagingException e){
        log.error("message={}", e);
        return BaseResponse.toResponseEntity(EmailResultCode.EMAIL_SENDING_FAILURE);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse<Void>> runtimeExHandler(RuntimeException e){
        log.error("message={}", e);
        return BaseResponse.toResponseEntity(EmailResultCode.UNKNOWN_SERVER_ERROR);
    }

    @Operation(summary = "인증 메일 보내기", description = "회원 가입을 희망하는 사람의 이메일로 메일 보내기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 보내기 성공"),
            @ApiResponse(responseCode = "500", description = "이메일 전송 실패"
            )
    })
    @PostMapping("/send")
    public ResponseEntity<BaseResponse<Void>> sendMail(@Validated @RequestBody EmailDTO.SentRequest emailRequest) throws MessagingException {

        if(emailAuthService.checkCertifiedNumIsMade(emailRequest.getTo()))
            return BaseResponse.toResponseEntity(EmailResultCode.IS_ALREADY_MADE);

        String authNum = EmailAuthService.createdCertifyNum();
        emailAuthService.sendEmailNotice(emailRequest, authNum, "email");
        log.info("email is sent successfully!");

        emailAuthService.saveVerificationCode(emailRequest.getTo(), authNum, 30);
        return BaseResponse.toResponseEntity(EmailResultCode.EMAIL_SEND_SUCCESS);
    }


    @Operation(summary = "이메일 검증하기", description = "회원 가입을 희망하는 사람이 받은 메일에서 인증 번호를 입력하여 서버에 전달한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "인증 번호가 틀린 경우"),
            @ApiResponse(responseCode = "200", description = "이메일 인증 성공"
            )
    })
    @PostMapping("/verify")
    public ResponseEntity<BaseResponse<Void>> verifyMail(@RequestBody EmailDTO.VerifyMailRequest request) {

        boolean verified = emailAuthService.verifyEmail(request.getEmail(), request.getAuthNum());

        if (!verified)
            return BaseResponse.toResponseEntity(EmailResultCode.VERIFY_FAILURE);
        else {
            return BaseResponse.toResponseEntity(EmailResultCode.VERIFY_SUCCESS);
        }
    }
}