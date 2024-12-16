package com.example.Sasimee_Back.controller;

import com.example.Sasimee_Back.dto.PostDTO;
import com.example.Sasimee_Back.dto.SasimeePrincipal;
import com.example.Sasimee_Back.entity.Post;
import com.example.Sasimee_Back.entity.PostType;
import com.example.Sasimee_Back.entity.User;
import com.example.Sasimee_Back.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "설문형 게시글 등록", description = "설문을 포함한 게시글 등록")
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
                        .body("설문 주소가 포함되어있는지 한 번 더 확인해주세요.");
            }
        }else{
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("postType이 Survey인지 한 번 더 확인해주세요.");
        }
    }

    //Create Post Function
    @Operation(summary = "수행형 게시글 등록", description = "급여와 주소를 포함한 게시글 등록")
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
    @Operation(summary = "특정 설문형 게시글 조회", description = "게시글 ID를 통한 설문형 게시글 조회")
    @GetMapping("/get/survey/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "설문형 게시글 ID 조회 성공"),
            @ApiResponse(responseCode = "400", description = "설문형 게시글 ID 조회 실패")
    })
    public ResponseEntity<PostDTO.getSurveyPostResponse> getSurveyPostById(@PathVariable Long postId) {
        PostDTO.getSurveyPostResponse response = postService.getSurveyPostById(postId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Search Post (by ID)
    @Operation(summary = "특정 수행형 게시글 조회", description = "게시글 ID를 통한 수행형 게시글 조회")
    @GetMapping("/get/task/{postId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수행형 게시글 ID 조회 성공"),
            @ApiResponse(responseCode = "400", description = "수행형 게시글 ID 조회 실패")
    })
    public ResponseEntity<PostDTO.getTaskPostResponse> getTaskPostById(@PathVariable Long postId) {
        PostDTO.getTaskPostResponse response = postService.getTaskPostById(postId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Search Posts (by tag name)
    @Operation(summary = "태그를 사용한 게시글 조회", description = "특정 태그를 포함하고 있는 설문형/수행형 게시글 요약 정보 전체 조회")
    @GetMapping("/tag/{postType}/{tagName}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "태그를 포함한 게시글들 요약 정보 조회 성공"),
            @ApiResponse(responseCode = "400", description = "태그를 포함한 게시글들 요약 정보 조회 실패")
    })
    public ResponseEntity<PostDTO.getAllPostResponse> getPostByTag( @PathVariable PostType postType, @PathVariable String tagName) {
        PostDTO.getAllPostResponse response = postService.getPostByTag(tagName, postType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "현재 로그인한 유저가 작성한 게시글 조회", description = "유저가 작성한 게시글들에 대한 요약 정보 전체 조회")
    @GetMapping("/get/user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저가 작성한 게시글들 요약 정보 조회 성공"),
            @ApiResponse(responseCode = "400", description = "유저가 작성한 게시글들 요약 정보 조회 실패")
    })
    public ResponseEntity<PostDTO.getAllPostResponse> getPostByUser(@AuthenticationPrincipal SasimeePrincipal sasimeePrincipal) {
        String userEmail = sasimeePrincipal.getUsername();
        PostDTO.getAllPostResponse response = postService.getPostByUser(userEmail);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    //Search All Posts (with Paging)
    @Operation(summary = "설문형 게시글 전체 조회", description = "페이징을 통한 설문형 게시글들 요약 정보 전체 조회")
    @GetMapping("/get/survey")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "페이징을 통한 게시글들 조회 성공"),
            @ApiResponse(responseCode = "400", description = "페이징을 통한 게시글들 조회 실패")
    })
    public  ResponseEntity<PostDTO.getAllPostResponse> getAllSurvey(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) {
        PostType postType = PostType.S;
        PostDTO.getAllPostResponse response = postService.getAllPosts(page, size, postType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "수행형 게시글 전체 조회", description = "페이징을 통한 수행형 게시글들 요약 정보 전체 조회")
    @GetMapping("/get/task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "페이징을 통한 게시글들 조회 성공"),
            @ApiResponse(responseCode = "400", description = "페이징을 통한 게시글들 조회 실패")
    })
    public  ResponseEntity<PostDTO.getAllPostResponse> getAllTask(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        PostType postType = PostType.T;
        PostDTO.getAllPostResponse response = postService.getAllPosts(page, size, postType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "설문형 게시글 수정", description = "게시글 ID를 포함한 정보로 설문형 게시글 수정")
    @PutMapping("/update/survey")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "설문형 게시글 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "설문형 게시글 업데이트 실패")
    })
    public  ResponseEntity<Void> updateSurvey(@RequestBody @Valid PostDTO.UpdateSurveyRequest request) {
        postService.updateSurveyPost(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "수행형 게시글 수정", description = "게시글 ID를 포함한 정보로 수행형 게시글 수정")
    @PutMapping("/update/task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수행형 게시글 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "수행형 게시글 업데이트 실패")
    })
    public  ResponseEntity<Void> updateTask(@RequestBody @Valid PostDTO.UpdateTaskRequest request) {
        postService.updateTaskPost(request);
        return ResponseEntity.ok().build();
    }

    //Delete Post (by ID)
    @Operation(summary = "게시글 삭제", description = "게시글 ID를 통한 게시글 삭제")
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
