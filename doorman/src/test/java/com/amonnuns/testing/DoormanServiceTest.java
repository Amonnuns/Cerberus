package com.amonnuns.testing;


import com.amonnuns.doorman.DoormanService;
import com.amonnuns.doorman.User;
import com.amonnuns.doorman.UserLoginForm;
import com.amonnuns.doorman.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;

import java.time.LocalDateTime;
import java.util.Optional;

class DoormanServiceTest {

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    private DoormanService underTest;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        underTest = new DoormanService(userRepository);
    }


    @Test
    void deveSalvarNovoUsuario(){

        String userName = "userteste";
        String password = "passwordtest";

        User user = new User("user", "teste",
                userName, password, false,LocalDateTime.now());

        given(userRepository.findByUserNameAndPassword(userName, password))
                .willReturn(Optional.empty());

        underTest.cadastrarUsuário(user);

        then(userRepository).should().save(userArgumentCaptor.capture());

        User userArgumentCaptorValue = userArgumentCaptor.getValue();

        assertThat(userArgumentCaptorValue).isEqualTo(user);

    }

    @Test
    void naoDeveSalvarUsuarioExistente(){
        String userName = "userteste";
        String password = "passwordtest";

        User user = new User("user", "teste",
                userName, password, false,LocalDateTime.now());
        Example<User> userExample = Example.of(user);
        given(userRepository.exists(userExample))
                .willReturn(true);

        // When
        underTest.cadastrarUsuário(user);

        // Then
        then(userRepository).should(never()).save(any());
    }

    @Test
    void deveRetornarPermissaoTrueParaUsuarioAutenticadoENaoBloqueado(){

        String userName = "userteste";
        String password = "passwordtest";

        UserLoginForm loginForm = new UserLoginForm(userName, password);

        User user = new User("user", "teste",
                userName, password, false,LocalDateTime.now());

        given(userRepository.findByUserNameAndPassword(userName, password)).willReturn(Optional.of(user));

        assertThat(underTest.verificaPermissao(loginForm))
                .isEqualTo(true);
    }
    @Test
    void deveRetornarPermissaoFalseParaUsuarioAutenticadoEBloqueado(){
        String userName = "userteste";
        String password = "passwordtest";

        UserLoginForm loginForm = new UserLoginForm(userName, password);

        User user = new User("user", "teste",
                userName, password, true,LocalDateTime.now());

        given(userRepository.findByUserNameAndPassword(userName, password)).willReturn(Optional.of(user));

        assertThat(underTest.verificaPermissao(loginForm))
                .isEqualTo(false);
    }

    @Test
    void deveRetornarPermissaoFalseParaUsuarioNaoAutenticado(){
        String userName = "userteste";
        String password = "passwordtest";

        UserLoginForm loginForm = new UserLoginForm(userName, password);

        given(userRepository.findByUserNameAndPassword(userName, password)).willReturn(Optional.empty());

        assertThat(underTest.verificaPermissao(loginForm))
                .isEqualTo(false);
    }



}
