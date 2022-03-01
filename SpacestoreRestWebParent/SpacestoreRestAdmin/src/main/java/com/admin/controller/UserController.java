package com.admin.controller;

import com.admin.service.UserServiceImpl;
import com.spacestore.common.dto.UserDto;
import com.spacestore.common.entity.User;
import com.spacestore.common.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers(){
        return new ResponseEntity<>(userServiceImpl.getAllUsers(),HttpStatus.OK);
    }


    @GetMapping("/usersbypage")
    public ResponseEntity<List<UserDto>> getUsersByPage(@RequestParam Integer pageNumber,
                                               @RequestParam Integer pageSize){
        return new ResponseEntity<>(userServiceImpl.getUsersByPage(pageNumber,pageSize),HttpStatus.OK);
    }


    @GetMapping("/users/{id}")
    public ResponseEntity<Optional<User>> getUser(@PathVariable("id") Integer id) throws UserNotFoundException {
        return new ResponseEntity<>(userServiceImpl.getUser(id),HttpStatus.OK);
    }


    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userServiceImpl.saveUser(userDto),HttpStatus.CREATED);
    }


    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody UserDto userDto){
        userDto.setId(id);
        return new ResponseEntity<>(userServiceImpl.updateUser(userDto),HttpStatus.CREATED);
    }


    @DeleteMapping("/users")
    public ResponseEntity<HttpStatus> deleteUser(@RequestParam("id") Integer id) {
        userServiceImpl.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/users/filterByEmail")
    public ResponseEntity<List<UserDto>> getUsersByEmail(@RequestParam String email){
        return new ResponseEntity<>(userServiceImpl.findUsersByEmail(email),HttpStatus.OK);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleMethodArgumentNoValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String message = error.getDefaultMessage();
            String fieldName = ((FieldError) error).getField();
            errors.put(fieldName, message);
        });
        return errors;
    }
}
