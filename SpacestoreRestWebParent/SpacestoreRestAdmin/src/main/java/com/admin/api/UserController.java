package com.admin.api;

import com.admin.service.SettingService;
import com.admin.service.UserService;
import com.admin.util.settings.EmailSettingBag;
import com.admin.util.settings.SettingsUtil;
import com.spacestore.common.dto.UserDto;
import com.spacestore.common.entity.User;
import com.spacestore.common.exception.UserNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
@Api(tags = {"Authentication"})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SettingService settingService;

    @ApiOperation(value = "Gets all users from database.")
    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDto>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }


    @GetMapping("/usersbypage")
    public ResponseEntity<List<UserDto>> getUsersByPage(@RequestParam Integer pageNumber,
                                                        @RequestParam Integer pageSize) {
        return new ResponseEntity<>(userService.getUsersByPage(pageNumber, pageSize), HttpStatus.OK);
    }


    @GetMapping("/users/{id}")
    public ResponseEntity<Optional<User>> getUser(@PathVariable("id") Integer id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }


    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.saveUser(userDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Updates user details")
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody UserDto userDto) {
        userDto.setId(id);
        return new ResponseEntity<>(userService.updateUser(userDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Deletes the user from database by id.")
    @DeleteMapping("/users")
    public ResponseEntity<HttpStatus> deleteUser(@RequestParam("id") Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/users/filterByEmail")
    public ResponseEntity<List<UserDto>> getUsersByEmail(@RequestParam String email) {
        return new ResponseEntity<>(userService.findUsersByEmail(email), HttpStatus.OK);
    }


    @PostMapping("/users/register")
    public ResponseEntity<HttpStatus> registerUser(@RequestBody UserDto user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        userService.registerUser(user);
        userService.sendVerificationEmail(user, request);
        return new ResponseEntity<>(HttpStatus.CREATED);
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

    // For test demo
    @GetMapping("/hello")
    public String greet() {
        return "Hello";
    }
}
