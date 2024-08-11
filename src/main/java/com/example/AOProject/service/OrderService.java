package com.example.AOProject.service;

import com.example.AOProject.model.Order;
import com.example.AOProject.model.User;
import com.example.AOProject.repository.OrderRepository;
import com.example.AOProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public Order createOrder(Long userId, Order order) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            order.setUser(user);
            return orderRepository.save(order);
        }
        return null;
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return userRepository.findById(userId).map(User::getOrders).orElse(null);
    }
}
