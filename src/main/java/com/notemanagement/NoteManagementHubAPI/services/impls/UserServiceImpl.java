package com.notemanagement.NoteManagementHubAPI.services.impls;

import com.notemanagement.NoteManagementHubAPI.dtos.authdtos.AuthResponse;
import com.notemanagement.NoteManagementHubAPI.dtos.authdtos.LoginRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.authdtos.RegisterRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.userdtos.UserResponse;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.BadRequestException;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.NotFoundException;
import com.notemanagement.NoteManagementHubAPI.models.User;
import com.notemanagement.NoteManagementHubAPI.repositories.UserRepository;
import com.notemanagement.NoteManagementHubAPI.securities.jwt.JwtService;
import com.notemanagement.NoteManagementHubAPI.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<AuthResponse> register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new BadRequestException("Email has already registered");
        }

        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setEmail(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(newUser);

        //Generate JWT token and response
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_ALL")
        );

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                savedUser.getEmail(),
                savedUser.getPassword(),
                authorities
        );

        String jwtToken = jwtService.generateToken(userDetails);

        return CompletableFuture.completedFuture(
                new AuthResponse("SUCCESS", jwtToken, "", savedUser.getId(), savedUser.getEmail())
        );
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<AuthResponse> login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User is not found"));

        String jwtToken = jwtService.generateToken(userDetails);
        return CompletableFuture.completedFuture(
                new AuthResponse("SUCCESS", jwtToken, "", user.getId(), user.getEmail())
        );
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<UserResponse> getProfile(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User is not found"));
        UserResponse response = new UserResponse(
                user.getId(),
                user.getEmail()
        );

        return CompletableFuture.completedFuture(response);
    }
}
