package com.seonbistudy.seonbistudy.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.seonbistudy.seonbistudy.model.entity.Account;
import com.seonbistudy.seonbistudy.model.entity.User;

public interface IUserService extends UserDetailsService {
    
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    UserDetails loadUserByEmail(String email) throws UsernameNotFoundException;
    Account findAccountByEmail(String email);
    Account createAccountWithUser(Account account, User user);
    boolean isAccountEnabled(Account account);
}
