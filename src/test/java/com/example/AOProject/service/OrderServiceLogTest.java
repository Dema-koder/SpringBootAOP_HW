package com.example.AOProject.service;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.AOProject.aspect.LoggingAspect;
import com.example.AOProject.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderServiceLogTest {

    @Autowired
    private OrderService orderService;

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        Logger logger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @Test
    @DisplayName("Check logs around createOrder and deleteOrder")
    void testLoggingCreateDeleteOrder() {
        Long userId = 1L;
        Order order = new Order();
        order.setDescription("New Order");
        order.setStatus("Pending");

        System.out.println(order.toString());
        Order createdOrder = orderService.createOrder(userId, order);
        orderService.deleteOrder(createdOrder.getId());

        System.out.println(createdOrder);
        assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .anyMatch(message ->
                        message.contains("Выполнение метода createOrder с аргументами [1, {null New Order Pending}]"))
                .anyMatch(message ->
                        message.contains("Метод createOrder выполнился за ") &&
                                message.contains("с результатом " + createdOrder))
                .anyMatch(message ->
                        message.contains("Выполнение метода deleteOrder с аргументами [" + createdOrder.getId() + "]"))
                .anyMatch(message ->
                        message.contains("Метод deleteOrder выполнился за ") &&
                                message.contains("с результатом null"));
    }

    @Test
    @DisplayName("Check logs around getOrdersByUserId")
    void testLoggingGetOrdersByUserId() {
        Long userId = 1L;
        List<Order> orders = orderService.getOrdersByUserId(userId);

        assertThat(listAppender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .anyMatch(message ->
                        message.contains("Выполнение метода getOrdersByUserId с аргументами [1]"))
                .anyMatch(message ->
                        message.contains("Метод getOrdersByUserId выполнился за") &&
                                message.contains("с результатом " + orders));
    }
}