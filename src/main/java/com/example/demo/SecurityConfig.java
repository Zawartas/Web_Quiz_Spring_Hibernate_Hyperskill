package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());


//                .inMemoryAuthentication()
//                .withUser("user1").password("{noop}abc"/*encoder().encode("abc")*/)
//                .roles("USER")
//                ;
//                .and()
//                .withUser("user2").password(encoder().encode("123"))
//                .roles("USER")
//                .and()
//                .withUser("admin1").password(encoder().encode("secret2"))
//                .roles("USER", "ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/actuator/shutdown").permitAll()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/h2/**").permitAll()
                .anyRequest().authenticated()
//                .antMatchers("/students/**")
//                .hasRole("USER")
//                .antMatchers("/**")
//                .hasRole("ADMIN")
                .and()
                .csrf().disable() // we are not passing the CSRF header with Postman
                .headers().frameOptions().disable() //allows to enter H2 console with Spring Security enabled
                .and()
                .httpBasic();
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

        /*
          .antMatchers("/private/**")
          .authenticated()
          .antMatchers("/public/**")
          .permitAll()
          .and()
          .httpBasic();
        * */

        // https://www.baeldung.com/spring-security-registration-password-encoding-bcrypt

        /*
        * http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/login*", "/logout*", "/signin/**", "/signup/**", "/customLogin",
                        "/user/registration*", "/registrationConfirm*", "/expiredAccount*", "/registration*",
                        "/badUser*", "/user/resendRegistrationToken*" ,"/forgetPassword*", "/user/resetPassword*","/user/savePassword*","/updatePassword*",
                        "/user/changePassword*", "/emailError*", "/resources/**","/old/user/registration*","/successRegister*","/qrcode*","/user/enableNewLoc*").permitAll()
                .antMatchers("/invalidSession*").anonymous()
                .antMatchers("/user/updatePassword*").hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
                .anyRequest().hasAuthority("READ_PRIVILEGE")
                .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/homepage.html")
                .failureUrl("/login?error=true")
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .authenticationDetailsSource(authenticationDetailsSource)
            .permitAll()
                .and()
            .sessionManagement()
                .invalidSessionUrl("/invalidSession.html")
                .maximumSessions(1).sessionRegistry(sessionRegistry()).and()
                .sessionFixation().none()
            .and()
            .logout()
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .invalidateHttpSession(false)
                .logoutSuccessUrl("/logout.html?logSucc=true")
                .deleteCookies("JSESSIONID")
                .permitAll()
             .and()
                .rememberMe().rememberMeServices(rememberMeServices()).key("theKey");
        *
        *
        * */
    }
}
