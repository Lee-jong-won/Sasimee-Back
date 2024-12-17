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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Setter
    private String encryptPassword;

    //@Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    //@Column(nullable = false)
    //private Gender gender;

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

    public void addUserAuthority(){
        this.role = Role.ROLE_USER;
    }
    public void setTags(List<UserTag> tags){
        this.tags = tags;
    }

}
