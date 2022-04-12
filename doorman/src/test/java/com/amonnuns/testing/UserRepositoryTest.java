package com.amonnuns.testing;


import com.amonnuns.doorman.User;
import com.amonnuns.doorman.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void deveEncontrarUsuarioPorUsernameEPassword(){

        String userName = "userteste";
        String password = "passwordtest";

        User user = new User("user", "teste",
                userName, password, false, LocalDateTime.now());

        userRepository.save(user);

        Optional<User> optionalUser = userRepository
                .findByUserNameAndPassword(userName, password);

        assertThat(optionalUser)
                .isPresent()
                .isEqualTo(user);
    }

}
