package com.seonbistudy.seonbistudy.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.seonbistudy.seonbistudy.model.entity.User;

public interface IUserService extends UserDetailsService {
    
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    
    UserDetails loadUserByEmail(String email) throws UsernameNotFoundException;
    
    User findByEmail(String email);
    
    User createUser(User user);
    
    boolean isAccountEnabled(User user);
}
