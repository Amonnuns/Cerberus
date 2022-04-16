package com.amonnuns.testing;

import com.amonnuns.gate.GateRepository;
import com.amonnuns.gate.GateService;
import com.amonnuns.gate.UserHistoricalModel;
import com.amonnuns.gate.UserLoginForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

public class GateServiceTests {

    @Mock
    private WebClient webClient;

    @Mock
    private GateRepository gateRepository;

    @Captor
    private ArgumentCaptor<UserHistoricalModel> userHistoricalArgumentCaptor;

    private GateService underTest;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        underTest = new GateService(webClient, gateRepository);
    }

    @Test
    void  deveSalvarNovaEntradaNoHistorico(){
        String userName = "userteste";
        String password = "passwordtest";

        UserLoginForm loginForm = new UserLoginForm(userName, password);

        underTest.saveEntry(loginForm);

        then(gateRepository).should().save(userHistoricalArgumentCaptor.capture());

        UserHistoricalModel userHistoricalModel = userHistoricalArgumentCaptor.getValue();

        assertThat(userHistoricalModel.getUserName())
                .isEqualTo(userName);

        assertThat(userHistoricalModel.getActionType())
                .isEqualTo("Entrada");
    }

    @Test
    void deveSalvarNovaSaídaNoHistorico(){
        String userName = "userteste";
        String password = "passwordtest";

        UserLoginForm loginForm = new UserLoginForm(userName, password);

        underTest.saveExit(loginForm);

        then(gateRepository).should().save(userHistoricalArgumentCaptor.capture());

        UserHistoricalModel userHistoricalModel = userHistoricalArgumentCaptor.getValue();

        assertThat(userHistoricalModel.getUserName())
                .isEqualTo(userName);

        assertThat(userHistoricalModel.getActionType())
                .isEqualTo("Saída");

    }

    @Test
    void deveRetornarVerdadeiroSeUltimoRegistroForDeSaida(){

        String userName = "userteste";
        String actionType = "Saída";

        UserHistoricalModel userHistorical = new UserHistoricalModel(
                userName, actionType, LocalDateTime.now()
        );

        given(gateRepository
                .findFirstByUserNameOrderByDateOfInsertDesc(userName))
                .willReturn(userHistorical);

        Boolean valueReturned = underTest.hasExited(userName);

        assertThat(valueReturned).isEqualTo(true);

    }

    @Test
    void deveRetornarFalsoSeUltimoRegistroForDeEntrada(){

        String userName = "userteste";
        String actionType = "Entrada";

        UserHistoricalModel userHistorical = new UserHistoricalModel(
                userName, actionType, LocalDateTime.now()
        );

        given(gateRepository
                .findFirstByUserNameOrderByDateOfInsertDesc(userName))
                .willReturn(userHistorical);

        Boolean valueReturned = underTest.hasExited(userName);

        assertThat(valueReturned).isEqualTo(false);
    }

    @Test
    void deveRetornarVerdadeiroSeUltimoRegistroForDeEntrada(){
        String userName = "userteste";
        String actionType = "Entrada";

        UserHistoricalModel userHistorical = new UserHistoricalModel(
                userName, actionType, LocalDateTime.now()
        );

        given(gateRepository
                .findFirstByUserNameOrderByDateOfInsertDesc(userName))
                .willReturn(userHistorical);

        Boolean valueReturned = underTest.hasEntered(userName);

        assertThat(valueReturned).isEqualTo(true);

    }

    @Test
    void deveRetornarFalsoSeUltimoRegistroForDeSaida(){
        String userName = "userteste";
        String actionType = "Saída";

        UserHistoricalModel userHistorical = new UserHistoricalModel(
                userName, actionType, LocalDateTime.now()
        );

        given(gateRepository
                .findFirstByUserNameOrderByDateOfInsertDesc(userName))
                .willReturn(userHistorical);

        Boolean valueReturned = underTest.hasEntered(userName);

        assertThat(valueReturned).isEqualTo(false);
    }


}
