package dev.cyan.travel.response;

import com.mongodb.MongoWriteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class Advice {
    private ResponseEntity<MessageResponse> exceptionHandler(Exception exception, HttpStatus httpStatus) {
        String message = exception.getLocalizedMessage();
        return ResponseEntity.status(httpStatus)
                .body(new MessageResponse(message));
    }

    @ExceptionHandler(MongoWriteException.class)
    public ResponseEntity<MessageResponse> handleMongoWriteException(MongoWriteException exception) {
        return exceptionHandler(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<MessageResponse> handleNoSuchElementException(NoSuchElementException exception) {
        return exceptionHandler(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        return exceptionHandler(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<MessageResponse> handleCannotDeleteException(IOException exception) {
        return exceptionHandler(exception, HttpStatus.BAD_REQUEST);
    }
}
