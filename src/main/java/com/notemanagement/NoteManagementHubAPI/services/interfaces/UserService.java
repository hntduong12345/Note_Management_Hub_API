package com.notemanagement.NoteManagementHubAPI.services.interfaces;

import com.notemanagement.NoteManagementHubAPI.dtos.authdtos.AuthResponse;
import com.notemanagement.NoteManagementHubAPI.dtos.authdtos.LoginRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.authdtos.RegisterRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.userdtos.UserResponse;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserService {
    public CompletableFuture<AuthResponse> register(RegisterRequest request);
    public CompletableFuture<AuthResponse> login(LoginRequest request);
    public CompletableFuture<UserResponse> getProfile(UUID id);
//    public CompletableFuture<MFAConfigResponse> setupMfaCode(UUID userId);
//    public CompletableFuture<Boolean> verifyMfaCode(UUID userId, int code);
}
