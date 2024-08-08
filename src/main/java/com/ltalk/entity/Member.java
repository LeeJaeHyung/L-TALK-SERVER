package com.ltalk.entity;

import com.ltalk.config.PasswordEncoder;
import com.ltalk.request.LoginRequest;
import com.ltalk.request.SignupRequest;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "members")
@Setter
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false , unique = true )
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true )
    private String email;

    public Member(SignupRequest request) throws NoSuchAlgorithmException {
        this.username = request.getUsername();
        this.password = new PasswordEncoder().encode(request.getPassword());
        this.email = request.getEmail();
    }

    public Member(LoginRequest request) throws NoSuchAlgorithmException {
        this.username = request.getUsername();
        this.password = new PasswordEncoder().encode(request.getPassword());
    }

}
