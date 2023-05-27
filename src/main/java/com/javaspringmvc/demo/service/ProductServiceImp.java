package com.javaspringmvc.demo.service;

import com.javaspringmvc.demo.model.Product;
import com.javaspringmvc.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(long id) {

        return productRepository.findById(id).get();
    }

    @Override
    public void deleteProductById(long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getAllProduct() {

            return productRepository.findAll();


    }

    @Override
    public List<Product> getSearchProduct(String keyword) {
        if(keyword != null){
            return productRepository.search(keyword);
        }else {
            return productRepository.findAll();
        }

    }

    @Override
    public List<Product> getProductByPrice(Double from, Double to) {
        if(from != null || to != null){
            return productRepository.findByPrice(from, to);
        }else{
            return productRepository.findAll();
        }
    }


    @Override
    public Page<Product> findPaginated(int pageNo, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return productRepository.findAll(pageable);
    }
}
