package com.admin;


import com.admin.repository.UserRepository;
import com.admin.service.UserServiceImpl;
import com.spacestore.common.dto.UserDto;
import com.spacestore.common.entity.Role;
import com.spacestore.common.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class MainController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

//    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public User register(@RequestBody UserDto user) {
        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRoles(user.getRoles());
        return userRepository.save(newUser);

    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> login(@RequestBody UserDto user) throws Exception {
        Authentication authentication;
        try {
            authentication =  authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),
                            user.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch(BadCredentialsException e) {
            throw new Exception("Invalid Credential");
        }
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

}
