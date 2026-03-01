package com.notemanagement.NoteManagementHubAPI.dtos.notedtos;

import com.notemanagement.NoteManagementHubAPI.dtos.tagdtos.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class NoteResponse {
    private UUID id;
    private String title;
    private Map<String, Object> contentBody;
    private String category;
    private List<TagDTO> tags; // Detailed tag info
    private LocalDateTime updatedAt;
}
