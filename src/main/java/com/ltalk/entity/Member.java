package com.ltalk.entity;

import com.ltalk.config.PasswordEncoder;
import com.ltalk.request.SignupRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
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

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    public Member(SignupRequest request) throws NoSuchAlgorithmException {
        this.username = request.getUsername();
        this.password = new PasswordEncoder().encode(request.getPassword());
        this.email = request.getEmail();
    }

}
