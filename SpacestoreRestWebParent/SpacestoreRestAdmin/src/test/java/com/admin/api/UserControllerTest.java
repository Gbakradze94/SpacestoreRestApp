package com.admin.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class UserControllerTest {
    @Test
    void hello(){
        UserController controller = new UserController();  // Arrange
        String response = controller.greet(); // Act

        assertEquals("Hello",response); // Assert

    }
}