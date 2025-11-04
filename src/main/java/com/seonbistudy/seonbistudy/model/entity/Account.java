package com.seonbistudy.seonbistudy.model.entity;

import java.util.Collection;
import java.util.Collections;

import com.seonbistudy.seonbistudy.model.enums.AccountStatus;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.seonbistudy.seonbistudy.model.base.BaseEntity;
import com.seonbistudy.seonbistudy.model.enums.AuthProvider;
import com.seonbistudy.seonbistudy.model.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity implements UserDetails {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "hashed_password")
    private String hashedPassword;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    private AccountStatus accountStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private AuthProvider provider = AuthProvider.LOCAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.STUDENT;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "is_receive_emails", nullable = false)
    private Boolean isReceiveEmails;

    @Column(name = "is_accept_terms")
    private Boolean isAcceptTerms;

    @OneToOne(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Streak streak;

    @Override
    public String getPassword() {
        return hashedPassword;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return AccountStatus.ACTIVE.equals(accountStatus);
    }
}