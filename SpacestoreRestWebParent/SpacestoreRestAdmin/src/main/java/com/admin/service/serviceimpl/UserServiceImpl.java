package com.admin.service.serviceimpl;

import com.admin.repository.RoleRepository;
import com.admin.repository.UserRepository;
import com.admin.service.SettingService;
import com.admin.service.UserService;
import com.admin.util.settings.EmailSettingBag;
import com.admin.util.settings.SettingsUtil;
import com.spacestore.common.dto.UserDto;
import com.spacestore.common.entity.Role;
import com.spacestore.common.entity.User;
import com.spacestore.common.exception.UserNotFoundException;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private SettingService settingService;


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

        String verificationCode = RandomString.make(64);

        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRoles(user.getRoles());
        newUser.setVerificationCode(verificationCode);
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

    public void sendVerificationEmail(UserDto userDto, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = SettingsUtil.prepareMailSender(emailSettings);

        User user = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User with the email could not be found."));

        String toAddress = user.getEmail();
        String subject = emailSettings.getUserVerifySubject();
        String content = emailSettings.getUserVerifyContent();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName());

        String verifyURL = SettingsUtil.getSiteURL(request) + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }
}
