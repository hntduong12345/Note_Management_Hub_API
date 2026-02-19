package com.notemanagement.NoteManagementHubAPI.dtos.notedtos;

import com.notemanagement.NoteManagementHubAPI.dtos.tagdtos.CreateTagRequest;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class NoteRequest {
    private String title;
    private Map<String, Object> contentBody;
    private UUID categoryId;
    private List<CreateTagRequest> tagRequest;
    private LocalDateTime lastSyncAt;
}
