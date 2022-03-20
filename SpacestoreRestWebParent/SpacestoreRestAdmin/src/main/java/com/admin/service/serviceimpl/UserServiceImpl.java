package com.admin.service.serviceimpl;

import com.admin.repository.RoleRepository;
import com.admin.repository.UserRepository;
import com.admin.service.UserService;
import com.spacestore.common.dto.UserDto;
import com.spacestore.common.entity.Role;
import com.spacestore.common.entity.User;
import com.spacestore.common.exception.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream().map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUsersByPage(Integer pageNumber, Integer pageSize) {
        Pageable pages = PageRequest.of(pageNumber, pageSize);
        return userRepository.findAll(pages)
                .getContent()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
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
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public User registerUser(UserDto user) {
        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRoles(user.getRoles());
        //Role role = roleRepository.findById(1).get();
        //Set<Role> roles = new HashSet<>();
        //roles.add(role);
        //user.setRoles(roles);
        newUser.setPassword(getEncodedPassword(user.getPassword()));
        return userRepository.save(newUser);
    }

    private String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public User updateUser(UserDto userDto) {
        return userRepository.save(modelMapper.map(userDto, User.class));
    }
}
