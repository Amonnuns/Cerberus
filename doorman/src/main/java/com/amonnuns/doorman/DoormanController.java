package com.amonnuns.doorman;

import com.amonnuns.doorman.rabbitmq.RabbitNotify;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    public ResponseEntity<Object> cadastraUsuario(@Valid @RequestBody UserDto userDto){

        var user = new User();
        BeanUtils.copyProperties(userDto,user);

        Optional<User> userReturn = doormanService.cadastrarUsuário(user);
        if(userReturn.isEmpty()){
            return ResponseEntity.status(HttpStatus.CREATED).body("User already created");
        }
        String message = "username: %s was created".formatted(user.getUserName());
        rabbitNotify.enviarNotificacao(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(userReturn.get());


    }

    @PostMapping("/permission")
    public boolean verificaPermissao(@Valid @RequestBody UserLoginForm loginForm){

        return doormanService.verificaPermissao(loginForm);
    }

    @PostMapping("/block/{username}")
    public ResponseEntity<Object> bloqueiaUsuario(@NotNull @PathVariable(value = "username") String username){

        if(doormanService.bloqueiaUsuario(username)){
            String message = "Username: %s was blocked".formatted(username);
            rabbitNotify.enviarNotificacao(message);
            return ResponseEntity.status(HttpStatus.OK).body("Usuário bloqueado");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esse usuário não existe");
    }
    @PostMapping("/unblock/{username}")
    public ResponseEntity<Object> desbloqueiaUsuario(@NotNull @PathVariable(value = "username") String username){

        if(doormanService.desbloqueiaUsuario(username)){
            String message = "Username: %s was unblocked".formatted(username);
            rabbitNotify.enviarNotificacao(message);
            return ResponseEntity.status(HttpStatus.OK).body("Usuário desbloqueado");

        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esse usuário não existe");
    }


}
