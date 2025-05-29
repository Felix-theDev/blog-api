package com.felix.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
@ToString(exclude = "blogPosts")
public class User {

    public enum Role {
        EMPLOYEE,
        NON_EMPLOYEE,
        ADMIN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled = true;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Set<BlogPost> blogPosts;
}