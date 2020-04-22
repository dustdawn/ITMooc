package com.njit.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security配置
 * 通过继承WebSecurityConfigurerAdapter并用@EnableWebSecurity注解来实现安全保障。
 * 注解@Order(-1) 保证该AOP在@Transactional之前执行
 *
 * @author dustdawn
 * @date 2020/4/10 19:25
 */
@Configuration
@EnableWebSecurity
@Order(-1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 指定springsecurity编码
     * 采用bcrypt对密码进行编码
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     *  Spring Security中进行身份验证的是AuthenticationManager接口
     *  ProviderManager是它的一个默认实现，但它并不用来处理身份认证，而是委托给配置好的AuthenticationProvider，
     *  每个AuthenticationProvider会轮流检查身份认证。检查后或者返回Authentication对象或者抛出异常。
     *
     *  验证身份就是加载响应的UserDetails，看看是否和用户输入的账号、密码、权限等信息匹配。
     *  此步骤由实现AuthenticationProvider的DaoAuthenticationProvider
     *  （它利用UserDetailsService验证用户名、密码和授权）处理。
     *  包含 GrantedAuthority 的 UserDetails对象在构建 Authentication对象时填入数据。
     *
     * 认证管理器
     * 进行身份认证
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager manager = super.authenticationManagerBean();
        return manager;
    }



    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/userlogin","/userlogout","/userjwt","/userlogon");

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().and()
                .formLogin()
                .and()
                .authorizeRequests().anyRequest().authenticated();

    }
}
