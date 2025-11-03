package com.seonbistudy.seonbistudy.service.impl;

import java.time.LocalDateTime;

import com.seonbistudy.seonbistudy.model.entity.Streak;
import com.seonbistudy.seonbistudy.model.entity.UserProgress;
import com.seonbistudy.seonbistudy.model.enums.XpActivityType;
import com.seonbistudy.seonbistudy.repository.StreakRepository;
import com.seonbistudy.seonbistudy.repository.UserProgressRepository;
import com.seonbistudy.seonbistudy.service.IStreakService;
import com.seonbistudy.seonbistudy.service.IXpService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seonbistudy.seonbistudy.model.entity.Account;
import com.seonbistudy.seonbistudy.model.entity.User;
import com.seonbistudy.seonbistudy.repository.AccountRepository;
import com.seonbistudy.seonbistudy.repository.UserRepository;
import com.seonbistudy.seonbistudy.service.IUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final IXpService xpService;
    private final IStreakService streakService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found with username: " + username));
    }

    @Override
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found with email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElse(null);
    }

    @Override
    @Transactional
    public Account createAccountWithUser(Account account, User user) {
        Account savedAccount = accountRepository.save(account);
        user.setAccount(savedAccount);
        userRepository.save(user);

        streakService.initStreak(savedAccount);
        xpService.initProgress(savedAccount);

        return savedAccount;
    }

    @Override
    public boolean isAccountEnabled(Account account) {
        return account != null && account.isEnabled();
    }
}
