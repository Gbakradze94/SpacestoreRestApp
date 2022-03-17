package com.admin.security;

import com.admin.repository.UserRepository;
import com.spacestore.common.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SpacestoreUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDto userDto = userRepository.getUserByEmail(email);
        if(userDto != null){
            return new SpacestoreUserDetails(userDto);
        }
        throw new UsernameNotFoundException("Could not find user with email: " + email);

    }


}
