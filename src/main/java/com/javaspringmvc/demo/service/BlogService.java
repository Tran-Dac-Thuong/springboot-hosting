package com.javaspringmvc.demo.service;

import com.javaspringmvc.demo.model.Blog;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BlogService {
    Blog saveBlog(Blog blog);

    Blog getBlogById(long id);
    void deleteBlogById(long id);
    List<Blog> getAllBlog();

    List<Blog> getSearchBlog(String keyword);
}
