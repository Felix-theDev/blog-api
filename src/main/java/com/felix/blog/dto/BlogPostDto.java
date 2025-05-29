package com.felix.blog.dto;

import com.felix.blog.entity.BlogPost;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BlogPostDto {
    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private LocalDateTime createdAt;


    public static BlogPostDto mapToDto(BlogPost blog) {
        return BlogPostDto.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .authorUsername(blog.getAuthor().getUsername())
                .createdAt(blog.getCreatedAt())
                .build();
    }
}
