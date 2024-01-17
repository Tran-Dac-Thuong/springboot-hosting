package com.javaspringmvc.demo.controller;


import com.javaspringmvc.demo.FileUploadUtil;
import com.javaspringmvc.demo.model.*;
import com.javaspringmvc.demo.repository.*;
import com.javaspringmvc.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SendEmailRepository sendEmailRepository;
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;
    @GetMapping("/admin/dashboard")
    public String dash(Model model, HttpServletRequest request){
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            List<User> users = userRepository.findAll();
            List<Order> orders = orderRepository.findAll();
            Double total_price = 0.0;
            for (Order order : orders){
                total_price = total_price + order.getTotal_cost();
                model.addAttribute("total_price", total_price);
            }
            for (Order orderProcessing : orders){
                String process = String.valueOf(orderProcessing.getOrder_status().equals("Processing"));
                List<Order> orderProcess = orderRepository.findByProcess(process);
                model.addAttribute("process", orderProcess);
            }
            model.addAttribute("orders", orders);
            model.addAttribute("users", users);
            model.addAttribute("currentUser", currentUser);
            return "index";
        }else {
            return "redirect:/sign-in";
        }

    }
    @GetMapping("/admin/category")
    public String showCategory(Model model, HttpServletRequest request){
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            model.addAttribute("currentUser", currentUser);
            return findPaginateAndSortingCategory(0,"id","asc", model, request);

        }else {
            return "redirect:/sign-in";
        }


    }

    @GetMapping("/admin/category/add")
    public String addCategory(Model model, HttpServletRequest request){
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            Category category = new Category();
            model.addAttribute("category", category);
            model.addAttribute("currentUser", currentUser);
            return "categoryAdd";
        }else {
            return "redirect:/sign-in";
        }

    }

   @PostMapping("/admin/category/post")
    public String postCategory(@ModelAttribute("category") Category category, HttpServletRequest request){
       User currentUser = authService.isAuthenticatedUser(request);
       if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
           return "error/403";
       } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
           categoryRepository.save(category);
           return "redirect:/admin/category";
       }else {
           return "redirect:/sign-in";
       }

   }

   @GetMapping("/admin/category/delete/{id}")
   public String deleteCategory(@PathVariable int id, Model model, HttpServletRequest request){
       User currentUser = authService.isAuthenticatedUser(request);
       if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
           return "error/403";
       } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
           model.addAttribute("currentUser", currentUser);
           categoryRepository.deleteById(id);
           return "redirect:/admin/category";
       }else {
           return "redirect:/sign-in";
       }

   }

   @GetMapping("/admin/category/edit/{id}")
   public String editCategory(@PathVariable int id, Model model, HttpServletRequest request){
       User currentUser = authService.isAuthenticatedUser(request);
       if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
           return "error/403";
       } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
           Optional<Category> category = categoryRepository.findById(id);
           if(category.isPresent()){
               model.addAttribute("currentUser", currentUser);
               model.addAttribute("category", category.get());
               return "categoryEdit";
           }else {
               return "404";
           }
       }else {
           return "redirect:/sign-in";
       }


   }

   @GetMapping("/admin/product")
    public String showProduct(Model model, HttpServletRequest request){
       User currentUser = authService.isAuthenticatedUser(request);
       if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
           return "error/403";
       } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
           List<Product> listProduct = productService.getAllProduct();
           model.addAttribute("listProduct", listProduct);
           model.addAttribute("currentUser", currentUser);
           return "product";
       }else {
           return "redirect:/sign-in";
       }




   }

    @GetMapping("/admin/product/add")
    public String addProduct(Model model, HttpServletRequest request){
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            Product product = new Product();
            Iterable<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("product", product);
            model.addAttribute("currentUser", currentUser);
            return "productAdd";
        }else {
            return "redirect:/sign-in";
        }

    }



    @PostMapping("/admin/product/post")
    public String postProduct(@ModelAttribute("product") Product product, @RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            product.setImage(fileName);
            Product saveProduct = productService.saveProduct(product);
            String uploadDir = "product_photos/" + saveProduct.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            return "redirect:/admin/product";
        }else {
            return "redirect:/sign-in";
        }

    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable(value = "id") long id, Model model, HttpServletRequest request){
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            productService.deleteProductById(id);
            model.addAttribute("currentUser", currentUser);
            return "redirect:/admin/product";
        }else {
            return "redirect:/sign-in";
        }

    }

    @GetMapping("/admin/product/edit/{id}")
    public String editProduct(@PathVariable(value = "id") long id, Model model, HttpServletRequest request){
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            Optional<Product> optional = productRepository.findById(id);
            if(optional.isPresent()){
                Iterable<Category> categories = categoryRepository.findAll();
                model.addAttribute("categories", categories);
                model.addAttribute("product", optional.get());
                model.addAttribute("currentUser", currentUser);
                return "productEdit";
            }else {
                return "404";
            }
        }else {
            return "redirect:/sign-in";
        }



    }

    @GetMapping("/admin/blog")
    public String listBlog(Model model, HttpServletRequest request){
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            List<Blog> listBlog = blogService.getAllBlog();
            model.addAttribute("listBlog", listBlog);
            model.addAttribute("currentUser", currentUser);
            return "blog";
        }else {
            return "redirect:/sign-in";
        }

    }

    @GetMapping("/admin/blog/add")
    public String addBlog(Model model, HttpServletRequest request){
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            Blog blog = new Blog();
            model.addAttribute("blog", blog);
            model.addAttribute("currentUser", currentUser);
            return "blogAdd";
        }else {
            return "redirect:/sign-in";
        }
    }

    @PostMapping("/admin/blog/post")
    public String postBlog(@ModelAttribute("blog") Blog blog, @RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            blog.setBlog_image(fileName);
            Blog saveBlog = blogService.saveBlog(blog);
            String uploadDir = "blog_photos/" + saveBlog.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            return "redirect:/admin/blog";
        }else {
            return "redirect:/sign-in";
        }

    }

    @GetMapping("/admin/blog/edit/{id}")
    public String editBlog(@PathVariable(value = "id") long id, Model model, HttpServletRequest request){
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            Optional<Blog> optional = blogRepository.findById(id);
            if(optional.isPresent()){
                model.addAttribute("blog", optional.get());
                model.addAttribute("currentUser", currentUser);
                return "blogEdit";
            }else {
                return "404";
            }
        }else {
            return "redirect:/sign-in";
        }


    }

    @GetMapping("/admin/blog/delete/{id}")
    public String deleteBlog(@PathVariable(value = "id") long id, Model model, HttpServletRequest request){
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            blogService.deleteBlogById(id);
            model.addAttribute("currentUser", currentUser);
            return "redirect:/admin/blog";
        }else {
            return "redirect:/sign-in";
        }

    }

    @GetMapping("/admin/order")
    public String showOrder(Model model, HttpServletRequest request){
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            List<Order> listOrder = orderRepository.findAll();
            model.addAttribute("listOrder", listOrder);
            model.addAttribute("currentUser", currentUser);
            return "order";
        }else {
            return "redirect:/sign-in";
        }

    }

    @GetMapping("/admin/order/confirm/{id}")
    public String confirmOrder(@PathVariable(value = "id") long id, Model model, HttpServletRequest request){
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            Optional<Order> optional = orderRepository.findById(id);
            if(optional.isPresent()){
                Order currentOrder = optional.get();
                currentOrder.setOrder_status("Paid");
                orderRepository.save(currentOrder);
                model.addAttribute("currentUser", currentUser);
                return "redirect:/admin/order";
            }else{
                return "404";
            }
        }else {
            return "redirect:/sign-in";
        }

    }

    @GetMapping("/admin/contact")
    public String showContact(Model model, HttpServletRequest request){
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            List<SendEmail> sendEmails = sendEmailRepository.findAll();
            model.addAttribute("sendEmails", sendEmails);
            model.addAttribute("currentUser", currentUser);
            return "contact";
        }else {
            return "redirect:/sign-in";
        }

    }

    @GetMapping("/admin/contact/reply/{id}")
    public String replyContact(@PathVariable(value = "id") long id, Model model, HttpServletRequest request){
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            Optional<SendEmail> optional = sendEmailRepository.findById(id);
            if(optional.isPresent()){
                model.addAttribute("contact", optional.get());
                model.addAttribute("currentUser", currentUser);
                return "contactReply";
            }else{
                return "404";
            }
        }else {
            return "redirect:/sign-in";
        }

    }

    @PostMapping("/admin/contact/send")
    public String sendContact(@RequestParam("email") String email, @RequestParam("name") String name, @RequestParam("reply") String reply, HttpSession session, HttpServletRequest request){
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            sendEmailService.sendMail(email, name, reply);
            session.setAttribute("msg", "Email sent successfully");
            return "redirect:/admin/contact";
        }else {
            return "redirect:/sign-in";
        }

    }


    @GetMapping("/paginate/{pageNo}")
    public String findPaginateAndSortingCategory(@PathVariable(value = "pageNo") int pageNo,
                                         @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model m, HttpServletRequest request) {
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                    : Sort.by(sortField).descending();

            Pageable pageable = PageRequest.of(pageNo, 5,sort);

            Page<Category> page = categoryRepository.findAll(pageable);

            List<Category> list = page.getContent();

            m.addAttribute("pageNo", pageNo);
            m.addAttribute("totalElements", page.getTotalElements());
            m.addAttribute("totalPage", page.getTotalPages());
            m.addAttribute("categories", list);

            m.addAttribute("sortField",sortField);
            m.addAttribute("sortDir",sortDir);
            m.addAttribute("revSortDir",sortDir.equals("asc") ? "desc" : "asc");
            m.addAttribute("currentUser", currentUser);

            return "category";
        }else {
            return "redirect:/sign-in";
        }

    }



    @GetMapping("/paginateOrder/{pageNo}")
    public String findPaginateAndSortingOrder(@PathVariable(value = "pageNo") int pageNo,
                                             @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model m, HttpServletRequest request) {
        User currentUser = authService.isAuthenticatedUser(request);
        if (currentUser != null && currentUser.getRole().equals("ROLE_USER")) {
            return "error/403";
        } else if (currentUser != null && currentUser.getRole().equals("ROLE_ADMIN")) {
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                    : Sort.by(sortField).descending();

            Pageable pageable = PageRequest.of(pageNo, 5,sort);

            Page<Order> page = orderRepository.findAll(pageable);

            List<Order> list = page.getContent();

            m.addAttribute("pageNo", pageNo);
            m.addAttribute("totalElements", page.getTotalElements());
            m.addAttribute("totalPage", page.getTotalPages());
            m.addAttribute("listOrder", list);

            m.addAttribute("sortField",sortField);
            m.addAttribute("sortDir",sortDir);
            m.addAttribute("revSortDir",sortDir.equals("asc") ? "desc" : "asc");
            m.addAttribute("currentUser", currentUser);

            return "order";
        }else {
            return "redirect:/sign-in";
        }
    }

}
