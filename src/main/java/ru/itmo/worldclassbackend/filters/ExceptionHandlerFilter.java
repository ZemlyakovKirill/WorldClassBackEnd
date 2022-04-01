package ru.itmo.worldclassbackend.filters;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itmo.worldclassbackend.utils.AbstractController;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException e) {
            response.setHeader("Content-Type","application/json");
            response.setStatus(403);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(AbstractController.responseString(HttpStatus.FORBIDDEN,"Просроченный токен"));
        }catch (MalformedJwtException e) {
            response.setHeader("Content-Type","application/json");
            response.setStatus(403);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(AbstractController.responseString(HttpStatus.FORBIDDEN,"Поврежденный токен"));
        }catch (UnsupportedJwtException e){
            response.setHeader("Content-Type","application/json");
            response.setStatus(403);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(AbstractController.responseString(HttpStatus.FORBIDDEN,"Токен не поддерживается"));
        }catch (IllegalArgumentException ignored){

        }catch(SignatureException e){
            response.setHeader("Content-Type","application/json");
            response.setStatus(403);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(AbstractController.responseString(HttpStatus.FORBIDDEN,"Неправильная сигнатура токена"));
        }
    }
}