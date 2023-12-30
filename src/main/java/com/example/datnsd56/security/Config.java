package com.example.datnsd56.security;

import com.example.datnsd56.repository.AccountRepository;
//import com.example.datnsd56.service.impl.UserInforDetailService;
//import com.example.datnsd56.service.impl.UserInforDetailService;
//import com.example.datnsd56.service.impl.UserInforDetailService;
//import com.example.datnsd56.security1.UserService;
//import com.example.datnsd56.service.impl.UserInforDetailService;
//import com.example.datnsd56.service.impl.UserInforDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
@RequiredArgsConstructor


public class Config {
    //sql
    @Autowired
    private final AccountRepository repository;
//@Autowired
//    UserService userService;
    //sql- start
    @Bean
    public UserDetailsService userDetailsService(  ) {
        return new UserInforDetailService(repository);
        };
//    }
//    sql- end
//    @Bean
//    protected void configure(AuthenticationManagerBuilder auth)
//            throws Exception {
//        auth.userDetailsService(userService) // Cung cáp userservice cho spring security
//            .passwordEncoder(passwordEncoder()); // cung cấp password encoder
//    }
//    fix cung - start
//@Bean
//public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//    UserDetails admin = User.withUsername("cam")
//            .password(encoder.encode("cam"))
//            .roles("ADMIN")
//            .build();
//    UserDetails user = User.withUsername("tao")
//            .password(encoder.encode("tao"))
//            .roles("USER")
//            .build();
//    return new InMemoryUserDetailsManager(admin, user);
//}
//fix cung end
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/dashboard/css/**", "/dashboard/js/**", "/dashboard/img/**","/dashboard/bundles/**","/dashboard/fonts/**","/dashboard/lib/**","/dashboard/scss/**").permitAll()
            .requestMatchers("/website/css/**", "/website/js/**", "/website/img/**", "/website/lib/**", "/website/scss/**","templates/website/index/**","templates/dashboard/**","/website/**").permitAll()
            .requestMatchers("/hello").permitAll()
            .requestMatchers("/product/**").permitAll()// với endpoint /hello thì sẽ được cho qua
            .requestMatchers("/pay/**").permitAll()// với endpoint /hello thì sẽ được cho qua
            .requestMatchers("/vnpay-ipn/**").permitAll()// với endpoint /hello thì sẽ được cho qua
//        .requestMatchers("/display/**").permitAll()
//            .requestMatchers("/cart/**").permitAll()// với endpoint /hello thì sẽ được cho qua
             .requestMatchers("/error/**").permitAll()// với endpoint /hello thì sẽ được cho qua
            .requestMatchers("/login/**").permitAll()// với endpoint /hello thì sẽ được cho qua
            .requestMatchers("/admin/**").permitAll()// với endpoint /hello thì sẽ được cho qua
           // .requestMatchers("/admin/san-pham-test/**").permitAll()// với endpoint /hello thì sẽ được cho qua

            .and()
            .authorizeHttpRequests()
            .requestMatchers("/customer/**").authenticated()
            .requestMatchers("/cart/**").authenticated()// với endpoint /customer/** sẽ yêu cầu authenticate
            .requestMatchers("/user/**").authenticated()// với endpoint /customer/** sẽ yêu cầu authenticate
          //  .requestMatchers("/admin/**").authenticated() // với endpoint /customer/** sẽ yêu cầu authenticate
            .requestMatchers("/rest/**").authenticated()// với endpoint /customer/** sẽ yêu cầu authenticate

            .and().formLogin()// trả về page login nếu chưa authenticate

           .defaultSuccessUrl("/product/hien-thi")


            .loginPage("/login/custom-login").permitAll().successHandler(new CustomAuthenticationSuccessHandler())//.failureHandler(new SimpleUrlAuthenticationFailureHandler())
            .and()
            .logout()
            .logoutSuccessUrl("/login/custom-login")
            .permitAll()
            .and().build();
}

    //sql
@Bean
public AuthenticationProvider authenticationProvider(){
    DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
}

}

