package com.javaspringmvc.demo.controller;


import com.javaspringmvc.demo.FileUploadUtil;
import com.javaspringmvc.demo.model.*;
import com.javaspringmvc.demo.repository.*;
import com.javaspringmvc.demo.service.BlogService;
import com.javaspringmvc.demo.service.ProductService;
import com.javaspringmvc.demo.service.SendEmailService;
import com.javaspringmvc.demo.service.UserService;
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
    @GetMapping("/admin/dashboard")
    public String dash(Model model){
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
        return "index";
    }
    @GetMapping("/admin/category")
    public String showCategory(Model model){
//        Iterable<Category> categories = categoryRepository.findAll();
//        model.addAttribute("categories", categories);
//        model.addAttribute("category", new Category());
        return findPaginateAndSortingCategory(0,"id","asc", model);
    }

    @GetMapping("/admin/category/add")
    public String addCategory(Model model){
        Category category = new Category();
        model.addAttribute("category", category);
        return "categoryAdd";
    }

   @PostMapping("/admin/category/post")
    public String postCategory(@ModelAttribute("category") Category category){
        categoryRepository.save(category);
        return "redirect:/admin/category";
   }

   @GetMapping("/admin/category/delete/{id}")
   public String deleteCategory(@PathVariable int id){
        categoryRepository.deleteById(id);
        return "redirect:/admin/category";
   }

   @GetMapping("/admin/category/edit/{id}")
   public String editCategory(@PathVariable int id, Model model){
       Optional<Category> category = categoryRepository.findById(id);
       if(category.isPresent()){
           model.addAttribute("category", category.get());
           return "categoryEdit";
       }else {
           return "404";
       }

   }

   @GetMapping("/admin/product")
    public String showProduct(Model model){
        List<Product> listProduct = productService.getAllProduct();
        model.addAttribute("listProduct", listProduct);
        return "product";


//       return findPaginateAndSorting(0, "id", "asc", model);

   }

    @GetMapping("/admin/product/add")
    public String addProduct(Model model){
        Product product = new Product();
        Iterable<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("product", product);
        return "productAdd";
    }



    @PostMapping("/admin/product/post")
    public String postProduct(@ModelAttribute("product") Product product, @RequestParam("file") MultipartFile multipartFile) throws IOException {

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            product.setImage(fileName);
            Product saveProduct = productService.saveProduct(product);
            String uploadDir = "product_photos/" + saveProduct.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);



        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable(value = "id") long id){
        productService.deleteProductById(id);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/edit/{id}")
    public String editProduct(@PathVariable(value = "id") long id, Model model){
        Optional<Product> optional = productRepository.findById(id);
        if(optional.isPresent()){
            Iterable<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("product", optional.get());
            return "productEdit";
        }else {
           return "404";
        }


    }

    @GetMapping("/admin/blog")
    public String listBlog(Model model){
        List<Blog> listBlog = blogService.getAllBlog();
        model.addAttribute("listBlog", listBlog);
        return "blog";
//        return findPaginateAndSortingBlog(0,"id","asc", model);
    }

    @GetMapping("/admin/blog/add")
    public String addBlog(Model model){
        Blog blog = new Blog();
        model.addAttribute("blog", blog);
        return "blogAdd";
    }

    @PostMapping("/admin/blog/post")
    public String postBlog(@ModelAttribute("blog") Blog blog, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        blog.setBlog_image(fileName);
        Blog saveBlog = blogService.saveBlog(blog);
        String uploadDir = "blog_photos/" + saveBlog.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        return "redirect:/admin/blog";
    }

    @GetMapping("/admin/blog/edit/{id}")
    public String editBlog(@PathVariable(value = "id") long id, Model model){
        Optional<Blog> optional = blogRepository.findById(id);
        if(optional.isPresent()){
            model.addAttribute("blog", optional.get());
            return "blogEdit";
        }else {
            return "404";
        }

    }

    @GetMapping("/admin/blog/delete/{id}")
    public String deleteBlog(@PathVariable(value = "id") long id){
        blogService.deleteBlogById(id);
        return "redirect:/admin/blog";
    }

    @GetMapping("/admin/order")
    public String showOrder(Model model){
        List<Order> listOrder = orderRepository.findAll();
        model.addAttribute("listOrder", listOrder);
        return "order";
//        return findPaginateAndSortingOrder(0,"id","asc", model);
    }

    @GetMapping("/admin/order/confirm/{id}")
    public String confirmOrder(@PathVariable(value = "id") long id, Model model){
        Optional<Order> optional = orderRepository.findById(id);
        if(optional.isPresent()){
            Order currentOrder = optional.get();
            currentOrder.setOrder_status("Paid");
            orderRepository.save(currentOrder);
            return "redirect:/admin/order";
        }else{
            return "404";
        }
    }

    @GetMapping("/admin/contact")
    public String showContact(Model model){
        List<SendEmail> sendEmails = sendEmailRepository.findAll();
        model.addAttribute("sendEmails", sendEmails);
        return "contact";
    }

    @GetMapping("/admin/contact/reply/{id}")
    public String replyContact(@PathVariable(value = "id") long id, Model model){
        Optional<SendEmail> optional = sendEmailRepository.findById(id);
        if(optional.isPresent()){
            model.addAttribute("contact", optional.get());
            return "contactReply";
        }else{
            return "404";
        }
    }

    @PostMapping("/admin/contact/send")
    public String sendContact(@RequestParam("email") String email, @RequestParam("name") String name, @RequestParam("reply") String reply, HttpSession session){
        sendEmailService.sendMail(email, name, reply);
        session.setAttribute("msg", "Email sent successfully");
        return "redirect:/admin/contact";
    }
    @GetMapping("/page/{pageNo}")
    public String findPaginateAndSorting(@PathVariable(value = "pageNo") int pageNo,
                                         @RequestParam("sortField") String sortField,
                                         @RequestParam("sortDir") String sortDir,
                                         Model m) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo, 5,sort);

        Page<Product> page = productRepository.findAll(pageable);

        List<Product> list = page.getContent();

        m.addAttribute("pageNo", pageNo);
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("totalPage", page.getTotalPages());
        m.addAttribute("all_products", list);

        m.addAttribute("sortField",sortField);
        m.addAttribute("sortDir",sortDir);
        m.addAttribute("revSortDir",sortDir.equals("asc") ? "desc" : "asc");


        return "product";
    }

    @GetMapping("/paginate/{pageNo}")
    public String findPaginateAndSortingCategory(@PathVariable(value = "pageNo") int pageNo,
                                         @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model m) {

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


        return "category";
    }

    @GetMapping("/paginateBlog/{pageNo}")
    public String findPaginateAndSortingBlog(@PathVariable(value = "pageNo") int pageNo,
                                                 @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model m) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo, 5,sort);

        Page<Blog> page = blogRepository.findAll(pageable);

        List<Blog> list = page.getContent();

        m.addAttribute("pageNo", pageNo);
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("totalPage", page.getTotalPages());
        m.addAttribute("listBlog", list);

        m.addAttribute("sortField",sortField);
        m.addAttribute("sortDir",sortDir);
        m.addAttribute("revSortDir",sortDir.equals("asc") ? "desc" : "asc");


        return "blog";
    }

    @GetMapping("/paginateOrder/{pageNo}")
    public String findPaginateAndSortingOrder(@PathVariable(value = "pageNo") int pageNo,
                                             @RequestParam("sortField") String sortField, @RequestParam("sortDir") String sortDir, Model m) {

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


        return "order";
    }

}
