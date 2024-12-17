package com.example.Sasimee_Back.service;

import com.example.Sasimee_Back.dto.TagDTO;
import com.example.Sasimee_Back.dto.UserDTO;
import com.example.Sasimee_Back.entity.PostTag;
import com.example.Sasimee_Back.entity.User;
import com.example.Sasimee_Back.entity.UserTag;
import com.example.Sasimee_Back.exception.UserAlreadyExistsException;
import com.example.Sasimee_Back.repository.UserRepository;
import com.example.Sasimee_Back.repository.UserTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    private final PasswordEncoder passwordEncoder;
    private final UserTagRepository userTagRepository;

    @Transactional
    public UserDTO.registerResponse register(UserDTO.registerRequest registerRequest) {
        Optional<User> userOptional = userRepository.findByEmail(registerRequest.getEmail());

        if (userOptional.isPresent())
            throw new UserAlreadyExistsException("이미 해당 이메일로 가입된 계정이 존재합니다!");

        registerRequest.setPassword1(passwordEncoder.encode(registerRequest.getPassword1()));
        User user = UserDTO.registerRequest.toEntity(registerRequest);
        user.addUserAuthority();

        List<UserTag> tags = registerRequest.getTags().stream()
                .map(tagRequest -> userTagRepository.findByNameAndCategory(tagRequest.getName(), tagRequest.getCategory())
                        .orElseGet(() -> userTagRepository.save(new UserTag(tagRequest.getName(), tagRequest.getCategory(), user))))
                .collect(Collectors.toList());

        user.setTags(tags);
        userRepository.save(user);

        return UserDTO.registerResponse.builder()
                .status(true)
                .message("회원가입이 성공적으로 완료되었습니다!")
                .build();
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
