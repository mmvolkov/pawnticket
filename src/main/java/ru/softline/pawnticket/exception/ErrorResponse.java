package ru.softline.pawnticket.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatusCode;

@Value
@Builder
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime timestamp;

    int           status;
    String        error;
    String        message;
    String        path;

    public static ErrorResponse of(HttpStatusCode status,
                                   String message,
                                   String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.toString())
                .message(message)
                .path(path)
                .build();
    }

}
