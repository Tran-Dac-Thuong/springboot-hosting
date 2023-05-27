package com.javaspringmvc.demo.repository;

import com.javaspringmvc.demo.model.Blog;
import com.javaspringmvc.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    @Query("SELECT blog FROM Blog blog WHERE CONCAT(blog.id, ' ', blog.blog_title) LIKE %?1%")
    public List<Blog> searchBlog(String keyword);
}
