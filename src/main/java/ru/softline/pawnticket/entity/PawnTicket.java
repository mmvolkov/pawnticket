package ru.softline.pawnticket.entity;

import jakarta.persistence.*;
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
@Entity
@Table(name = "pawn_tickets")
public class PawnTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ticketNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "client_name")
    private String clientName;

    @Column(nullable = false)
    private String itemDescription;

    @Column(nullable = false)
    private BigDecimal loanAmount;

    @Column(nullable = false)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private LocalDateTime issueDate;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    private LocalDateTime prolongedUntil;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (issueDate == null) {
            issueDate = LocalDateTime.now();
        }
        if (status == null) {
            status = Status.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Status {
        ACTIVE, PROLONGED, REDEEMED, EXPIRED
    }
}