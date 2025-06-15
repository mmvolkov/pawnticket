package ru.softline.pawnticket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePawnTicketDto {

    /** Можно передать готовый номер; если не передан, будет сгенерирован автоматически. */
    private String ticketNumber;

    @NotBlank(message = "Item description is required")
    private String itemDescription;

    @NotNull(message = "Loan amount is required")
    @Positive
    private BigDecimal loanAmount;

    @NotNull(message = "Interest rate is required")
    @Positive
    private BigDecimal interestRate;

    /** Дата выдачи; если не указана, будет установлена текущая. */
    private LocalDateTime issueDate;

    @NotNull(message = "Expiry date is required")
    private LocalDateTime expiryDate;
}
