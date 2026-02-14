package com.LucasBot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 1) 你原本的 PasswordEncoder（保留 + 可用）
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    // 2) 解决 X-Frame-Options: DENY，并让你的接口先都能访问（方便你用 Postman 测试）
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 你现在主要在做 API 测试，先关掉 CSRF（否则 POST/PUT/DELETE 在某些情况下会被拦）
                .csrf(csrf -> csrf.disable())

                // 解决 X-Frame-Options: DENY（允许被 iframe 嵌套 / H2 console 等）
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // 先全部放行（后面你做 JWT 登录时再改成需要认证）
                .authorizeRequests(auth -> auth.anyRequest().permitAll())

                // 可选：如果你不需要表单登录/Basic 认证，可以保持默认即可
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
