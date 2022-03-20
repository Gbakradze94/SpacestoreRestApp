package com.admin;


import com.admin.repository.UserRepository;
import com.spacestore.common.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
