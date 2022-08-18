package com.ncamc.config;

import com.ncamc.expression.AuthenticationEntryHanderImpl;
import com.ncamc.expression.AuthenticationEntryPointImpl;
import com.ncamc.expression.LogoutSuccessHandlerImpl;
import com.ncamc.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author: hugaoqiang
 * @CreateTime: 2022-07-05 10:20
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启注解权限
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AuthenticationEntryHanderImpl authenticationEntryHander;

    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/admin/*.html",
                        "/css/*.css",
                        "/img/*.*",
                        "/img/**",
                        "/plugins/**"
                ).permitAll()
                .antMatchers("/user/getUsername").anonymous()
                .antMatchers("/user/logout").anonymous()
                .antMatchers("/user/login").anonymous()
                .antMatchers("/user/register").anonymous()
                .antMatchers("/product/doc").anonymous()
                .antMatchers("/swagger-ui.html").anonymous()
                .antMatchers("/swagger-resources/**").anonymous()
                .anyRequest().authenticated();
        //添加过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //配置异常处理
        http.exceptionHandling()
                .accessDeniedHandler(authenticationEntryHander)
                .authenticationEntryPoint(authenticationEntryPoint);
        //退出登录
        http.logout().logoutSuccessHandler(logoutSuccessHandler);
        //允许跨域
        http.cors();
        // 同源头
        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}