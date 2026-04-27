package br.com.issufibadji.todolist.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.issufibadji.todolist.exception.BadRequestException;

@ControllerAdvice
public class GeneralExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  private ResponseEntity<Object> handleBadRequest(BadRequestException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(exception.getMessage());
  }
}
