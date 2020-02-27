package com.ant.ptpapp.config;

import com.ant.ptpapp.common.NoPasswordEncoder;
import com.ant.ptpapp.filter.JwtAuthenticationTokenFilter;
import com.ant.ptpapp.handler.*;
import com.ant.ptpapp.service.SelfUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author: yichen
 * @description:
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConf extends WebSecurityConfigurerAdapter {


    @Autowired
    //未登陆时返回 JSON 格式的数据给前端（否则为 html）
    AjaxAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    //登录成功返回的 JSON 格式数据给前端（否则为 html）
    AjaxAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    //登录失败返回的 JSON 格式数据给前端（否则为 html）
    AjaxAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    //注销成功返回的 JSON 格式数据给前端（否则为 登录时的 html）
    AjaxLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    //无权访问返回的 JSON 格式数据给前端（否则为 403 html 页面）
    AjaxAccessDeniedHandler accessDeniedHandler;

    @Autowired
    // 自定义user
    SelfUserDetailsService userDetailsService;

    @Autowired
    // JWT 拦截器
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 加入自定义的安全认证
//        auth.authenticationProvider(provider);
        auth.userDetailsService(userDetailsService).passwordEncoder(new NoPasswordEncoder());//这里使用自定义的加密方式(不使用加密)，security提供了 BCryptPasswordEncoder 加密可自定义或使用这个
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 去掉 CSRF
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //关闭session管理，使用token机制处理
                .and() .httpBasic().authenticationEntryPoint(authenticationEntryPoint)
                .and().authorizeRequests().antMatchers("/swagger-ui.html").permitAll()
                .and().authorizeRequests().antMatchers("/swagger-resources/**").permitAll()
                .and().authorizeRequests().antMatchers("/images/**").permitAll()
                .and().authorizeRequests().antMatchers("/webjars/**").permitAll()
                .and().authorizeRequests().antMatchers("/v2/api-docs").permitAll()
                .and().authorizeRequests().antMatchers("/configuration/ui").permitAll()
                .and().authorizeRequests().antMatchers("/configuration/security").permitAll()
                //.and().antMatcher("/login")
                //.and().authorizeRequests().anyRequest().access("@rbacauthorityservice.hasPermission(request,authentication)")// 自定义权限校验  RBAC 动态 url 认证
//                .and().authorizeRequests().antMatchers(HttpMethod.GET,"/test").hasAuthority("test:list")
//                .and().authorizeRequests().antMatchers(HttpMethod.POST,"/test").hasAuthority("test:add")
//                .and().authorizeRequests().antMatchers(HttpMethod.PUT,"/test").hasAuthority("test:update")
//                .and().authorizeRequests().antMatchers(HttpMethod.DELETE,"/test").hasAuthority("test:delete")
//                .and().authorizeRequests().antMatchers("/test/*").hasAuthority("test:manager")
                .and().authorizeRequests().antMatchers(HttpMethod.POST,"/login").permitAll() //放行login(这里使用自定义登录)
                .and().authorizeRequests().antMatchers("/hello").permitAll();

//                .and()
//                .formLogin()  //开启登录, 定义当需要用户登录时候，转到的登录页面
//                .loginPage("/test/login.html")
//                .loginProcessingUrl("/login")
//                .successHandler(authenticationSuccessHandler) // 登录成功
//                .failureHandler(authenticationFailureHandler) // 登录失败
//                .permitAll()

//                .and()
//                .logout()//默认注销行为为logout
//                .logoutUrl("/logout")
//                .logoutSuccessHandler(logoutSuccessHandler)
//                .permitAll();

        // 无权访问 JSON 格式的数据
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
        // JWT Filter
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults(){
        return new GrantedAuthorityDefaults("");//remove the ROLE_ prefix
    }

}