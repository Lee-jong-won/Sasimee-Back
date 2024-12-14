package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.dto.PostDTO;
import com.example.Sasimee_Back.dto.SasimeePrincipal;
import com.example.Sasimee_Back.service.PostService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Tag(name = "게시글 관리", description="게시글의 관리를 위한 api들")
public class PostController {
    private final PostService postService;

    //Create Post Function
    @PostMapping("/create")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 등록 성공"),
            @ApiResponse(responseCode = "400", description = "게시글 등록 실패")
    })
    public ResponseEntity<PostDTO.createResponse> post(@AuthenticationPrincipal SasimeePrincipal sasimeePrincipal, @Valid @RequestBody PostDTO.createRequest createRequest) {
        String userEmail = sasimeePrincipal.getUseremail();
        PostDTO.createResponse response = postService.createPost(userEmail, createRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //Search Post (by ID)
    @GetMapping("/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 ID 조회 성공"),
            @ApiResponse(responseCode = "400", description = "게시글 ID 조회 실패")
    })
    public ResponseEntity<PostDTO.getPostResponse> getPostById(@PathVariable Long postId) {
        PostDTO.getPostResponse response = postService.getPostById(postId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Search Posts (by tag name)
    @GetMapping("/tag/{tagName}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "태그를 포함한 게시글들 조회 성공"),
            @ApiResponse(responseCode = "400", description = "태그를 포함한 게시글들 조회 실패")
    })
    public ResponseEntity<PostDTO.getAllPostResponse> getPostByTag(@PathVariable String tagName) {
        PostDTO.getAllPostResponse response = postService.getPostByTag(tagName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Search All Posts (with Paging)
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "페이징을 통한 게시글들 조회 성공"),
            @ApiResponse(responseCode = "400", description = "페이징을 통한 게시글들 조회 실패")
    })
    public  ResponseEntity<PostDTO.getAllPostResponse> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) {
        PostDTO.getAllPostResponse response = postService.getAllPosts(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Delete Post (by ID)
    @DeleteMapping("/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ID 기반 게시글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "ID 기반 게시글 삭제 실패")
    })
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal SasimeePrincipal sasimeePrincipal, @PathVariable Long postId) {
        String userEmail = sasimeePrincipal.getUseremail();
        postService.deletePost(userEmail, postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
