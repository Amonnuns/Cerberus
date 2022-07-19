package com.amonnuns.gate;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class GateService {

    private final WebClient webClient;
    private final GateRepository gateRepository;

    public GateService(WebClient webClient, GateRepository gateRepository) {
        this.webClient = webClient;
        this.gateRepository = gateRepository;
    }

    public boolean verificaPermissao(UserLoginForm userLoginForm) {
        Mono<Boolean> result = null;
        result = this.webClient.method(HttpMethod.POST)
                .uri("/api/v1/doorman/permission")
                .bodyValue(userLoginForm)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorReturn(false);

        return result.block();
    }

    public void saveEntry(UserLoginForm userLoginForm) {

        UserHistoricalModel userhistorical = new UserHistoricalModel();
        userhistorical.setUserName(userLoginForm.getUsername());
        userhistorical.setActionType("Entrada");
        userhistorical.setDateOfInsert(LocalDateTime.now(ZoneId.of("UTC")));

        gateRepository.save(userhistorical);
    }

    public void saveExit(UserLoginForm userLoginForm) {

        UserHistoricalModel userhistorical = new UserHistoricalModel();
        userhistorical.setUserName(userLoginForm.getUsername());
        userhistorical.setActionType("Saída");
        userhistorical.setDateOfInsert(LocalDateTime.now(ZoneId.of("UTC")));

        gateRepository.save(userhistorical);
    }

    public boolean hasExited(String username) {
        Optional<UserHistoricalModel> historical = Optional.ofNullable(gateRepository
                .findFirstByUserNameOrderByDateOfInsertDesc(username));

        String exit = "Saída";
        return historical.isEmpty() || exit.equals(historical.get().getActionType());
    }

    public boolean hasEntered(String username) {
        UserHistoricalModel historical = gateRepository
                .findFirstByUserNameOrderByDateOfInsertDesc(username);

        String entry = "Entrada";
        return entry.equals(historical.getActionType());
    }
}
