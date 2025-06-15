package ru.softline.pawnticket.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softline.pawnticket.entity.PawnTicket;

import java.util.List;

@Repository
public interface PawnTicketRepository extends JpaRepository<PawnTicket, Long> {

    @EntityGraph(attributePaths = "user")
    List<PawnTicket> findAll();

    @EntityGraph(attributePaths = "user")
    List<PawnTicket> findByUserId(Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);
}
