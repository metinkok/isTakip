package com.example.job_scheduler;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class Config extends WebSecurityConfigurerAdapter{
    
    //Spring security login ekranını devre dışı bırakır
    @Override
    public void configure(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());

        //Api filtreleri JWT rol için
        //Get metodları için
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/users/**").authenticated();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/users/admin").hasAuthority("admin");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/users/**/admin").hasAuthority("admin");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/message/**").authenticated();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/jobs/**").authenticated();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/logs/").authenticated();

        //Post metodları
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/jobs/deadline/update").hasAuthority("admin");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/jobs/**/point/update").hasAuthority("admin");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/jobs/**").authenticated();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/message/**").authenticated();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/users/**/reset_password").authenticated();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/users/update/point").authenticated();
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/users/**/reset_password/admin").hasAuthority("admin");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/users/active").hasAuthority("admin");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/users/reset/points").hasAuthority("admin");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/users/**/authorization").hasAuthority("admin");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/users/**/authorization/revoke").hasAuthority("admin");
        
        http.httpBasic().disable();
        //JWT
        http.addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilter(new JsonWebToken(authenticationManagerBean()));
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("localhost:*"));//Localhosttan gelen istekleri bloklama
        configuration.setAllowedMethods(Arrays.asList("*"));//Bütün metodlara izin ver
        configuration.setAllowedHeaders(Arrays.asList("*"));//Bütün headerlara izin ver
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return new Authantication();
    }

    public static Algorithm getAlgorithm(){
        return Algorithm.HMAC256("Voychek".getBytes());
    }
}