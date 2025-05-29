package com.felix.blog.controller;


import com.felix.blog.dto.ApiResponse;
import com.felix.blog.dto.BlogPostDto;
import com.felix.blog.entity.BlogPost;
import com.felix.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BlogPostDto>>> listBlogs() {
        try {
            List<BlogPostDto> blogs = blogService.getAllApprovedBlogs();

            return ResponseEntity.ok(
                    ApiResponse.<List<BlogPostDto>>builder()
                            .success(true)
                            .message("Approved blog posts retrieved successfully")
                            .data(blogs)
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<BlogPostDto>>builder()
                            .success(false)
                            .message("Failed to retrieve blog posts: " + e.getMessage())
                            .data(null)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BlogPostDto>> getBlog(@PathVariable Long id) {
        try {
            BlogPostDto blog = blogService.getBlogById(id);

            return ResponseEntity.ok(
                    ApiResponse.<BlogPostDto>builder()
                            .success(true)
                            .message("Blog post retrieved successfully")
                            .data(blog)
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<BlogPostDto>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .status(HttpStatus.NOT_FOUND)
                            .build()
                    );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<BlogPostDto>builder()
                            .success(false)
                            .message("An error occurred: " + e.getMessage())
                            .data(null)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build()
                    );
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BlogPostDto>> createBlog(
            @RequestBody BlogPost blog,
            Principal principal) {
        try {
            System.out.println("In the controller");
            BlogPost createdBlog = blogService.createBlog(blog, principal.getName());

            String message = createdBlog.getStatus() == BlogPost.Status.APPROVED
                    ? "Blog post created and automatically approved"
                    : "Blog post created and pending approval";

            BlogPostDto blogDto = BlogPostDto.mapToDto(createdBlog);

            return ResponseEntity.ok(
                    ApiResponse.<BlogPostDto>builder()
                            .success(true)
                            .message(message)
                            .data(blogDto)
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<BlogPostDto>builder()
                            .success(false)
                            .message("Failed to create blog post: " + e.getMessage())
                            .data(null)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BlogPostDto>> updateBlog(@PathVariable Long id,
                                                               @RequestBody BlogPost blog,
                                                               Principal principal) {
        try {
            BlogPostDto updatedDto = blogService.updateBlog(id, blog, principal.getName());

            return ResponseEntity.ok(
                    ApiResponse.<BlogPostDto>builder()
                            .success(true)
                            .message("Blog post updated successfully")
                            .data(updatedDto)
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<BlogPostDto>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .status(HttpStatus.FORBIDDEN)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<BlogPostDto>builder()
                            .success(false)
                            .message("Failed to update blog post: " + e.getMessage())
                            .data(null)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBlog(@PathVariable Long id, Principal principal) {
        try {
            blogService.deleteBlog(id, principal.getName());

            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .success(true)
                            .message("Blog post delete request successful submitted")
                            .data(null)
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.<Void>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .status(HttpStatus.FORBIDDEN)
                            .build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<Void>builder()
                            .success(false)
                            .message("Blog post not found")
                            .data(null)
                            .status(HttpStatus.NOT_FOUND)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Void>builder()
                            .success(false)
                            .message("Failed to process delete request: " + e.getMessage())
                            .data(null)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build());
        }
    }


    @PostMapping("/{id}/approve-delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> approveDelete(@PathVariable Long id) {
        try {
            String message = blogService.approveDelete(id);

            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .success(true)
                            .message(message)
                            .data(null)
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<Void>builder()
                            .success(false)
                            .message("Failed to delete: " + e.getMessage())
                            .data(null)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build());
        }
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BlogPostDto>> approveBlog(@PathVariable Long id) {
        try {
            BlogPostDto approvedBlog = blogService.approveBlog(id);

            return ResponseEntity.ok(
                    ApiResponse.<BlogPostDto>builder()
                            .success(true)
                            .message("Blog has been approved successfully")
                            .data(approvedBlog)
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<BlogPostDto>builder()
                            .success(false)
                            .message("Failed to approve blog post: " + e.getMessage())
                            .data(null)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build());
        }
    }

}
