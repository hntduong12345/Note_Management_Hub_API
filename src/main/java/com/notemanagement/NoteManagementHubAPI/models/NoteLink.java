package com.notemanagement.NoteManagementHubAPI.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "note_links")
@Getter
@Setter
@NoArgsConstructor
public class NoteLink extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_note_id")
    private Note sourceNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_note_id")
    private Note targetNote;

    @Column(columnDefinition = "text")
    private String linkContext;
}
