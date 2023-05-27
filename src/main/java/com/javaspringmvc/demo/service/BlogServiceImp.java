package com.javaspringmvc.demo.service;

import com.javaspringmvc.demo.model.Blog;
import com.javaspringmvc.demo.model.Product;
import com.javaspringmvc.demo.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogServiceImp implements BlogService{
    @Autowired
    private BlogRepository blogRepository;
    @Override
    public Blog saveBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public Blog getBlogById(long id) {
        Optional<Blog> optional = blogRepository.findById(id);
        Blog blog = null;
        if(optional.isPresent()){
            blog = optional.get();
        }else {
            throw new RuntimeException("Blog not found for id: " + id);

        }
        return blog;
    }

    @Override
    public void deleteBlogById(long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public List<Blog> getAllBlog() {
        return blogRepository.findAll();
    }

    @Override
    public List<Blog> getSearchBlog(String keyword) {
        if(keyword != null){
            return blogRepository.searchBlog(keyword);
        }else {
            return blogRepository.findAll();
        }
    }


}
