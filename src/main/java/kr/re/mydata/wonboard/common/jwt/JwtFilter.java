package kr.re.mydata.wonboard.common.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.re.mydata.wonboard.common.config.UserDetailsServiceImpl;
import kr.re.mydata.wonboard.common.constant.ApiRespPolicy;
import kr.re.mydata.wonboard.common.exception.CommonApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

//    @Autowired
//    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, jakarta.servlet.ServletException {
        final String authorization = request.getHeader("Authorization");
        logger.info("authorization : " + authorization);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            logger.error("authorization is null or does not start with Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7); // Bearer 부분을 제거하고 토큰만 추출
        logger.info("Extracted token: " + token);

        try {
            if (!jwtUtil.isTokenValid(token)) {
                logger.error("token is invalid or expired");
                throw new CommonApiException(ApiRespPolicy.ERR_TOKEN_EXPIRED);
            }
        } catch (CommonApiException e) {
            response.setContentType("text/plain; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage());
            return;
        }

//        if (!jwtUtil.isTokenValid(token)) {
//            logger.error("token is invalid or expired");
//            filterChain.doFilter(request, response);
//            return;
//        }

        String userName = jwtUtil.extractLoginEmail(token);
        logger.info("Extracted userName: " + userName);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        if(userDetails == null) {
            logger.error("User not found");
            filterChain.doFilter(request, response);
            return;
        }


//        User user = userService.getUserByEmail(userName);
//        if (user == null) {
//            logger.error("User not found");
//            filterChain.doFilter(request, response);
//            return;
//        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        logger.info("Authentication set for user: " + userName);

        filterChain.doFilter(request, response);
    }
}
