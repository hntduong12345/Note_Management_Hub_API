package com.notemanagement.NoteManagementHubAPI.dtos.userdtos;

import java.util.UUID;

public record UserResponse (
    UUID id,
    String email
){}
