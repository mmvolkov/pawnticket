package ru.softline.pawnticket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.softline.pawnticket.dto.CreatePawnTicketDto;
import ru.softline.pawnticket.dto.PawnTicketResponseDto;
import ru.softline.pawnticket.dto.ProlongPawnTicketDto;
import ru.softline.pawnticket.service.PawnTicketService;

import java.util.List;

@RestController
@RequestMapping("/api/pawn-tickets")
@RequiredArgsConstructor
public class PawnTicketController {

    private final PawnTicketService pawnTicketService;

    @PostMapping
    public ResponseEntity<PawnTicketResponseDto> createPawnTicket(
            @Valid @RequestBody CreatePawnTicketDto dto) {

        PawnTicketResponseDto created = pawnTicketService.createPawnTicket(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<PawnTicketResponseDto>> getAllPawnTickets() {
        return ResponseEntity.ok(pawnTicketService.getAllPawnTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PawnTicketResponseDto> getPawnTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(pawnTicketService.getPawnTicketById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PawnTicketResponseDto>> getPawnTicketsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(pawnTicketService.getPawnTicketsByUserId(userId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @pawnTicketService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<PawnTicketResponseDto> updatePawnTicket(
            @PathVariable Long id,
            @Valid @RequestBody CreatePawnTicketDto dto) {

        return ResponseEntity.ok(pawnTicketService.updatePawnTicket(id, dto));
    }

    @PutMapping("/{id}/prolong")
    @PreAuthorize("hasRole('ADMIN') or @pawnTicketService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<PawnTicketResponseDto> prolongPawnTicket(
            @PathVariable Long id,
            @Valid @RequestBody ProlongPawnTicketDto dto) {

        return ResponseEntity.ok(pawnTicketService.prolongPawnTicket(id, dto.getProlongUntil()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @pawnTicketService.isOwner(#id, authentication.principal.id)")
    public ResponseEntity<Void> deletePawnTicket(@PathVariable Long id) {
        pawnTicketService.deletePawnTicket(id);
        return ResponseEntity.noContent().build();
    }
}
