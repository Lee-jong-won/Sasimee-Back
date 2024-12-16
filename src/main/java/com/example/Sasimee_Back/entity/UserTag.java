package com.example.Sasimee_Back.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTag extends Tag{

    @ManyToMany(mappedBy = "tags")
    private List<User> users = new ArrayList<>();

    public UserTag(String name, TagCategory category, User user) {
        super(name, category);
        this.users.add(user);
    }
}
