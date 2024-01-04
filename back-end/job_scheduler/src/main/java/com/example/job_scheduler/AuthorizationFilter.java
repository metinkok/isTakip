package com.example.job_scheduler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


public class AuthorizationFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/api/login")){
           filterChain.doFilter(request, response);
        }
        else{
            String jwt = request.getHeader(AUTHORIZATION);
            if((jwt != null) && (jwt.startsWith("Bearer "))){
                try{
                    String token = jwt.substring("Bearer ".length());
                    Algorithm algorithm = Config.getAlgorithm();
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decoded_token = verifier.verify(token);
                    String username = decoded_token.getSubject();
                    String role = decoded_token.getClaim("role").asString();
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                    //Otoriteler Collection olarak gitmeli UsernamePasswordAuthenticationToken oluşturulurken
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(authority);
                    UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(upat);
                    //Tokenın başka bilgisayarda çalıştırılmasının kontrolü(Uzun süreli access token kullanıyoruz)
                    if(request.getRemoteAddr().equals(decoded_token.getId())){
                        filterChain.doFilter(request, response);
                    }
                    else
                        throw new Exception("Remote ip and token ip does not match");
                }
                catch(Exception e){
                    if(e.getMessage().equals("Remote ip and token ip does not match"))
                        response.sendError(401, e.getMessage());
                    else
                        response.sendError(403, e.getMessage());
                    e.printStackTrace();
                }
            }
            else
                filterChain.doFilter(request, response);
        }
    }
    
}
