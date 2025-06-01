package com.example.Sasimee_Back.entity;

import com.example.Sasimee_Back.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //@Column(unique = true, nullable = false)
    private String email;

    @Setter
    private String encryptPassword;

    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    //@Column(nullable = false)
    private Gender gender;

    @Setter
    private String name;
    @Setter
    private String phoneNumber;
    //private String address;
    //private String birth;

    @ManyToMany
    @JoinTable(
            name = "user_tags",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<UserTag> tags;
    public void setTags(List<UserTag> tags){
        this.tags = tags;
    }

}
