package com.javaspringmvc.demo.controller;

import com.javaspringmvc.demo.config.SecurityUtils;
import com.javaspringmvc.demo.model.*;
import com.javaspringmvc.demo.repository.*;
import com.javaspringmvc.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import java.util.Map;
import java.util.Optional;

@Controller
public class HomeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SendEmailRepository sendEmailRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;
    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private PaypalService service;

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";


    @GetMapping("/")
    public String index(Model model, Principal principal, HttpSession session) {

        if (principal != null) {

            session.setAttribute("username", principal.getName());

        } else {
            session.removeAttribute("username");
        }

        List<Product> products = productService.getAllProduct();
        model.addAttribute("products", products);
        User currentUser = userService.getCurrentUser();
        List<CartItem> cartItems = cartItemRepository.findByUser(currentUser);
        model.addAttribute("cartItems", cartItems);
        return "home/index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        User currentUser = userService.getCurrentUser();
        List<CartItem> cartItems = cartItemRepository.findByUser(currentUser);
        model.addAttribute("cartItems", cartItems);
        return "home/about";
    }

    @GetMapping("/blog")
    public String blog(Model model, @Param("keyword") String keyword) {
        List<Blog> blogs = blogService.getSearchBlog(keyword);
        model.addAttribute("blogs", blogs);
        User currentUser = userService.getCurrentUser();
        List<CartItem> cartItems = cartItemRepository.findByUser(currentUser);
        model.addAttribute("cartItems", cartItems);
        return "home/blog";

    }


    @GetMapping("/contact")
    public String contact(Model model) {
        SendEmail sendEmail = new SendEmail();
        User currentUser = userService.getCurrentUser();
        List<CartItem> cartItems = cartItemRepository.findByUser(currentUser);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("sendEmail", sendEmail);
        return "home/contact";
    }

    @PostMapping("/sendEmail")
    public String sendEmail(@ModelAttribute SendEmail email, HttpSession session) {
        sendEmailRepository.save(email);
//        sendEmailService.sendMail(email);
        session.setAttribute("msg", "Email sent successfully");
        return "redirect:/contact";
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam("email") String email, HttpSession session) {
        sendEmailService.sendSubscribe(email);
        session.setAttribute("msg", "You have successfully subscribe");
        return "redirect:/";
    }

    @PostMapping("/about/subscribe")
    public String aboutSubscribe(@RequestParam("email") String email, HttpSession session) {
        sendEmailService.sendSubscribe(email);
        session.setAttribute("msg", "You have successfully subscribe");
        return "redirect:/about";
    }

    @PostMapping("/product/subscribe")
    public String productSubscribe(@RequestParam("email") String email, HttpSession session) {
        sendEmailService.sendSubscribe(email);
        session.setAttribute("msg", "You have successfully subscribe");
        return "redirect:/product";
    }


    @GetMapping("/cart")
    public String cart(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/sign-in";
        } else {
            User currentUser = userService.getCurrentUser();
            List<CartItem> cartItems = cartItemRepository.findByUser(currentUser);
            model.addAttribute("cartItems", cartItems);
            return "home/cart";
        }


    }

    @PostMapping("/addToCart/{productId}")
    public String addToCart(@PathVariable Long productId, @RequestParam int quantity, Principal principal, HttpSession session) throws Exception {
        if (principal == null) {
            return "redirect:/sign-in";
        } else {
            Product product = productRepository.findById(productId).orElseThrow(() -> new Exception("Product not found"));
            User currentUser = userService.getCurrentUser();
//            String username = principal.getName();
//            User currentUser = userService.findByUsername(username);
            CartItem currentCart = cartItemRepository.findByUserAndProduct(currentUser, product);
            if (currentCart != null) {
                int addQuantity = currentCart.getQuantity() + quantity;
                currentCart.setQuantity(addQuantity);
            } else {
                currentCart = new CartItem();
                currentCart.setUser(currentUser);
                currentCart.setProduct(product);
                currentCart.setQuantity(quantity);
            }

            cartItemRepository.save(currentCart);
            session.setAttribute("msg", "The product has been added to cart");
            return "redirect:/detailProduct/{productId}";
        }


    }

//    @PostMapping("/updateCart")
//    public String updateQuantity(@RequestParam("id") Long productId, @RequestParam("quantity") int quantity, HttpSession session, Model model) throws Exception {
//        User user = userService.getCurrentUser();
//        Product product = productRepository.findById(productId).orElseThrow(() -> new Exception("Product not found"));
//
//        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);
//        if (cartItem != null) {
//            cartItem.setQuantity(quantity);
//            cartItemRepository.save(cartItem);
//            session.setAttribute("msg", "Update cart successful");
//        }
//        return "redirect:/cart";
//    }

    @GetMapping("/deleteCart/{id}")
    public String deleteCart(@PathVariable("id") Long cartId, HttpSession session) {
        cartItemRepository.deleteById(cartId);
        session.setAttribute("msg", "Delete cart successful");
        return "redirect:/cart";
    }

    @GetMapping("/sign-in")
    public String login(Model model) {
        User currentUser = userService.getCurrentUser();
        List<CartItem> cartItems = cartItemRepository.findByUser(currentUser);
        model.addAttribute("cartItems", cartItems);
        return "home/login";
    }


    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        User currentUser = userService.getCurrentUser();
        List<CartItem> cartItems = cartItemRepository.findByUser(currentUser);
        model.addAttribute("cartItems", cartItems);
        return "home/register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, HttpSession session, @RequestParam("confirm") String confirm) {
        if (bindingResult.hasErrors()) {
            return "home/register";
        } else {
            boolean check = userService.checkEmail(user.getEmail());
            boolean checkFirstNameAndLastName = userService.checkUsername(user.getFirstName(), user.getLastName());
            if (check) {
                session.setAttribute("msg", "The email already exist");
                return "redirect:/register";
            } else if (checkFirstNameAndLastName) {
                session.setAttribute("msg", "The first name or last name already exist");
                return "redirect:/register";
            } else {
                if (user.getPassword().equals(confirm) == false) {
                    session.setAttribute("msg", "The password not match");
                    return "redirect:/register";
                } else {
                    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                    user.setUsername(user.getFirstName() + " " + user.getLastName());
                    user.setRole("ROLE_USER");
                    userRepository.save(user);
                    return "redirect:/sign-in";
                }


            }
        }

    }

    @GetMapping("/forgot")
    public String forgot() {
        return "home/forgot_password";
    }

    @PostMapping("/forgot")
    public String forgotPost(@RequestParam String email, HttpSession session) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return "redirect:/reset/" + user.getId();
        } else {
            session.setAttribute("msg", "Invalid email");
            return "home/forgot_password";
        }
    }

    @GetMapping("/reset/{id}")
    public String resetPass(@PathVariable int id, Model model) {
        model.addAttribute("id", id);
        return "home/reset_password";
    }

    @PostMapping("/changePassword")
    public String postReset(@RequestParam String password, @RequestParam Integer id, HttpSession session) {
        User user = userRepository.findById(id).get();
        String encrypt = bCryptPasswordEncoder.encode(password);
        user.setPassword(encrypt);
        User updateUser = userRepository.save(user);
        if (updateUser != null) {
            session.setAttribute("changed", "Password has been changed");
        }
        return "redirect:/sign-in";

    }


    @GetMapping("/product")
    public String product(Model model, Principal principal, @Param("keyword") String keyword) {
        if (principal == null) {
            return "redirect:/sign-in";
        } else {
            List<Product> products = productService.getSearchProduct(keyword);
            Iterable<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("products", products);
            User currentUser = userService.getCurrentUser();
            List<CartItem> cartItems = cartItemRepository.findByUser(currentUser);
            model.addAttribute("cartItems", cartItems);
            return "home/product_list";
        }

    }

    @GetMapping("/product/searchByPrice")
    public String searchPrice(@RequestParam(value = "min_price", required = false) Double from, @RequestParam(value = "max_price", required = false) Double to, Model model) {
        if (from == null || to == null) {
            List<Product> products = productRepository.findAll();
            model.addAttribute("products", products);
            User currentUser = userService.getCurrentUser();
            List<CartItem> cartItems = cartItemRepository.findByUser(currentUser);
            model.addAttribute("cartItems", cartItems);
            Iterable<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);
        } else {
            List<Product> productByPrice = productService.getProductByPrice(from, to);
            model.addAttribute("products", productByPrice);
            User currentUser = userService.getCurrentUser();
            List<CartItem> cartItems = cartItemRepository.findByUser(currentUser);
            model.addAttribute("cartItems", cartItems);
            Iterable<Category> categories = categoryRepository.findAll();
            model.addAttribute("categories", categories);
        }

        return "home/product_list";
    }


    @GetMapping("/detailProduct/{id}")
    public String detailProduct(@PathVariable(value = "id") long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        User currentUser = userService.getCurrentUser();
        List<CartItem> cartItems = cartItemRepository.findByUser(currentUser);
        model.addAttribute("cartItems", cartItems);
        return "home/single-product";

    }


    @GetMapping("/checkout")
    public String checkout(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/sign-in";
        } else {
            User currentUser = userService.getCurrentUser();
            List<CartItem> cartItems = cartItemRepository.findByUser(currentUser);
            model.addAttribute("cartItems", cartItems);
            return "home/checkout";
        }

    }

    @GetMapping("/order")
    public String showOrder(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/sign-in";
        } else {
            User currentUser = userService.getCurrentUser();
            List<Order> listOrder = orderRepository.findByUser(currentUser);
            model.addAttribute("listOrder", listOrder);
            List<CartItem> cartItems = cartItemRepository.findByUser(currentUser);
            model.addAttribute("cartItems", cartItems);
            return "home/order";
        }
    }

    @PostMapping("/placeOrder")
    public String placeOrder(Principal principal,
                             HttpSession session,
                             @RequestParam("totalCost") Double totalCost,
                             @RequestParam("selector") String paymentSelect) throws PayPalRESTException {
        if (principal == null) {
            return "redirect:/sign-in";
        } else {
            User currentUser = userService.getCurrentUser();
            List<CartItem> currentCart = cartItemRepository.findByUser(currentUser);
            for (CartItem cartItem : currentCart) {
                Order newOrder = new Order();
                if (paymentSelect.equals("Cash")) {
                    newOrder.setOrder_status("Processing");
                } else {
                    newOrder.setOrder_status("Paid");
                }
                newOrder.setOrder_date(new Date());
                newOrder.setCurrency("USD");
                newOrder.setUser(cartItem.getUser());
                newOrder.setProduct(cartItem.getProduct());
                newOrder.setProduct_name(cartItem.getProduct().getProduct_name());
                newOrder.setQuantity(cartItem.getQuantity());
                newOrder.setTotal_cost(cartItem.getProduct().getPrice() * cartItem.getQuantity());
                if (paymentSelect.equals("Cash")) {
                    newOrder.setPayment_method("CASH");
                } else {
                    newOrder.setPayment_method("PAYPAL");
                    Payment payment = service.createPayment(totalCost, "USD", paymentSelect, "SALE", "http://localhost:8080/" + CANCEL_URL, "http://localhost:8080/" + SUCCESS_URL);
                    for (Links link : payment.getLinks()) {
                        if (link.getRel().equals("approval_url")) {
                            for (CartItem cartItemPaypal : currentCart) {
                                Order newOrderPaypal = new Order();
                                if (paymentSelect.equals("Cash")) {
                                    newOrderPaypal.setOrder_status("Processing");
                                } else {
                                    newOrderPaypal.setOrder_status("Paid");
                                }
                                newOrderPaypal.setOrder_date(new Date());
                                newOrderPaypal.setCurrency("USD");
                                if (paymentSelect.equals("Cash")) {
                                    newOrderPaypal.setPayment_method("CASH");
                                } else {
                                    newOrderPaypal.setPayment_method("PAYPAL");
                                }
                                newOrderPaypal.setUser(cartItemPaypal.getUser());
                                newOrderPaypal.setProduct(cartItemPaypal.getProduct());
                                newOrderPaypal.setProduct_name(cartItemPaypal.getProduct().getProduct_name());
                                newOrderPaypal.setQuantity(cartItemPaypal.getQuantity());
                                newOrderPaypal.setTotal_cost(cartItemPaypal.getProduct().getPrice() * cartItemPaypal.getQuantity());
                                orderRepository.save(newOrderPaypal);
                                cartItemRepository.delete(cartItemPaypal);

                            }
                            return "redirect:" + link.getHref();
                        }
                    }
                }
                orderRepository.save(newOrder);
                cartItemRepository.delete(cartItem);
            }
            session.setAttribute("msgOrder", "You have successfully placed your order");
            return "redirect:/order";
        }
    }

    @GetMapping("/cancelOrder/{id}")
    public String cancelOrder(Principal principal, @PathVariable long id, HttpSession session) {
        if (principal == null) {
            return "redirect:/sign-in";
        } else {
            orderRepository.deleteById(id);
            session.setAttribute("msgOrder", "Order has been canceled successfully");
            return "redirect:/order";
        }
    }


    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "home/cancel";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return "home/success";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/";
    }


    @GetMapping("/confirmation")
    public String confirmation() {
        return "home/confirmation";
    }
}
