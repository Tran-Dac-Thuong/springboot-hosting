package com.javaspringmvc.demo.service;


import com.javaspringmvc.demo.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;
public interface ProductService {
    Product saveProduct(Product product);
    Product getProductById(long id);
    void deleteProductById(long id);
    List<Product> getAllProduct();

    List<Product> getSearchProduct(String keyword);

    List<Product> getProductByPrice(Double min, Double max);


    Page<Product> findPaginated(int pageNo, int pageSize, String sortField, String sortDir);
}
