package com.notemanagement.NoteManagementHubAPI.services.impls;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.notemanagement.NoteManagementHubAPI.dtos.attachmentdtos.AttachmentResponse;
import com.notemanagement.NoteManagementHubAPI.dtos.attachmentdtos.AttachmentUploadRequest;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.InternalException;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.NotFoundException;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.UnauthorizedException;
import com.notemanagement.NoteManagementHubAPI.models.Attachment;
import com.notemanagement.NoteManagementHubAPI.models.Note;
import com.notemanagement.NoteManagementHubAPI.repositories.AttachmentRepository;
import com.notemanagement.NoteManagementHubAPI.repositories.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentServiceImpl implements com.notemanagement.NoteManagementHubAPI.services.interfaces.AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final NoteRepository noteRepository;
    private final Cloudinary cloudinary;

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<AttachmentResponse> linkAttachment(AttachmentUploadRequest request, UUID userId) {
        //Check note ownership
        Note note = noteRepository.findById(request.getNoteId())
                .orElseThrow(() -> new NotFoundException("Note is not found"));

        //Extract id from cloudinary
//        String publicId = extractPublicId(request.getFileUrl());
        String publicId = "test";

        Attachment newAttachment = new Attachment();
        newAttachment.setNote(note);
        newAttachment.setFileUrl(request.getFileUrl());
        newAttachment.setExternalId(publicId);
        newAttachment.setFileType(request.getFileType());
        newAttachment.setFileSizeBytes(request.getFileSizeBytes());

        //Save to db
        Attachment savedAttachment = attachmentRepository.save(newAttachment);
        AttachmentResponse response = new AttachmentResponse(
                savedAttachment.getId(),
                savedAttachment.getFileUrl(),
                savedAttachment.getFileType(),
                savedAttachment.getFileSizeBytes()
        );
        return CompletableFuture.completedFuture(response);
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Void> deleteAttachment(UUID id, UUID userId) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Attachment is not found"));

        if(!attachment.getNote().getUser().getId().equals(userId)){
            throw new UnauthorizedException("Unauthorized user");
        }

        attachmentRepository.delete(attachment);

//        Temporary disable for only BE testing
//        try {
//            //Delete from Cloudinary using the External ID
//            cloudinary.uploader().destroy(attachment.getExternalId(), ObjectUtils.emptyMap());
//            //Delete from Postgres
//            attachmentRepository.delete(attachment);
//        } catch (IOException e) {
//            log.error("Failed to delete from Cloudinary: {}", attachment.getExternalId(), e);
//            throw new InternalException("Cloudinary deletion failed", e.getCause());
//        }

        return CompletableFuture.completedFuture(null);
    }

    //Helper get the id from the cloudinary file url
    private String extractPublicId(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
    }
}
