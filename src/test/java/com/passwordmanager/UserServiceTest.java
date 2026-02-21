package com.passwordmanager;

import com.passwordmanager.service.UserService;
import com.passwordmanager.entity.User;
import com.passwordmanager.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testUserRegistration() {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setMasterPasswordHash("Test@123");

        User savedUser = userService.register(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
    }
}
