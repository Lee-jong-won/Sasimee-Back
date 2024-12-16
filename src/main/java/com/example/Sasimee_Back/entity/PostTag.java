package com.example.Sasimee_Back.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@DiscriminatorValue("post")
public class PostTag extends Tag {

    @ManyToMany(mappedBy = "tags")
    private List<Post> posts = new ArrayList<>();

    public PostTag(String name, TagCategory category, Post post) {
        super(name, category);
        posts.add(post);
    }
}
