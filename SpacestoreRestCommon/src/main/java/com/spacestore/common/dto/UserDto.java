package com.spacestore.common.dto;

import com.spacestore.common.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Integer id;

    @Email(message = "Please, enter valid email address")
    private String email;
    private String password;
    private String firstName;

    private String lastName;

    private String verificationCode;
    private String photos;

    private boolean enabled;

    private Set<Role> roles = new HashSet<>();
}
