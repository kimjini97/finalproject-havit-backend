package com.havit.finalbe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.havit.finalbe.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String profileUrl;

    public boolean isValidateMember(Long memberId) {
        return this.memberId == memberId;
    }

    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public void edit(String profileUrl, String nickname, String password) {
        this.profileUrl = profileUrl;
        this.nickname = nickname;
        this.password = password;
    }

    public void deleteImg(String profileUrl) {
        this.profileUrl = profileUrl;
    }

}
