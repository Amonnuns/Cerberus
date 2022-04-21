package com.amonnuns.doorman;

import com.amonnuns.doorman.rabbitmq.RabbitNotify;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/doorman")
public class DoormanController {

    private final DoormanService doormanService;
    private final RabbitNotify rabbitNotify;

    public DoormanController(DoormanService doormanService, RabbitNotify rabbitNotify) {
        this.doormanService = doormanService;
        this.rabbitNotify = rabbitNotify;
    }


    @PostMapping("/subscribe")
    public ResponseEntity<Object> cadastraUsuario(@RequestBody User user){

        Optional<User> userReturn = doormanService.cadastrarUsuário(user);
        if(userReturn.isEmpty()){
            return ResponseEntity.status(HttpStatus.CREATED).body("User already created");
        }
        String message = "User: %s was created with username: %s".formatted(
                user.getFirstName(), user.getUserName());
        rabbitNotify.enviarNotificacao(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(userReturn.get());


    }

    @PostMapping("/permission")
    public boolean verificaPermissao(@RequestBody UserLoginForm loginForm){

        return doormanService.verificaPermissao(loginForm);
    }

    @PostMapping("/block/{username}")
    public ResponseEntity<Object> bloqueiaUsuario(@PathVariable(value = "username") String username){

        if(doormanService.bloqueiaUsuario(username)){
            String message = "User with username: %s was blocked".formatted(username);
            rabbitNotify.enviarNotificacao(message);
            return ResponseEntity.status(HttpStatus.OK).body("Usuário bloqueado");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esse usuário não existe");
    }
    @PostMapping("/unblock/{username}")
    public ResponseEntity<Object> desbloqueiaUsuario(@PathVariable(value = "username") String username){

        if(doormanService.desbloqueiaUsuario(username)){
            String message = "User with username: %s was unblocked".formatted(username);
            rabbitNotify.enviarNotificacao(message);
            return ResponseEntity.status(HttpStatus.OK).body("Usuário desbloqueado");

        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esse usuário não existe");
    }


}
