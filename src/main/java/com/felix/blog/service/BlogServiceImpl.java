package com.felix.blog.service;

import com.felix.blog.dto.BlogPostDto;
import com.felix.blog.entity.BlogPost;
import com.felix.blog.entity.User;
import com.felix.blog.repository.BlogRepository;
import com.felix.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BlogServiceImpl implements BlogService{
    @Autowired
    private BlogRepository blogRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public List<BlogPostDto> getAllApprovedBlogs() {
        List<BlogPost> blogs = blogRepo.findByStatus(BlogPost.Status.APPROVED);
        return blogs.stream()
                .map(BlogPostDto::mapToDto)
                .toList();
    }

    @Override
    public BlogPostDto getBlogById(Long id) {
        BlogPost blog = blogRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Blog post not found with ID: " + id));

        return BlogPostDto.mapToDto(blog);
    }

    @Override
    public BlogPost createBlog(BlogPost blog, String username) {
        // Validate input
        if (blog.getTitle() == null || blog.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Blog title cannot be empty");
        }

        if (blog.getContent() == null || blog.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Blog content cannot be empty");
        }

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        blog.setAuthor(user);

        // Set status based on user role
        blog.setStatus(user.getRole() == User.Role.EMPLOYEE || user.getRole() == User.Role.ADMIN
                ? BlogPost.Status.APPROVED
                : BlogPost.Status.PENDING);

        return blogRepo.save(blog);
    }

    @Override
    public BlogPostDto updateBlog(Long id, BlogPost updated, String username) {
        BlogPost existing = blogRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Blog post not found with ID: " + id));

        if (!existing.getAuthor().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only update your own posts.");
        }

        existing.setTitle(updated.getTitle());
        existing.setContent(updated.getContent());

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (user.getRole() == User.Role.NON_EMPLOYEE) {
            existing.setStatus(BlogPost.Status.PENDING);
        }

        BlogPost saved = blogRepo.save(existing);
        return BlogPostDto.mapToDto(saved);
    }


    @Override
    public void deleteBlog(Long id, String username) {
        BlogPost blog = blogRepo.findById(id).orElseThrow();
        if (!blog.getAuthor().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only delete your own posts.");
        }

        User user = userRepo.findByUsername(username).orElseThrow();
        if (user.getRole() == User.Role.NON_EMPLOYEE) {
            blog.setPendingDelete(true); // mark for admin
            blog.setStatus(BlogPost.Status.PENDING); // optionally reset status
            blogRepo.save(blog);
        } else {
            blogRepo.delete(blog);
        }
    }

    @Override
    public BlogPostDto approveBlog(Long id) {
        BlogPost blog = blogRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Blog post not found with ID: " + id));

        blog.setStatus(BlogPost.Status.APPROVED);
        BlogPost saved = blogRepo.save(blog);

        return BlogPostDto.mapToDto(saved);
    }

    @Override
    public String approveDelete(Long id) {
        BlogPost blog = blogRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Blog post not found with ID: " + id));

        if (blog.isPendingDelete()) {
            blogRepo.delete(blog);
            return "Blog post has been deleted successfully.";
        } else {
            throw new IllegalStateException("This blog is not marked for deletion.");
        }
    }

}
