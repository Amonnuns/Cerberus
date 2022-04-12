package com.amonnuns.testing;

import com.amonnuns.testing.DoormanServiceTest;
import com.amonnuns.testing.UserRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {DoormanServiceTest.class, UserRepositoryTest.class})
class ApplicationTests {

    @Test
    void contextLoads() {
    }
}
