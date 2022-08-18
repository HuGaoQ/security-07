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
 * 权限配置
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启注解权限
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * token认证过滤器
     */
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    /**
     * 权限不足处理类
     */
    @Autowired
    private AuthenticationEntryHanderImpl authenticationEntryHander;

    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    /**
     * 退出处理类
     */
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关系csrf
                .csrf().disable()
                //基于token所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //配置权限
                .authorizeRequests()
                //不拦截静态资源
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/admin/*.html",
                        "/css/*.css",
                        "/img/*.*",
                        "/img/**",
                        "/plugins/**"
                ).permitAll()
                //以下内容允许匿名访问
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