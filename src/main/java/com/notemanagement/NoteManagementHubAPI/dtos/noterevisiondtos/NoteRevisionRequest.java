package com.notemanagement.NoteManagementHubAPI.dtos.noterevisiondtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class NoteRevisionRequest {
    private Map<String, Object> contentBody;
    private int versionNumber;
}
