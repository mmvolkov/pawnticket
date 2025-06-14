package ru.softline.pawnticket.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProlongPawnTicketDto {
    @NotNull(message = "Prolongation date is required")
    private LocalDateTime prolongUntil;
}