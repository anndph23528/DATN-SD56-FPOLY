package com.example.datnsd56.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {
    final private List<CustomerSecurity> customers = List.of(
            CustomerSecurity.builder().id("001").name("Customer 1").email("c1@gmail.com").build(),
            CustomerSecurity.builder().id("002").name("Customer 2").email("c2@gmail.com").build()
    );
//    @GetMapping("/custom-login")
//    public String customLoginPage(@RequestParam(name = "error", required = false) String error) {
//        if (error != null) {
//            // Handle the unauthorized access error, e.g., display a message to the user
//            // You can also customize the behavior based on the error parameter
//        }
//        return "/auth_login/auth-login"; // Return the name of your custom login page view
//    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello is exception");
    }

    @GetMapping("/customer/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<CustomerSecurity> getCustomerList(@PathVariable("id") String id, Authentication authentication) {
        // Log the roles of the current user
        if (authentication != null) {
            authentication.getAuthorities().forEach(authority -> {
                System.out.println("User has authority: " + authority.getAuthority());
            });
        }

        List<CustomerSecurity> customers = this.customers.stream().filter(customer -> customer.getId().equals(id)).toList();
        return ResponseEntity.ok(customers.get(0));
    }


    @GetMapping("/customer/all")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('user')")
    public ResponseEntity<List<CustomerSecurity>> getAll() {
        List<CustomerSecurity> customers = this.customers;
        return ResponseEntity.ok(customers);
    }
}
