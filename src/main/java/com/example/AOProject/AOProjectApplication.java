package com.example.AOProject;

import com.example.AOProject.model.Order;
import com.example.AOProject.model.User;
import com.example.AOProject.service.OrderService;
import com.example.AOProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;

@SpringBootApplication
public class AOProjectApplication {

	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;

	public static void main(String[] args) {
		SpringApplication.run(AOProjectApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void applicationReadyAction() {
		User user = new User();
		user.setName("Demyan");
		user.setEmail("example@gmail.com");
		User newUser = userService.createUser(user);

		Order order = new Order();
		order.setDescription("DESCRIPTION");
		order.setStatus("STATUS");
		Order newOrder = orderService.createOrder(newUser.getId(), order);
	}
}
