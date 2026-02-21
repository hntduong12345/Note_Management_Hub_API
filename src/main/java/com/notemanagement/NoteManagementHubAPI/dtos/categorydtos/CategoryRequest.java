package com.notemanagement.NoteManagementHubAPI.dtos.categorydtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryRequest {
    private String name;
    private String iconIdentifier;
}
