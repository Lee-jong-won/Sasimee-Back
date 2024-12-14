package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.dto.PostDTO;
import com.example.Sasimee_Back.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "포스트", description = "포스트 관리 API")
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //Create Post Function
    @Operation(summary = "게시글 업로드", description = "유저의 닉네임을 받고, 게시글을 업로드 한다.")
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 업로드 성공"),
            @ApiResponse(responseCode = "400", description = "게시글 업로드 실패")
    })
    public ResponseEntity<PostDTO.createResponse> post(@RequestParam Long userId, @Valid @RequestBody PostDTO.createRequest createRequest) {
            PostDTO.createResponse response = postService.createPost(userId, createRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //Search Post (by ID)
    @Operation(summary = "게시글 ID 검색" ,description = "게시글의 ID를 통하여 게시글의 모든 정보를 가져온다.")
    @GetMapping("/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 ID 검색 성공"),
            @ApiResponse(responseCode = "400", description = "게시글 ID 검색 실패")
    })
    public ResponseEntity<PostDTO.getPostResponse> getPostById(@PathVariable Long postId) {
        PostDTO.getPostResponse response = postService.getPostById(postId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Search Posts (by tag name)
    @Operation(summary = "게시글 태그 검색", description = "특정한 태그를 포함하고 있는 모든 게시글들의 대략적인 정보를 가져온다.")
    @GetMapping("/tag/{tagName}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 태그 검색 성공"),
            @ApiResponse(responseCode = "400", description = "게시글 태그 검색 실패")
    })
    public ResponseEntity<PostDTO.getAllPostResponse> getPostByTag(@PathVariable String tagName) {
        PostDTO.getAllPostResponse response = postService.getPostByTag(tagName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Search All Posts (with Paging)
    @Operation(summary = "게시글 전체 검색", description = "페이지에 해당하는 모든 게시글들에 대한 대략적인 정보들을 가져온다.")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 전체 조회 성공"),
            @ApiResponse(responseCode = "400", description = "게시글 전체 조회 실패")
    })
    public  ResponseEntity<PostDTO.getAllPostResponse> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) {
        PostDTO.getAllPostResponse response = postService.getAllPosts(page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Delete Post (by ID)
    @Operation(summary = "게시글 삭제", description = "해당 유저가 작성한 게시글을 삭제한다.")
    @DeleteMapping("/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "게시글 삭제 실패")
    })
    public ResponseEntity<Void> deletePost(@RequestParam Long userId, @PathVariable Long postId) {
            postService.deletePost(userId, postId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
