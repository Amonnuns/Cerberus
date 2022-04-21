package com.amonnuns.doorman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class DoormanService {

    private final UserRepository userRepository;

    @Autowired
    public DoormanService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Optional<User> cadastrarUsuário(User user){

        Example<User> example = Example.of(user);

        if(userRepository.exists(example)){
            return Optional.empty();
        }
        user.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        user.setBlocked(false);

        userRepository.save(user);

        return Optional.of(user);
    }


    public boolean verificaPermissao(UserLoginForm loginForm){

        Optional<User> user =  userRepository.findByUserNameAndPassword(
                loginForm.getUsername(),
                loginForm.getPassword());

        if(user.isPresent()){
           User recoveredUser = user.get();
            return !recoveredUser.getBlocked();
        }

        return false;
    }

    public boolean bloqueiaUsuario(String username){

        User user = userRepository.findByUserName(username);

        if( user != null){
            user.setBlocked(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean desbloqueiaUsuario(String username) {

        User user = userRepository.findByUserName(username);

        if( user != null){
            user.setBlocked(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
