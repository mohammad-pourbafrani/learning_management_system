package com.lms.learning_management_system.utils.jwt;

import com.lms.learning_management_system.entity.user.UserTokens;
import com.lms.learning_management_system.repository.user.UserTokensRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserTokensRepository userTokensRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // remove "Bearer "

        try {
            // 1️⃣ Check token exists in DB
            Optional<UserTokens> tokenRecord = userTokensRepository.findByAccessToken(token);
            if (tokenRecord.isEmpty()) {
                request.setAttribute("authError", "Token not found , please login");
                throw new BadCredentialsException("Token not found , please login");
            }
            //TODO:after chek a user how many try with not exist , revoke and expire token repeat non-stop for ban or ...

            if (tokenRecord.get().getRevoked()) {
                request.setAttribute("authError", "Token revoked , please login");
                throw new BadCredentialsException("Token revoked , please login");
            } else if (!jwtUtil.validateToken(token)) {
                request.setAttribute("authError", "Token expire , please refresh");
                throw new BadCredentialsException("Token expire , please refresh");
            }


            String email = jwtUtil.extractEmail(token);
            String phone = jwtUtil.extractPhone(token);
            String role = jwtUtil.extractRole(token);

            String username = !email.isBlank() ? email : phone;

            // 3️⃣ Create Authentication object
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())) // set authorities
                    );

            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            throw new BadCredentialsException("Invalid or expired token");
        }

        filterChain.doFilter(request, response);
    }
}