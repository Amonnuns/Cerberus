package com.amonnuns.gate;

import com.amonnuns.gate.rabbitmq.RabbitNotify;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/gate")
public class GateController {

    private final GateService gateService;
    private final RabbitNotify rabbitNotify;

    public GateController(GateService gateService, RabbitNotify rabbitNotify) {
        this.gateService = gateService;
        this.rabbitNotify = rabbitNotify;
    }

    @PostMapping("/entry")
    public ResponseEntity<Object> entrada( @RequestBody UserLoginForm userLoginForm){

        boolean hasPermission = gateService.verificaPermissao(userLoginForm);
        if(hasPermission) {
            if (gateService.hasExited(userLoginForm.getUsername())) {
                String message = "%s has entered".formatted(userLoginForm.getUsername());
                rabbitNotify.enviarNotificacao(message);
                gateService.saveEntry(userLoginForm);
                return ResponseEntity.status(HttpStatus.OK).body("Acesso liberado");
            }
            String message = "%s has already entered".formatted(userLoginForm.getUsername());
            rabbitNotify.enviarNotificacao(message);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Entrada Duplicada");
        }
        String message = "Attempt:%s access denied".formatted(userLoginForm.getUsername());
        rabbitNotify.enviarNotificacao(message);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso Negado");

    }

    @PostMapping("/exit")
    public ResponseEntity<Object> saida( @RequestBody UserLoginForm userLoginForm) {

        boolean hasPermission = gateService.verificaPermissao(userLoginForm);
        if (hasPermission) {
            if (gateService.hasEntered(userLoginForm.getUsername())) {
                String message = "%s has exited".formatted(userLoginForm.getUsername());
                rabbitNotify.enviarNotificacao(message);
                gateService.saveExit(userLoginForm);
                return ResponseEntity.status(HttpStatus.OK).body("Saida liberada");
            }
            String message = "Attempt:%s exit without an entry".formatted(userLoginForm.getUsername());
            rabbitNotify.enviarNotificacao(message);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tentativa de saída sem entrada");
        }
        String message = "Attempt:%s access denied".formatted(userLoginForm.getUsername());
        rabbitNotify.enviarNotificacao(message);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso Negado");
    }
}
