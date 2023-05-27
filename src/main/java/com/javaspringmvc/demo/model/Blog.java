package com.javaspringmvc.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String blog_title;
    private String blog_content;
    @Column(nullable = true, length = 250)
    private String blog_image;

    @Transient
    public String getPhotosImagePath(){
        if(blog_image == null){
            return null;
        }
        return "/blog_photos/" + id + "/" + blog_image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBlog_title() {
        return blog_title;
    }

    public void setBlog_title(String blog_title) {
        this.blog_title = blog_title;
    }

    public String getBlog_content() {
        return blog_content;
    }

    public void setBlog_content(String blog_content) {
        this.blog_content = blog_content;
    }

    public String getBlog_image() {
        return blog_image;
    }

    public void setBlog_image(String blog_image) {
        this.blog_image = blog_image;
    }
}
