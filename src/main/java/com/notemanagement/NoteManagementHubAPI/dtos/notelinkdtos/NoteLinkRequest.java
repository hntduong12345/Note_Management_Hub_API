package com.notemanagement.NoteManagementHubAPI.dtos.notelinkdtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class NoteLinkRequest {
    private UUID sourceId;
    private UUID targetId;
    private UUID userId;
    private String linkContext;
}
