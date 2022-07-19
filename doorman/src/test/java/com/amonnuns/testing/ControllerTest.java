package com.amonnuns.testing;

import com.amonnuns.doorman.*;
import com.amonnuns.doorman.rabbitmq.RabbitNotify;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {com.amonnuns.doorman.DoormanApplication.class})
@WebMvcTest(controllers = DoormanController.class)
class ControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoormanService doormanService;

    @MockBean
    private RabbitNotify rabbitNotify;

    @Test
    void testVoidUserDto() throws Exception {
        var user = new UserDto();
        String body = objectMapper.writeValueAsString(user);

        mvc.perform(post("/api/v1/doorman/subscribe")
                .contentType("application/json")
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testVoidUserLoginForm() throws Exception {
        var userLoginForm = new UserLoginForm();
        String body = objectMapper.writeValueAsString(userLoginForm);

        mvc.perform(post("/api/v1/doorman/permission")
                .contentType("application/json")
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPasswordExcedingMaxLimit() throws Exception {
        var userLoginForm = new UserLoginForm();
        userLoginForm.setPassword("morethantwelvecharacters");
        String body = objectMapper.writeValueAsString(userLoginForm);

        mvc.perform(post("/api/v1/doorman/permission")
                .contentType("application/json")
                .content(body))
                .andExpect(status().isBadRequest());
    }

}
