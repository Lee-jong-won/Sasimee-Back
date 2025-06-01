package com.example.Sasimee_Back.service;

import com.example.Sasimee_Back.dto.TagDTO;
import com.example.Sasimee_Back.dto.TokenDto;
import com.example.Sasimee_Back.dto.UserDTO;
import com.example.Sasimee_Back.entity.RefreshToken;
import com.example.Sasimee_Back.entity.User;
import com.example.Sasimee_Back.entity.UserTag;
import com.example.Sasimee_Back.exception.PasswordNotMatchException;
import com.example.Sasimee_Back.exception.UserAlreadyExistsException;
import com.example.Sasimee_Back.repository.LockRepository;
import com.example.Sasimee_Back.repository.RefreshTokenRepository;
import com.example.Sasimee_Back.repository.UserRepository;
import com.example.Sasimee_Back.repository.UserTagRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${jwt.secret}")
    private String secretKey;

    //토큰 만료시간은 1시간으로 설정
    private Long expiredMs = 1000 * 60 * 60l;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserTagRepository userTagRepository;
    private final LockRepository lockRepository;

    @Transactional
    public void registerFacade(UserDTO.registerRequest registerRequest){
        try {
            Long available = lockRepository.getLock("register");
            if (available == 0) {
                throw new RuntimeException("락을 획득하지 못했습니다");
            }
            register(registerRequest);
        }
        finally{
            lockRepository.releaseLock("register");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void register(UserDTO.registerRequest registerRequest) {
        if (userRepository.findFirstForUpdateByEmail(registerRequest.getEmail()).isPresent())
            throw new UserAlreadyExistsException("이미 해당 이메일로 가입된 계정이 존재합니다!");

        registerRequest.setPassword1(BCrypt.hashpw(registerRequest.getPassword1(), BCrypt.gensalt()));
        User user = UserDTO.registerRequest.toEntity(registerRequest);
        List<UserTag> tags = null;

        if(registerRequest.getTags() != null) {
             tags = registerRequest.getTags().stream()
                    .map(tagRequest -> userTagRepository.findByNameAndCategory(tagRequest.getName(), tagRequest.getCategory())
                            .orElseGet(() -> userTagRepository.save(new UserTag(tagRequest.getName(), tagRequest.getCategory(), user))))
                    .collect(Collectors.toList());
            user.setTags(tags);
        }

        userRepository.saveAndFlush(user);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void register2(UserDTO.registerRequest registerRequest) {
        if (userRepository.findFirstForUpdateByEmail(registerRequest.getEmail()).isPresent())
            throw new UserAlreadyExistsException("이미 해당 이메일로 가입된 계정이 존재합니다!");

        registerRequest.setPassword1(BCrypt.hashpw(registerRequest.getPassword1(), BCrypt.gensalt()));
        User user = UserDTO.registerRequest.toEntity(registerRequest);
        List<UserTag> tags = null;

        if(registerRequest.getTags() != null) {
            tags = registerRequest.getTags().stream()
                    .map(tagRequest -> userTagRepository.findByNameAndCategory(tagRequest.getName(), tagRequest.getCategory())
                            .orElseGet(() -> userTagRepository.save(new UserTag(tagRequest.getName(), tagRequest.getCategory(), user))))
                    .collect(Collectors.toList());
            user.setTags(tags);
        }

        userRepository.saveAndFlush(user);
    }


    @Transactional
    public TokenDto login(String email, String password) {

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new RuntimeException("해당 유저를 찾을 수 없습니다."));

        if(!BCrypt.checkpw(password, user.getEncryptPassword()))
            throw new PasswordNotMatchException("비밀번호가 틀립니다!");

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        String accessToken = tokenProvider.createAccessToken(email, false, TokenProvider.ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = tokenProvider.createRefreshToken(email, TokenProvider.REFRESH_TOKEN_EXPIRE_TIME);

        // 4. RefreshToken 저장
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .key(email)
                .value(refreshToken)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);

        // 5. 토큰 발급
        return TokenDto.builder().grantType("bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(String useremail){
        refreshTokenRepository.deleteByKey(useremail);
    }

    public List<TagDTO.TagResponse> getAllUserTags(String email)
    {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));

        List<TagDTO.TagResponse> tagResponses = user.getTags()
                .stream()
                .map(tag -> new TagDTO.TagResponse(tag.getName(),tag.getCategory()))
                .collect(Collectors.toList());

        return tagResponses;
    }

    public UserDTO.profileResponse getUserProfile(String email)
    {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        return UserDTO.profileResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phonenumber(user.getPhoneNumber())
                .build();
    }

    public void modifyUserProfile(String email, UserDTO.profileRequest profileRequest)
    {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        user.setName(profileRequest.getName());
        user.setPhoneNumber(profileRequest.getPhonenumber());

        userRepository.save(user);
    }

    public void modifyUserTag(String email, List<TagDTO.TagRequest> tagRequests) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        //user의 id와 tagRequests의 category를 기준으로 tag를 탐색
        List<UserTag> userTags = user.getTags();

        // TagRequest 리스트 순회하여 처리
        for (TagDTO.TagRequest tagRequest : tagRequests) {
            // user에 매핑된 UserTag 중 category가 일치하는 것 찾기
            UserTag targetTag = userTags.stream()
                    .filter(userTag -> userTag.getCategory() == tagRequest.getCategory())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No UserTag found with the specified category"));

            // UserTag의 name을 tagRequest의 name으로 대체
            targetTag.setName(tagRequest.getName());

            // 변경사항 저장 (Cascade 옵션이 걸려 있으면 별도 저장 필요 X)
            userTagRepository.save(targetTag);
        }
    }


}
