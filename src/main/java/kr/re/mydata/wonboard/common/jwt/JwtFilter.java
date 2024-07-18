package kr.re.mydata.wonboard.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.re.mydata.wonboard.common.config.UserDetail;
import kr.re.mydata.wonboard.common.config.UserDetailsServiceImpl;
import kr.re.mydata.wonboard.common.constant.ApiRespPolicy;
import kr.re.mydata.wonboard.common.exception.CommonApiException;
import kr.re.mydata.wonboard.common.model.response.ApiV2Resp;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.lang.model.type.ErrorType;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, jakarta.servlet.ServletException, RuntimeException {
        final String authorization = request.getHeader("Authorization");
        // logger.info("authorization : " + authorization);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            logger.error("authorization is null or does not start with Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorization.substring(7); // Bearer 부분을 제거하고 토큰만 추출
        logger.info("Extracted accessToken: " + accessToken);

        try {
            jwtUtil.isTokenValid(accessToken);

            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            }

            String userName = jwtUtil.extractLoginEmail(accessToken);
            logger.info("Extracted userName: " + userName);

            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            if (userDetails == null) {
                logger.error("User not found");
                filterChain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            logger.info("Authentication set for user: " + userName);

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            jwtExceptionHandler(response, ApiRespPolicy.ERR_TOKEN_EXPIRED);
        }catch (io.jsonwebtoken.JwtException e) {
            jwtExceptionHandler(response, ApiRespPolicy.ERR_TOKEN_INVALID);
        }
    }
    
    public void jwtExceptionHandler(HttpServletResponse response, ApiRespPolicy apiRespPolicy) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(apiRespPolicy.getHttpStatus().value());
        try {
            String respBody = new ObjectMapper().writeValueAsString(ApiV2Resp.withError(apiRespPolicy));
            response.getWriter().write(respBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, jakarta.servlet.ServletException {
//        final String authorization = request.getHeader("Authorization");
//        logger.info("authorization : " + authorization);
//
//        if (authorization == null || !authorization.startsWith("Bearer ")) {
//            logger.error("authorization is null or does not start with Bearer");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authorization.substring(7); // Bearer 부분을 제거하고 토큰만 추출
//        logger.info("Extracted token: " + token);
//
//        try {
//            if (!jwtUtil.isTokenValid(token)) {
//                logger.error("token is invalid or expired");
//                throw new CommonApiException(ApiRespPolicy.ERR_TOKEN_EXPIRED);
//            }
//        } catch (CommonApiException e) {
//            response.setContentType("text/plain; charset=UTF-8");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write(e.getMessage());
//            return;
//        }
//
//        if (SecurityContextHolder.getContext().getAuthentication() != null) {
//            UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        }
//
//
//
////        if (!jwtUtil.isTokenValid(token)) {
////            logger.error("token is invalid or expired");
////            filterChain.doFilter(request, response);
////            return;
////        }
//
//        String userName = jwtUtil.extractLoginEmail(token);
//        logger.info("Extracted userName: " + userName);
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
//        if(userDetails == null) {
//            logger.error("User not found");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//
////        User user = userService.getUserByEmail(userName);
////        if (user == null) {
////            logger.error("User not found");
////            filterChain.doFilter(request, response);
////            return;
////        }
//
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        logger.info("Authentication set for user: " + userName);
//
//        filterChain.doFilter(request, response);
//    }


}
