package ru.softline.pawnticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.softline.pawnticket.entity.PawnTicket;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PawnTicketResponseDto {

    private Long            id;
    private String          ticketNumber;

    private Long            userId;
    private String          username;

    private String          clientName;
    private String          itemDescription;

    private BigDecimal      loanAmount;
    private BigDecimal      interestRate;

    private LocalDateTime   issueDate;
    private LocalDateTime   expiryDate;
    private LocalDateTime   prolongedUntil;

    private PawnTicket.Status status;
    private LocalDateTime   createdAt;
    private LocalDateTime   updatedAt;
}
