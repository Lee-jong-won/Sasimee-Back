package com.example.Sasimee_Back.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Post> posts;

    public Tag(String tagName) {
        this.name = tagName;
    }
}
