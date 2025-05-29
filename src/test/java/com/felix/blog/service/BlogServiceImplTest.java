package com.felix.blog.service;

import com.felix.blog.dto.BlogPostDto;
import com.felix.blog.entity.BlogPost;
import com.felix.blog.entity.User;
import com.felix.blog.repository.BlogRepository;
import com.felix.blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlogServiceImplTest {

    @InjectMocks
    private BlogServiceImpl blogService;

    @Mock
    private BlogRepository blogRepo;

    @Mock
    private UserRepository userRepo;

    private User employeeUser;
    private BlogPost blogPost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employeeUser = User.builder()
                .id(1L)
                .username("john")
                .password("123")
                .role(User.Role.EMPLOYEE)
                .build();

        blogPost = BlogPost.builder()
                .id(1L)
                .title("Title")
                .content("Content")
                .status(BlogPost.Status.PENDING)
                .author(employeeUser)
                .build();
    }

    @Test
    void testCreateBlog_AsEmployee_ShouldBeApproved() {
        when(userRepo.findByUsername("john")).thenReturn(Optional.of(employeeUser));
        when(blogRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        BlogPost blog = BlogPost.builder()
                .title("My Title")
                .content("My Content")
                .build();

        BlogPost result = blogService.createBlog(blog, "john");

        assertEquals(BlogPost.Status.APPROVED, result.getStatus());
        assertEquals(employeeUser, result.getAuthor());
    }

    @Test
    void testCreateBlog_InvalidTitle_ShouldThrow() {
        BlogPost blog = BlogPost.builder().title(" ").content("valid").build();
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> blogService.createBlog(blog, "john"));
        assertEquals("Blog title cannot be empty", ex.getMessage());
    }

    @Test
    void testDeleteBlog_AsNonEmployee_ShouldMarkPendingDelete() {
        User nonEmployee = User.builder()
                .username("jane")
                .role(User.Role.NON_EMPLOYEE)
                .build();
        blogPost.setAuthor(nonEmployee);

        when(blogRepo.findById(1L)).thenReturn(Optional.of(blogPost));
        when(userRepo.findByUsername("jane")).thenReturn(Optional.of(nonEmployee));

        blogService.deleteBlog(1L, "jane");

        assertTrue(blogPost.isPendingDelete());
        assertEquals(BlogPost.Status.PENDING, blogPost.getStatus());
        verify(blogRepo).save(blogPost);
    }

    @Test
    void testApproveBlog_ShouldSetStatusApproved() {
        blogPost.setStatus(BlogPost.Status.PENDING);
        blogPost.setCreatedAt(LocalDateTime.now());
        when(blogRepo.findById(1L)).thenReturn(Optional.of(blogPost));
        when(blogRepo.save(blogPost)).thenReturn(blogPost);

        BlogPostDto result = blogService.approveBlog(1L);

        assertEquals("john", result.getAuthorUsername());
        assertEquals("Title", result.getTitle());
    }

    @Test
    void testApproveDelete_NotMarked_ShouldThrow() {
        blogPost.setPendingDelete(false);
        when(blogRepo.findById(1L)).thenReturn(Optional.of(blogPost));

        Exception ex = assertThrows(IllegalStateException.class,
                () -> blogService.approveDelete(1L));
        assertEquals("This blog is not marked for deletion.", ex.getMessage());
    }

    @Test
    void testUpdateBlog_NotAuthor_ShouldThrow() {
        blogPost.setAuthor(employeeUser); // actual author is john
        when(blogRepo.findById(1L)).thenReturn(Optional.of(blogPost));

        Exception ex = assertThrows(AccessDeniedException.class,
                () -> blogService.updateBlog(1L, blogPost, "someone-else"));
        assertEquals("You can only update your own posts.", ex.getMessage());
    }
}
