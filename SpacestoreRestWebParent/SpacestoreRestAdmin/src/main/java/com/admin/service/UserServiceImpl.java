package com.admin.service;

import com.admin.repository.UserRepository;
import com.spacestore.common.dto.UserDto;
import com.spacestore.common.entity.User;
import com.spacestore.common.exception.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream().map(user -> modelMapper.map(user,UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUsersByPage(Integer pageNumber, Integer pageSize) {
        Pageable pages = PageRequest.of(pageNumber,pageSize);
        return userRepository.findAll(pages)
                .getContent()
                .stream()
                .map(user -> modelMapper.map(user,UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> getUser(Integer id) throws UserNotFoundException {
        return Optional.ofNullable(userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("The User with the ID: %s was not found", id)
                )));
    }

    @Override
    public User saveUser(UserDto userDto) {
        return userRepository.save((modelMapper.map(userDto, User.class)));
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> findUsersByEmail(String email) {
        return userRepository.findByEmail(email)
                .stream()
                .map(user -> modelMapper.map(user,UserDto.class))
                .collect(Collectors.toList());
    }

    public User updateUser(UserDto userDto) {
        return userRepository.save(modelMapper.map(userDto,User.class));
    }
}
