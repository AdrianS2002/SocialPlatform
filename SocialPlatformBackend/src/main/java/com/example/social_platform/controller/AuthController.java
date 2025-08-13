package com.example.social_platform.controller;


import com.example.social_platform.config.JwtUtil;
import com.example.social_platform.controller.model.AuthRequest;
import com.example.social_platform.service.model.Auth;
import com.example.social_platform.service.ports.incoming.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    public AuthController(AuthService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, RedisTemplate<String, String> redisTemplate) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody AuthRequest user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(Auth.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        Auth user = userService.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(user.getId(), user.getRole(), user.getEmail(), user.getIsValidated());


        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                long expirationMillis = jwtUtil.getExpirationDateFromToken(token).getTime();
                long nowMillis = System.currentTimeMillis();
                long ttl = (expirationMillis - nowMillis) / 1000;

                redisTemplate.opsForValue().set("blacklist:" + token, "true", java.time.Duration.ofSeconds(ttl));

                return ResponseEntity.ok("User logged out successfully");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or invalid Authorization header");
    }
}


