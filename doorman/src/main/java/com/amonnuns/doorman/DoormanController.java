package com.amonnuns.doorman;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/doorman")
public class DoormanController {

    private final DoormanService doormanService;

    public DoormanController(DoormanService doormanService) {
        this.doormanService = doormanService;
    }


    @PostMapping("/subscribe")
    public ResponseEntity<Object> cadastraUsuario(@RequestBody User user){

        Optional<User> userReturn = doormanService.cadastrarUsuário(user);
        if(userReturn.isEmpty()){
            return ResponseEntity.status(HttpStatus.CREATED).body("User already created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userReturn.get());


    }

    @PostMapping("/permission")
    public boolean verificaPermissao(@RequestBody UserLoginForm loginForm){

        return doormanService.verificaPermissao(loginForm);
    }

    @PostMapping("/block/{username}")
    public ResponseEntity<Object> bloqueiaUsuario(@PathVariable(value = "username") String username){

        if(doormanService.bloqueiaUsuario(username)){
            return ResponseEntity.status(HttpStatus.OK).body("Usuário bloqueado");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esse usuário não existe");
    }
    @PostMapping("/unblock/{username}")
    public ResponseEntity<Object> desbloqueiaUsuario(@PathVariable(value = "username") String username){

        if(doormanService.desbloqueiaUsuario(username)){
            return ResponseEntity.status(HttpStatus.OK).body("Usuário desbloqueado");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esse usuário não existe");
    }


}
