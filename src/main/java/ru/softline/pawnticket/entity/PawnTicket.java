package ru.softline.pawnticket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pawn_tickets")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PawnTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_number", nullable = false, unique = true)
    private String ticketNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "item_description")
    private String itemDescription;

    private BigDecimal loanAmount;
    private BigDecimal interestRate;

    private LocalDateTime issueDate;

    private LocalDateTime expiryDate;

    private LocalDateTime prolongedUntil;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Status {
        ACTIVE,
        CLOSED,
        EXPIRED
    }
}
