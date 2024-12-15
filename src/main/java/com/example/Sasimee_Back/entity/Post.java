package com.example.Sasimee_Back.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, name = "post_title")
    private String title;

    @Column(nullable = false, name = "post_content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "post_type")
    private PostType type;

    private String survey;

    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String startTime;

    @Column(nullable = false)
    private String endDate;

    @Column(nullable = false)
    private String endTime;

    @Column(nullable = false)
    private String author;

    private Long payment;

    private String address;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
