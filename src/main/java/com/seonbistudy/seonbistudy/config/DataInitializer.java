package com.seonbistudy.seonbistudy.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.seonbistudy.seonbistudy.model.entity.Account;
import com.seonbistudy.seonbistudy.model.entity.User;
import com.seonbistudy.seonbistudy.model.enums.AuthProvider;
import com.seonbistudy.seonbistudy.model.enums.Role;
import com.seonbistudy.seonbistudy.repository.AccountRepository;
import com.seonbistudy.seonbistudy.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeUsers();
    }

    private void initializeUsers() {
        // Initialize ADMIN account
        if (!accountRepository.existsByUsername("admin")) {
            Account adminAccount = Account.builder()
                    .username("admin")
                    .email("admin@seonbistudy.com")
                    .hashedPassword(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .provider(AuthProvider.LOCAL)
                    .enabled(true)
                    .build();
            adminAccount = accountRepository.save(adminAccount);
            
            User adminUser = User.builder()
                    .account(adminAccount)
                    .fullName("System Administrator")
                    .build();
            userRepository.save(adminUser);
            
            log.info("✅ Created ADMIN account - Username: admin, Password: admin123");
        }

        // Initialize INSTRUCTOR account
        if (!accountRepository.existsByUsername("instructor")) {
            Account instructorAccount = Account.builder()
                    .username("instructor")
                    .email("instructor@seonbistudy.com")
                    .hashedPassword(passwordEncoder.encode("instructor123"))
                    .role(Role.INSTRUCTOR)
                    .provider(AuthProvider.LOCAL)
                    .enabled(true)
                    .build();
            instructorAccount = accountRepository.save(instructorAccount);
            
            User instructorUser = User.builder()
                    .account(instructorAccount)
                    .fullName("Demo Instructor")
                    .build();
            userRepository.save(instructorUser);
            
            log.info("✅ Created INSTRUCTOR account - Username: instructor, Password: instructor123");
        }

        // Initialize STUDENT account
        if (!accountRepository.existsByUsername("student")) {
            Account studentAccount = Account.builder()
                    .username("student")
                    .email("student@seonbistudy.com")
                    .hashedPassword(passwordEncoder.encode("student123"))
                    .role(Role.STUDENT)
                    .provider(AuthProvider.LOCAL)
                    .enabled(true)
                    .build();
            studentAccount = accountRepository.save(studentAccount);
            
            User studentUser = User.builder()
                    .account(studentAccount)
                    .fullName("Demo Student")
                    .build();
            userRepository.save(studentUser);
            
            log.info("✅ Created STUDENT account - Username: student, Password: student123");
        }

        log.info("🎉 Data initialization completed!");
    }
}
