package com.felix.blog.service;
import com.felix.blog.dto.BlogPostDto;
import com.felix.blog.entity.BlogPost;

import java.util.List;

public interface BlogService {
    List<BlogPostDto> getAllApprovedBlogs();
    BlogPostDto getBlogById(Long id);
    BlogPost createBlog(BlogPost blog, String username);
    BlogPostDto updateBlog(Long id, BlogPost updated, String username);
    void deleteBlog(Long id, String username);
    BlogPostDto approveBlog(Long id);
    String approveDelete(Long id);

}
