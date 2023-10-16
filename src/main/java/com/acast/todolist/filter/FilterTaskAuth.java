package com.acast.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.acast.todolist.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if (servletPath.equals("/tasks/")) {
            System.out.println("EAEAE");
            var authorization = request.getHeader("Authorization");
            System.out.println("AUTH");
            
            var user_password_64 = authorization.substring("Basic".length()).trim();
            
            byte[] decoded = Base64.getDecoder().decode(user_password_64);
            var stringResult = new String(decoded);
            var credentials = stringResult.split(":");
            String username = credentials[0];
            String password = credentials[1];
            System.out.println(username);
            System.out.println(password);
    
            var user = this.userRepository.findByUsername(username);
    
            if (user == null) {
                response.sendError(401);
            } else {

                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if(passwordVerify.verified) {
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }
            
        }else {
            System.out.println("abatatatata");
            filterChain.doFilter(request, response);
        }

    }

}
