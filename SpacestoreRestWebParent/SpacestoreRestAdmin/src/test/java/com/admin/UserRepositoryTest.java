package com.admin;


import com.admin.repository.UserRepository;
import com.spacestore.common.entity.Role;
import com.spacestore.common.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(true)
class UserRepositoryTest {
    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testCreateUser(){
        Role adminRole = entityManager.find(Role.class,1);
        User user = new User("test@gmail.com","password","John","Doe");
        user.addRole(adminRole);

        User createdUser = repository.save(user);
        assertThat(createdUser.getId()).isNotNull();
    }

    @Test
    void testCreateUserWithTwoRoles(){
        User user = new User("user@tworoles.com","password","Firstname","Lastname");

        Role editor = new Role(3);
        Role shipper = new Role(4);

        user.addRole(editor);
        user.addRole(shipper);

        User savedUser = repository.save(user);
        assertThat(savedUser.getId()).isPositive();
    }

    @Test
    void testGetAllUsers(){
        Iterable<User> userList = repository.findAll();
        userList.forEach(System.out::println);
    }

    @Test
    void testGetUserById(){
        User user = repository.findById(1).get();

        log.info("User found by Id: " + user);

        assertThat(user).isNotNull();
    }

    @Test
    void testDeleteUser(){
        Integer userId = 37;
        repository.deleteById(userId);
    }
}
