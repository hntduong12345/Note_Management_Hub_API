package com.notemanagement.NoteManagementHubAPI.exceptions;

import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException exception){
        ExceptionObject notFoundException = new ExceptionObject(
                exception.getMessage(), exception.getCause(), HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(notFoundException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException exception){
        ExceptionObject notFoundException = new ExceptionObject(
                exception.getMessage(), exception.getCause(), HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(notFoundException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConflictException.class})
    public ResponseEntity<Object> handleConflictException(ConflictException exception){
        ExceptionObject notFoundException = new ExceptionObject(
                exception.getMessage(), exception.getCause(), HttpStatus.CONFLICT
        );

        return new ResponseEntity<>(notFoundException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {InternalException.class})
    public ResponseEntity<Object> handleInternalException(InternalException exception){
        ExceptionObject notFoundException = new ExceptionObject(
                exception.getMessage(), exception.getCause(), HttpStatus.INTERNAL_SERVER_ERROR
        );

        return new ResponseEntity<>(notFoundException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    public ResponseEntity<Object> handleUnauthorizeException(UnauthorizedException exception){
        ExceptionObject notFoundException = new ExceptionObject(
                exception.getMessage(), exception.getCause(), HttpStatus.UNAUTHORIZED
        );

        return new ResponseEntity<>(notFoundException, HttpStatus.UNAUTHORIZED);
    }
}
