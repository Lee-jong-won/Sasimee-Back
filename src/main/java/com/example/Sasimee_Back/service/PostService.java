package com.example.Sasimee_Back.service;


import com.example.Sasimee_Back.dto.PostDTO;
import com.example.Sasimee_Back.entity.ClearHistory;
import com.example.Sasimee_Back.entity.Post;
import com.example.Sasimee_Back.entity.Tag;
import com.example.Sasimee_Back.entity.User;
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
    public PostDTO.createResponse createPost(String userEmail, PostDTO.createRequest createRequest){
        User user = userRepository.findByEmail(userEmail).orElse(null);

        List<Tag> tags = createRequest.getTags().stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(new Tag(tagName))))
                .collect(Collectors.toList());

        Post post = Post.builder()
                .title(createRequest.getTitle())
                .content(createRequest.getContent())
                .type(createRequest.getPostType())
                .survey(createRequest.getSurvey())
                .deadline(createRequest.getDeadline())
                .tags(tags)
                .user(user)
                .build();

        Post savedPost = postRepository.save(post);

        return PostDTO.createResponse.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .postType(savedPost.getType())
                .survey(savedPost.getSurvey())
                .deadline(savedPost.getDeadline())
                .tags(tags.stream().map(Tag::getName).collect(Collectors.toList()))
                .build();
    }

    public PostDTO.getPostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("해당 포스트는 존재하지 않습니다."));

        User user = post.getUser();
        String userName = user.getName();

        if(userName == null){
            throw new RuntimeException("유저의 이름을 조회할 수 없습니다.");
        }

        List<String> tags = post.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toList());

        PostDTO.getPostResponse response = new PostDTO.getPostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setPostType(post.getType());
        response.setSurvey(post.getSurvey());
        response.setDeadline(post.getDeadline());
        response.setTags(tags);
        response.setAuthor(userName);

        return response;
    }

    public PostDTO.getAllPostResponse getPostByTag(String tagName){
        List<Post> posts = postRepository.findByTagsName(tagName);

        List<PostDTO.getAllPostResponse.PostSummary> postSummaries = posts.stream()
                .map(post -> {
                    List<String> tags = post.getTags().stream()
                            .map(Tag::getName)
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

                    List<String> tags = post.getTags().stream()
                            .map(Tag::getName)
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

    public PostDTO.getAllPostResponse getAllPosts(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        Page<Post> posts = postRepository.findAll(pageRequest);

        List<PostDTO.getAllPostResponse.PostSummary> postSummaries = posts.stream()
                .map(post -> {
                    List<String> tags = post.getTags().stream()
                            .map(Tag::getName)
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
