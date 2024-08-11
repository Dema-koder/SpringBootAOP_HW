package com.example.AOProject.service;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.AOProject.aspect.LoggingAspect;
import com.example.AOProject.model.User;
import com.example.AOProject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
    void testAroundAdviceLogging() throws Throwable {
        User user = new User();
        user.setName("Ivan");
        user.setEmail("ivan@gmail.com");
        userService.createUser(user);

        assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .anyMatch(message ->
                        message.contains("Выполнение метода createUser с аргуменетами [{null Ivan ivan@gmail.com}]"))
                .anyMatch(message ->
                        message.contains("Метод createUser выполнился за") &&
                                message.contains("с результатом {") && message.contains("Ivan ivan@gmail.com}"));
    }
}