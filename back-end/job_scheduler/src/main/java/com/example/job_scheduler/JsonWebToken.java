package com.example.job_scheduler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.security.authentication.AuthenticationManager;

public class JsonWebToken extends UsernamePasswordAuthenticationFilter{
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    Authantication authantication;
    
    public JsonWebToken(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/api/login");
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Login login = new Login(request.getParameter("e_mail"), request.getParameter("password"), 0);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login.getUserName(), login.getPassword());
        return authenticationManager.authenticate(authenticationToken);
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        Algorithm algorithm = Config.getAlgorithm();
        String role = "user";
        if(request.getParameter("e_mail").indexOf(".admin") == request.getParameter("e_mail").length()-6){
            role = "admin";
        }
        String e_mail = request.getParameter("e_mail");
        if(e_mail.contains(".admin"))
            e_mail = e_mail.substring(0, e_mail.indexOf(".admin"));
        String access_token = JWT.create()
                .withSubject(e_mail)
                .withClaim("role", role)
                .withExpiresAt(new Date(System.currentTimeMillis() +  60 * 60 * 1000 * 12))//12 saat süreyle access tokenı verilir
                .withIssuer(request.getRequestURL().toString())
                .withJWTId(request.getRemoteAddr())
                .sign(algorithm);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("message", "Oturum açma işlemi başarılı");
        request.getSession().setAttribute("User", request.getParameter("e_mail"));
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}