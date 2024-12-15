package com.example.Sasimee_Back.service;


import com.example.Sasimee_Back.dto.PostDTO;
import com.example.Sasimee_Back.dto.TagDTO;
import com.example.Sasimee_Back.entity.*;
import com.example.Sasimee_Back.repository.ClearHistoryRepository;
import com.example.Sasimee_Back.repository.PostRepository;
import com.example.Sasimee_Back.repository.TagRepository;
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
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ClearHistoryRepository clearHistoryRepository;

    @Transactional
    public PostDTO.createSurveyResponse createSurveyPost(String userEmail, PostDTO.createSurveyRequest createRequest){
        User user = userRepository.findByEmail(userEmail).orElse(null);

        List<Tag> tags = createRequest.getTags().stream()
                .map(tagRequest -> tagRepository.findByNameAndCategory(tagRequest.getName(), tagRequest.getCategory())
                        .orElseGet(() -> tagRepository.save(new Tag(tagRequest.getName(), tagRequest.getCategory()))))
                .collect(Collectors.toList());

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
                .tags(tags)
                .user(user)
                .build();

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

        List<Tag> tags = createRequest.getTags().stream()
                .map(tagRequest -> tagRepository.findByNameAndCategory(tagRequest.getName(), tagRequest.getCategory())
                        .orElseGet(() -> tagRepository.save(new Tag(tagRequest.getName(), tagRequest.getCategory()))))
                .collect(Collectors.toList());

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
                .tags(tags)
                .user(user)
                .build();

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

        User user = post.getUser();
        String userName = user.getName();

        if(userName == null){
            throw new RuntimeException("유저의 이름을 조회할 수 없습니다.");
        }

        List<TagDTO.TagRequest> tags = post.getTags().stream()
                .map(tag -> new TagDTO.TagRequest(tag.getName(), tag.getCategory()))
                .collect(Collectors.toList());

        return PostDTO.getSurveyPostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .postType(post.getType())
                .survey(post.getSurvey())
                .startDate(post.getStartDate())
                .startTime(post.getStartTime())
                .endDate(post.getEndDate())
                .endTime(post.getEndTime())
                .author(post.getAuthor())
                .tags(tags)
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

    public PostDTO.getAllPostResponse getPostByTag(String tagName, PostType postType){
        List<Post> posts = postRepository.findByTagsNameAndType(tagName, postType);

        List<PostDTO.getAllPostResponse.PostSummary> postSummaries = posts.stream()
                .map(post -> {
                    List<TagDTO.TagRequest> tags = post.getTags().stream()
                            .map(tag -> new TagDTO.TagRequest(tag.getName(), tag.getCategory()))
                            .collect(Collectors.toList());

                    return PostDTO.getAllPostResponse.PostSummary.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .postType(post.getType())
                            .tags(tags)
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

                    List<TagDTO.TagRequest> tags = post.getTags().stream()
                            .map(tag -> new TagDTO.TagRequest(tag.getName(), tag.getCategory()))
                            .collect(Collectors.toList());

                    return PostDTO.getAllPostResponse.PostSummary.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .postType(post.getType())
                            .tags(tags)
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
                    List<TagDTO.TagRequest> tags = post.getTags().stream()
                            .map(tag -> new TagDTO.TagRequest(tag.getName(), tag.getCategory()))
                            .collect(Collectors.toList());

                    return PostDTO.getAllPostResponse.PostSummary.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .postType(post.getType())
                            .tags(tags)
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
