package com.example.Sasimee_Back.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "context_type") // context_type 컬럼 추가
public abstract class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "tags")
    private List<Post> posts;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TagCategory category;

    public Tag(String name, TagCategory category) {
        this.name = name;
        this.category = category;
    }
}
