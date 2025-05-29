package com.felix.blog.repository;

import com.felix.blog.entity.BlogPost;
import com.felix.blog.entity.BlogPost.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BlogRepository extends JpaRepository<BlogPost, Long> {

    List<BlogPost> findByStatus(Status status);
}
