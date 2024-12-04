package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.dto.PostDTO;
import com.example.Sasimee_Back.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //Create Post Function
    @PostMapping
    public ResponseEntity<PostDTO.createResponse> post(@Valid @RequestBody PostDTO.createRequest createRequest) {
        PostDTO.createResponse response = postService.createPost(createRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //Search Post (by ID)
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO.getPostResponse> getPostById(@PathVariable Long id) {
        PostDTO.getPostResponse response = postService.getPostById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Search Posts (by tag name)
    @GetMapping("/tag/{tagName}")
    public ResponseEntity<PostDTO.getAllPostResponse> getPostByTag(@PathVariable String tagName) {
        PostDTO.getAllPostResponse response = postService.getPostByTag(tagName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Search All Posts (with Paging)
    @GetMapping
    public  ResponseEntity<PostDTO.getAllPostResponse> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) {
        PostDTO.getAllPostResponse response = postService.getAllPosts(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Delete Post (by ID)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
