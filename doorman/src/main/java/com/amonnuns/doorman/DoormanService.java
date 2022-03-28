package com.amonnuns.doorman;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class DoormanService {

    private final UserRepository userRepository;

    public DoormanService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User cadastrarUsuário(UserDto userDto){

        User user = new User();

        Example<User> example = Example.of(user);
        if(!userRepository.exists(example)){
            BeanUtils.copyProperties(userDto, user);
            user.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
            user.setBlocked(false);

            userRepository.save(user);
            return user;
        }
        return  user;
    }

    public boolean verificaPermissao(UserLoginForm loginForm){

        User user =  userRepository.findByUserNameAndPassword(
                loginForm.getUsername(),
                loginForm.getPassword());

        if(user != null ){
            return !user.getBlocked();
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
