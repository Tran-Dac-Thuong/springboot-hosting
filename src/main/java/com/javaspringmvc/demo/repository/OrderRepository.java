package com.javaspringmvc.demo.repository;

import com.javaspringmvc.demo.model.Order;
import com.javaspringmvc.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    public List<Order> findByUser(User user);

    @Query("SELECT order FROM Order order WHERE order.order_status = 'Processing'")
    public List<Order> findByProcess(String process);
}
