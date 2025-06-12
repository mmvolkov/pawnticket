package ru.softline.pawnticket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.softline.pawnticket.dto.CreatePawnTicketDto;
import ru.softline.pawnticket.dto.ProlongPawnTicketDto;
import ru.softline.pawnticket.entity.PawnTicket;
import ru.softline.pawnticket.service.PawnTicketService;

import java.util.List;

@RestController
@RequestMapping("/api/pawn-tickets")
@RequiredArgsConstructor
public class PawnTicketController {

    private final PawnTicketService pawnTicketService;

    @PostMapping
    public ResponseEntity<PawnTicket> createPawnTicket(@Valid @RequestBody CreatePawnTicketDto dto) {
        PawnTicket createdTicket = pawnTicketService.createPawnTicket(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @GetMapping
    public ResponseEntity<List<PawnTicket>> getAllPawnTickets() {
        List<PawnTicket> tickets = pawnTicketService.getAllPawnTickets();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PawnTicket> getPawnTicketById(@PathVariable Long id) {
        PawnTicket ticket = pawnTicketService.getPawnTicketById(id);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PawnTicket>> getPawnTicketsByUserId(@PathVariable Long userId) {
        List<PawnTicket> tickets = pawnTicketService.getPawnTicketsByUserId(userId);
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @pawnTicketService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<PawnTicket> updatePawnTicket(
            @PathVariable Long id,
            @Valid @RequestBody CreatePawnTicketDto dto) {
        PawnTicket updatedTicket = pawnTicketService.updatePawnTicket(id, dto);
        return ResponseEntity.ok(updatedTicket);
    }

    @PostMapping("/{id}/prolong")
    @PreAuthorize("hasRole('ADMIN') or @pawnTicketService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<PawnTicket> prolongPawnTicket(
            @PathVariable Long id,
            @Valid @RequestBody ProlongPawnTicketDto dto) {
        PawnTicket prolongedTicket = pawnTicketService.prolongPawnTicket(id, dto.getProlongUntil());
        return ResponseEntity.ok(prolongedTicket);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @pawnTicketService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<Void> deletePawnTicket(@PathVariable Long id) {
        pawnTicketService.deletePawnTicket(id);
        return ResponseEntity.noContent().build();
    }
}