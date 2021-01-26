package com.ttree.ttree.config;

import com.ttree.ttree.service.CustomUserDetailsService;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;


@Configuration
@EnableWebSecurity


public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect(){
        return new SpringSecurityDialect();
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception { //권한 인증 필요없는 애들은 통과
        webSecurity.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf().disable()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/projectPost", "/projectRules", "/projectNotes", "/staff/staffPage", "/changePW", "/changeEmail").hasRole("STAFF")
                .antMatchers("/user/studentPage", "/changePW", "/changeEmail").hasRole("STUDENT")
                .and()
                .formLogin().loginPage("/user/login")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true) //로그아웃시 세션제거
                .deleteCookies("JSESSIONID") // 쿠키제거
                .clearAuthentication(true)
                .permitAll()
                .and()
                .sessionManagement()
                .maximumSessions(1)//같은 id로 한명만 로그인 가능
                .expiredUrl("/user/login")
                .maxSessionsPreventsLogin(true);
    }

    /*
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPulisher(){
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }

     */

    /*사용자 임의로 넣기
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password(passwordEncoder().encode("1234")).roles("ADMIN")
                .and()
                .withUser("guest").password(passwordEncoder().encode("guest")).roles("GUEST");
    }

     */

    @Bean //invalidateHttpSession(true)가 작동하지 않을 때 사용
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher(){
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }
}