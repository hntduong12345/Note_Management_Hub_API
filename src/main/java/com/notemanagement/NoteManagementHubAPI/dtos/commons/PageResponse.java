package com.notemanagement.NoteManagementHubAPI.dtos.commons;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Schema(description = "Paginated response")
public class PageResponse<T> {
    @Schema(description = "List of content")
    private List<T> content;

    @Schema(description = "Current page number")
    private int number;

    @Schema(description = "Size of the page")
    private int size;

    @Schema(description = "Total elements")
    private long totalElements;

    @Schema(description = "Total pages")
    private int totalPages;

    @Schema(description = "Is last page")
    private boolean last;

    public static <T> PageResponse<T> from(Page<T> page) {
        PageResponse<T> response = new PageResponse<>();
        response.setContent(page.getContent());
        response.setNumber(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());
        return response;
    }
}
