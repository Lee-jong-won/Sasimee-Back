package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.dto.PostDTO;
import com.example.Sasimee_Back.dto.SasimeePrincipal;
import com.example.Sasimee_Back.entity.PostType;
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
    @PostMapping("/create/survey")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "설문형 게시글 등록 성공"),
            @ApiResponse(responseCode = "400", description = "설문형 게시글 등록 실패")
    })
    public ResponseEntity<?> surveyPost(@AuthenticationPrincipal SasimeePrincipal sasimeePrincipal, @Valid @RequestBody PostDTO.createSurveyRequest createRequest) {
        String userEmail = sasimeePrincipal.getUsername();
        if(createRequest.getPostType() == PostType.S){
            PostDTO.createSurveyResponse response = postService.createSurveyPost(userEmail, createRequest);
            if (response.getSurvey() != null){
                return new ResponseEntity<>(response, HttpStatus.CREATED);

            }else{
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\": \"설문 주소가 포함되어있는지 한 번 더 확인해주세요.\"}");
            }
        }else{
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\": \"postType이 Survey인지 한 번 더 확인해주세요.\"}");
        }
    }

    //Create Post Function
    @PostMapping("/create/task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수행형 게시글 등록 성공"),
            @ApiResponse(responseCode = "400", description = "수행형 게시글 등록 실패")
    })
    public ResponseEntity<?> taskPost(@AuthenticationPrincipal SasimeePrincipal sasimeePrincipal, @Valid @RequestBody PostDTO.createTaskRequest createRequest) {
        String userEmail = sasimeePrincipal.getUsername();
        if(createRequest.getPostType() == PostType.T){
            PostDTO.createTaskResponse response = postService.createTaskPost(userEmail, createRequest);
            if (response.getAddress() != null && response.getPayment() != null){
                return new ResponseEntity<>(response, HttpStatus.CREATED);

            }else{
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\": \"급여나 주소가 포함되어있는지 한 번 더 확인해주세요.\"}");
            }
        }else{
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("{\"message\": \"postType이 Task인지 한 번 더 확인해주세요.\"}");
        }
    }


    //Search Post (by ID)
    @GetMapping("/survey/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "설문형 게시글 ID 조회 성공"),
            @ApiResponse(responseCode = "400", description = "설문형 게시글 ID 조회 실패")
    })
    public ResponseEntity<PostDTO.getSurveyPostResponse> getSurveyPostById(@PathVariable Long postId) {
        PostDTO.getSurveyPostResponse response = postService.getSurveyPostById(postId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Search Post (by ID)
    @GetMapping("/task/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수행형 게시글 ID 조회 성공"),
            @ApiResponse(responseCode = "400", description = "수행형 게시글 ID 조회 실패")
    })
    public ResponseEntity<PostDTO.getTaskPostResponse> getTaskPostById(@PathVariable Long postId) {
        PostDTO.getTaskPostResponse response = postService.getTaskPostById(postId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Search Posts (by tag name)
    @GetMapping("/tag/{postType}/{tagName}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "태그를 포함한 게시글들 조회 성공"),
            @ApiResponse(responseCode = "400", description = "태그를 포함한 게시글들 조회 실패")
    })
    public ResponseEntity<PostDTO.getAllPostResponse> getPostByTag( @PathVariable PostType postType, @PathVariable String tagName) {
        PostDTO.getAllPostResponse response = postService.getPostByTag(tagName, postType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Search All Posts (with Paging)
    @GetMapping("/{postType}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "페이징을 통한 게시글들 조회 성공"),
            @ApiResponse(responseCode = "400", description = "페이징을 통한 게시글들 조회 실패")
    })
    public  ResponseEntity<PostDTO.getAllPostResponse> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size,
                                                                   @PathVariable PostType postType) {
        PostDTO.getAllPostResponse response = postService.getAllPosts(page, size, postType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Delete Post (by ID)
    @DeleteMapping("/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ID 기반 게시글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "ID 기반 게시글 삭제 실패")
    })
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal SasimeePrincipal sasimeePrincipal, @PathVariable Long postId) {
        String userEmail = sasimeePrincipal.getUsername();
        postService.deletePost(userEmail, postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
