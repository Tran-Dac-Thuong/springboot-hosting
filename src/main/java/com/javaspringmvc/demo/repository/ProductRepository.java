package com.javaspringmvc.demo.repository;

import com.javaspringmvc.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT product FROM Product product WHERE CONCAT(product.id, ' ', product.product_name, ' ', product.category_name) LIKE %?1%")
    public List<Product> search(String keyword);

    @Query("SELECT product FROM Product product WHERE product.price BETWEEN :from AND :to")
    public List<Product> findByPrice(@Param("from") Double from, @Param("to") Double to);
}
