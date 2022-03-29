package com.admin.service;

import com.spacestore.common.dto.UserDto;
import com.spacestore.common.entity.User;
import com.spacestore.common.exception.UserNotFoundException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> getAllUsers();

    List<UserDto> getUsersByPage(Integer pageNumber, Integer pageSize);

    Optional<User> getUser(Integer id) throws UserNotFoundException;

    User saveUser(UserDto userDto);

    User updateUser(UserDto userDto);

    void deleteUser(Integer id);

    List<UserDto> findUsersByEmail(String email);

    User registerUser(UserDto user);

    void sendVerificationEmail(UserDto userDto, HttpServletRequest request) throws MessagingException,
            UnsupportedEncodingException;
}
