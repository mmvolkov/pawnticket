package ru.softline.pawnticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softline.pawnticket.entity.PawnTicket;

import java.util.List;
import java.util.Optional;

@Repository
public interface PawnTicketRepository extends JpaRepository<PawnTicket, Long> {
    Optional<PawnTicket> findByTicketNumber(String ticketNumber);
    List<PawnTicket> findByUserId(Long userId);
    List<PawnTicket> findByStatus(PawnTicket.Status status);
}