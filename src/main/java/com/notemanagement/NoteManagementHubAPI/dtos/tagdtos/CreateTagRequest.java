package com.notemanagement.NoteManagementHubAPI.dtos.tagdtos;

import lombok.Data;

@Data
public class CreateTagRequest {
    public String name;
    public String color;
}
