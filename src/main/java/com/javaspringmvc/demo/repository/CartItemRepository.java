package com.javaspringmvc.demo.repository;

import com.javaspringmvc.demo.model.CartItem;
import com.javaspringmvc.demo.model.Product;
import com.javaspringmvc.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    public List<CartItem> findByUser(User user);

    public CartItem findByUserId(User user);

    public CartItem findByUserAndProduct(User user, Product product);
    @Query("UPDATE CartItem c SET c.quantity = ?1 WHERE c.product.id = ?2 AND c.user.id = ?3")
    @Modifying
    public void updateQuantity(Integer quantity, Long productId, Integer userId);
}
