package com.example.Sasimee_Back.service;


import com.example.Sasimee_Back.dto.PostDTO;
import com.example.Sasimee_Back.dto.TagDTO;
import com.example.Sasimee_Back.entity.*;
import com.example.Sasimee_Back.repository.ClearHistoryRepository;
import com.example.Sasimee_Back.repository.PostRepository;
import com.example.Sasimee_Back.repository.PostTagRepository;
import com.example.Sasimee_Back.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;
    private final UserRepository userRepository;
    private final ClearHistoryRepository clearHistoryRepository;

    @Transactional
    public PostDTO.createSurveyResponse createSurveyPost(String userEmail, PostDTO.createSurveyRequest createRequest){
        User user = userRepository.findByEmail(userEmail).orElse(null);

        Post post = Post.builder()
                .title(createRequest.getTitle())
                .content(createRequest.getContent())
                .type(createRequest.getPostType())
                .survey(createRequest.getSurvey())
                .startDate(createRequest.getStartDate())
                .startTime(createRequest.getStartTime())
                .endDate(createRequest.getEndDate())
                .endTime(createRequest.getEndTime())
                .author(createRequest.getAuthor())
                .user(user)
                .build();


        List<PostTag> tags = createRequest.getTags().stream()
                .map(tagRequest -> postTagRepository.findByNameAndCategory(tagRequest.getName(), tagRequest.getCategory())
                        .orElseGet(() -> postTagRepository.save(new PostTag(tagRequest.getName(), tagRequest.getCategory(), post))))
                .collect(Collectors.toList());
        post.setTags(tags);
        Post savedPost = postRepository.save(post);

        return PostDTO.createSurveyResponse.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .postType(savedPost.getType())
                .survey(savedPost.getSurvey())
                .startDate(savedPost.getStartDate())
                .startTime(savedPost.getStartTime())
                .endDate(savedPost.getEndDate())
                .endTime(savedPost.getEndTime())
                .author(savedPost.getAuthor())
                .tags(tags.stream().map(tag -> new TagDTO.TagRequest(tag.getName(), tag.getCategory()))
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public PostDTO.createTaskResponse createTaskPost(String userEmail, PostDTO.createTaskRequest createRequest){
        User user = userRepository.findByEmail(userEmail).orElse(null);

        Post post = Post.builder()
                .title(createRequest.getTitle())
                .content(createRequest.getContent())
                .type(createRequest.getPostType())
                .startDate(createRequest.getStartDate())
                .startTime(createRequest.getStartTime())
                .endDate(createRequest.getEndDate())
                .endTime(createRequest.getEndTime())
                .author(createRequest.getAuthor())
                .payment(createRequest.getPayment())
                .address(createRequest.getAddress())
                .user(user)
                .build();

        List<PostTag> tags = createRequest.getTags().stream()
                .map(tagRequest -> postTagRepository.findByNameAndCategory(tagRequest.getName(), tagRequest.getCategory())
                        .orElseGet(() -> postTagRepository.save(new PostTag(tagRequest.getName(), tagRequest.getCategory(), post))))
                .collect(Collectors.toList());
        post.setTags(tags);
        Post savedPost = postRepository.save(post);

        return PostDTO.createTaskResponse.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .postType(savedPost.getType())
                .startDate(savedPost.getStartDate())
                .startTime(savedPost.getStartTime())
                .endDate(savedPost.getEndDate())
                .endTime(savedPost.getEndTime())
                .author(savedPost.getAuthor())
                .payment(savedPost.getPayment())
                .address(savedPost.getAddress())
                .tags(tags.stream().map(tag -> new TagDTO.TagRequest(tag.getName(), tag.getCategory()))
                        .collect(Collectors.toList()))
                .build();
    }

    public PostDTO.getSurveyPostResponse getSurveyPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("해당 포스트는 존재하지 않습니다."));

        return PostDTO.getSurveyPostResponse.builder()

                .survey(post.getSurvey())
                .endDate(post.getEndDate())
                .endTime(post.getEndTime())
                .build();
    }

    public PostDTO.getTaskPostResponse getTaskPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("해당 포스트는 존재하지 않습니다."));

        User user = post.getUser();
        String userName = user.getName();

        if(userName == null){
            throw new RuntimeException("유저의 이름을 조회할 수 없습니다.");
        }

        List<TagDTO.TagRequest> tags = post.getTags().stream()
                .map(tag -> new TagDTO.TagRequest(tag.getName(), tag.getCategory()))
                .collect(Collectors.toList());

        return PostDTO.getTaskPostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .postType(post.getType())
                .startDate(post.getStartDate())
                .startTime(post.getStartTime())
                .endDate(post.getEndDate())
                .endTime(post.getEndTime())
                .author(post.getAuthor())
                .address(post.getAddress())
                .payment(post.getPayment())
                .tags(tags)
                .build();
    }

    public PostDTO.getAllPostResponse getPostByUser(String userEmail){
        User user = userRepository.findByEmail(userEmail).orElse(null);
        List<Post> posts = postRepository.findByUser(user);

        List<PostDTO.getAllPostResponse.PostSummary> postSummaries = posts.stream()
                .map(post -> {
                    List<String> tagName = post.getTags().stream()
                            .map(PostTag::getName)
                            .collect(Collectors.toList());

                    return PostDTO.getAllPostResponse.PostSummary.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .postType(post.getType())
                            .tagName(tagName)
                            .author(post.getAuthor())
                            .build();
                })
                .collect(Collectors.toList());

        return PostDTO.getAllPostResponse.builder().posts(postSummaries).build();
    }

    public PostDTO.getAllPostResponse getPostByTag(String tagName, PostType postType){
        List<Post> posts = postRepository.findByTagsNameAndType(tagName, postType);

        List<PostDTO.getAllPostResponse.PostSummary> postSummaries = posts.stream()
                .map(post -> {
                    List<String> tagNames = post.getTags().stream()
                            .map(PostTag::getName)
                            .collect(Collectors.toList());

                    return PostDTO.getAllPostResponse.PostSummary.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .postType(post.getType())
                            .tagName(tagNames)
                            .author(post.getAuthor())
                            .build();
                })
                .collect(Collectors.toList());

        return PostDTO.getAllPostResponse.builder().posts(postSummaries).build();
    }

    public PostDTO.getAllPostResponse getPostByHistory(String userEmail){
        User user = userRepository.findByEmail(userEmail).orElse(null);
        List<ClearHistory> clearHistories = clearHistoryRepository.findByUser(user);

        List<PostDTO.getAllPostResponse.PostSummary> postSummaries = clearHistories.stream()
                .map(clearHistory -> {
                    Post post = clearHistory.getPost();

                    List<String> tagName = post.getTags().stream()
                            .map(PostTag::getName)
                            .collect(Collectors.toList());

                    return PostDTO.getAllPostResponse.PostSummary.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .postType(post.getType())
                            .author(post.getAuthor())
                            .tagName(tagName)
                            .build();
                })
                .collect(Collectors.toList());

        return PostDTO.getAllPostResponse.builder().posts(postSummaries).build();
    }

    public PostDTO.getAllPostResponse getAllPosts(int page, int size, PostType postType){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        Page<Post> posts = postRepository.findByType(postType, pageRequest);

        List<PostDTO.getAllPostResponse.PostSummary> postSummaries = posts.stream()
                .map(post -> {
                    List<String> tagName = post.getTags().stream()
                            .map(PostTag::getName)
                            .collect(Collectors.toList());

                    return PostDTO.getAllPostResponse.PostSummary.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .postType(post.getType())
                            .tagName(tagName)
                            .author(post.getAuthor())
                            .build();
                })
                .collect(Collectors.toList());

        return PostDTO.getAllPostResponse.builder()
                .posts(postSummaries)
                .currentPage(posts.getNumber())
                .totalPage(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .build();
    }

    public PostDTO.getSurveyResponse getSurveyPosts(int page, int size, PostType postType){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        Page<Post> posts = postRepository.findByType(postType, pageRequest);

        List<PostDTO.getSurveyResponse.PostSummary> postSummaries = posts.stream()
                .map(post -> {
                    List<String> tagName = post.getTags().stream()
                            .map(PostTag::getName)
                            .collect(Collectors.toList());

                    return PostDTO.getSurveyResponse.PostSummary.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .postType(post.getType())
                            .tagName(tagName)
                            .survey(post.getSurvey())
                            .author(post.getAuthor())
                            .build();
                })
                .collect(Collectors.toList());

        return PostDTO.getSurveyResponse.builder()
                .posts(postSummaries)
                .currentPage(posts.getNumber())
                .totalPage(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .build();
    }

    public PostDTO.getTaskResponse getTaskPosts(int page, int size, PostType postType){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        Page<Post> posts = postRepository.findByType(postType, pageRequest);

        List<PostDTO.getTaskResponse.PostSummary> postSummaries = posts.stream()
                .map(post -> {
                    List<String> tagName = post.getTags().stream()
                            .map(PostTag::getName)
                            .collect(Collectors.toList());

                    return PostDTO.getTaskResponse.PostSummary.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .postType(post.getType())
                            .tagName(tagName)
                            .author(post.getAuthor())
                            .address(post.getAddress())
                            .payment(post.getPayment())
                            .build();
                })
                .collect(Collectors.toList());

        return PostDTO.getTaskResponse.builder()
                .posts(postSummaries)
                .currentPage(posts.getNumber())
                .totalPage(posts.getTotalPages())
                .totalElements(posts.getTotalElements())
                .build();
    }

    @Transactional
    public void updateSurveyPost(PostDTO.UpdateSurveyRequest request){
        Post post = postRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if(post.getType() != PostType.S){
            throw new IllegalArgumentException("Survey 게시글만 수정할 수 있습니다.");
        }

        updateCommonField(post, request);

        if(request.getSurvey() != null){
            post.setSurvey(request.getSurvey());
        }

        postRepository.save(post);
    }

    @Transactional
    public void updateTaskPost(PostDTO.UpdateTaskRequest request){
        Post post = postRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if(post.getType() != PostType.T){
            throw new IllegalArgumentException("Task 게시글만 수정할 수 있습니다.");
        }

        updateCommonField(post, request);

        if(request.getPayment() != null){
            post.setPayment(request.getPayment());
        }

        if(request.getAddress() != null){
            post.setAddress(request.getAddress());
        }

        postRepository.save(post);
    }

    public void updateCommonField(Post post, PostDTO.PostUpdateRequest request){
        if (request.getTitle() != null) {
            post.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            post.setContent(request.getContent());
        }
        if (request.getStartDate() != null) {
            post.setStartDate(request.getStartDate());
        }
        if(request.getStartTime() != null){
            post.setStartTime(request.getStartTime());
        }
        if (request.getEndDate() != null) {
            post.setEndDate(request.getEndDate());
        }
        if(request.getEndTime() != null){
            post.setEndTime(request.getEndTime());
        }
        if(request.getAuthor() != null){
            post.setAuthor(request.getAuthor());
        }

        if (request.getTags() != null) {
            List<PostTag> newTags = request.getTags().stream()
                    .map(tagRequest -> postTagRepository.findByNameAndCategory(tagRequest.getName(), tagRequest.getCategory())
                            .orElseGet(() -> postTagRepository.save(new PostTag(tagRequest.getName(), tagRequest.getCategory(), post))))
                    .collect(Collectors.toList());

            post.getTags().clear();
            post.getTags().addAll(newTags);
        }

    }

    public void deletePost(String userEmail, Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 포스트는 존재하지 않습니다."));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("해당 유저는 존재하지 않습니다."));

        if(!post.getUser().equals(user)){
            throw new RuntimeException("이 포스트는 해당 유저가 작성한 게시글이 아닙니다.");
        }

        postRepository.delete(post);
    }
}
