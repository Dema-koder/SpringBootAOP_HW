package com.example.AOProject.service;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.AOProject.aspect.LoggingAspect;
import com.example.AOProject.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceLogTest {
    @Autowired
    private UserService userService;

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        Logger logger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @Test
    @DisplayName("Check logs around createUser and deleteUser")
    void testLoggingCreateDeleteUer() {
        User user = new User();
        user.setName("Ivan");
        user.setEmail("ivan@gmail.com");
        var newUser = userService.createUser(user);
        userService.deleteUser(newUser.getId());

        assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .anyMatch(message ->
                        message.contains("Выполнение метода createUser с аргументами [{null Ivan ivan@gmail.com}]"))
                .anyMatch(message ->
                        message.contains("Метод createUser выполнился за") &&
                                message.contains("с результатом {" + newUser.getId() + " Ivan ivan@gmail.com}"))
                .anyMatch(message ->
                    message.contains("Выполнение метода deleteUser с аргументами [" + newUser.getId() + "]"))
                .anyMatch(message ->
                        message.contains("Метод deleteUser выполнился за") &&
                                message.contains("с результатом null"));
    }

    @Test
    @DisplayName("Check logs around getAllUsers")
    void testLoggingGetAllUsers() {
        var list = userService.getAllUsers();

        assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .anyMatch(message ->
                        message.contains("Выполнение метода getAllUsers с аргументами []"))
                .anyMatch(message ->
                        message.contains("Метод getAllUsers выполнился за") &&
                            message.contains("с результатом " + list.toString()));
    }

    @Test
    @DisplayName("Check logs around findUserByName")
    void testLoggingFindUserByName() {
        String name = "Demyan";
        User user = userService.findUserByName(name);

        assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .anyMatch(message ->
                        message.contains("Выполнение метода findUserByName с аргументами [Demyan]"))
                .anyMatch(message ->
                        message.contains("Метод findUserByName выполнился за") &&
                                message.contains("с результатом " + user.toString()));
    }

    @Test
    @DisplayName("Check logs around findUserById")
    void testLoggingFindUserById() {
        Long id = 1L;
        User user = userService.findUserById(id);

        assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .anyMatch(message ->
                        message.contains("Выполнение метода findUserById с аргументами [1]"))
                .anyMatch(message ->
                        message.contains("Метод findUserById выполнился за") &&
                                message.contains("с результатом " + user.toString()));
    }

    @Test
    @DisplayName("Check logs around updateUser")
    void testLoggingUpdateUser() {
        User user = new User();
        user.setName("Ivan");
        user.setEmail("ivan@gmail.com");
        var newUser = userService.createUser(user);
        Long id = newUser.getId();
        User userDetails = new User();
        userDetails.setName("NewName");
        userDetails.setEmail("newemail@gmail.com");

        User updatedUser = userService.updateUser(id, userDetails);
        userService.deleteUser(id);

        assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .anyMatch(message ->
                        message.contains("Выполнение метода updateUser с аргументами [" + id.toString() + ", {null NewName newemail@gmail.com}]"))
                .anyMatch(message ->
                        message.contains("Метод updateUser выполнился за") &&
                                message.contains("с результатом " + updatedUser.toString()));
    }

}