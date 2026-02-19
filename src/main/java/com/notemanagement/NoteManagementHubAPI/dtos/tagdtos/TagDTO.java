package com.notemanagement.NoteManagementHubAPI.dtos.tagdtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TagDTO {
    private UUID id;
    private String name;
    private String colorCode;
}
