package com.amonnuns.testing;


import com.amonnuns.doorman.User;
import com.amonnuns.doorman.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;


@ContextConfiguration(classes = {com.amonnuns.doorman.DoormanApplication.class})
@DataJpaTest(properties = {
        "spring.jpa.properties.javax.persistence.validation.mode=none"
})
public class UserRepositoryTest {

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
                .hasValueSatisfying(c -> assertThat(c).isEqualTo(user)
                );
    }

    @Test
    void deveEncontrarPorUsername(){
        String userName = "userteste";
        String password = "passwordtest";

        User user = new User("user", "teste",
                userName, password, false, LocalDateTime.now());

        userRepository.save(user);

        User userReturned = userRepository.findByUserName(userName);
        assertThat(userReturned.getUserName())
                .isEqualTo(userName);
    }

}
