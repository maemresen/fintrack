package com.maemresen.fintrack.persistence.it;

import com.maemresen.fintrack.persistence.entity.UserEntity;
import com.maemresen.fintrack.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRepositoryIT extends AbstractBaseJpaTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveUserTest(){
        final UserEntity user = new UserEntity();
        user.setEmail("myemail");

        final UserEntity saveUser = assertDoesNotThrow(() -> userRepository.save(user));
        assertEquals(user.getEmail(), saveUser.getEmail());
    }
}
