package com.amonnuns.gate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/gate")
public class GateController {

    GateService gateService;

    public GateController(GateService gateService) {
        this.gateService = gateService;
    }

    @PostMapping("/entry")
    public ResponseEntity<Object> entrada( @RequestBody UserLoginForm userLoginForm){

        if(gateService.hasExited(userLoginForm.getUsername())) {
            boolean hasPermission = gateService.verificaPermissao(userLoginForm);
            if (hasPermission) {
                //todo: avisa ao nodemcu
                gateService.saveEntry(userLoginForm);
                return ResponseEntity.status(HttpStatus.OK).body("Acesso liberado");
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso Negado");
        }
        //todo: avisa nodemcu
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Entrada Duplicada");
    }

    @PostMapping("/exit")
    public ResponseEntity<Object> saida( @RequestBody UserLoginForm userLoginForm) {

        if (gateService.hasEntry(userLoginForm.getUsername())) {
            boolean hasPermission = gateService.verificaPermissao(userLoginForm);
            //todo: verificar se existe entrada sem saída
            if (hasPermission) {
                //todo: avisa nodemcu
                gateService.saveExit(userLoginForm);
                return ResponseEntity.status(HttpStatus.OK).body("Saida liberada");
            }
            //todo: avisa nodemcu
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso Negado");
        }
        //todo:avisa nodemcu
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tentativa de saída sem entrada");
    }
}
