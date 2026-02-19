package com.notemanagement.NoteManagementHubAPI.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@NoArgsConstructor
public class Attachment extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id", nullable = false)
    private Note note;

    private String externalId;

    @Column(nullable = false, length = 512)
    private String fileUrl;

    private String fileType; // e.g., image/png, application/pdf

    private Long fileSizeBytes;
}
