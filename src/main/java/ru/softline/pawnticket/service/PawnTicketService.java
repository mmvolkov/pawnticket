package ru.softline.pawnticket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softline.pawnticket.dto.CreatePawnTicketDto;
import ru.softline.pawnticket.entity.PawnTicket;
import ru.softline.pawnticket.entity.User;
import ru.softline.pawnticket.repository.PawnTicketRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PawnTicketService {

    private final PawnTicketRepository pawnTicketRepository;
    private final UserService userService;

    @Transactional
    public PawnTicket createPawnTicket(CreatePawnTicketDto dto) {
        User currentUser = userService.getCurrentUser();

        String clientName = currentUser.getFirstName() + " " + currentUser.getLastName();
        if (clientName.trim().isEmpty() || clientName.equals(" ")) {
            clientName = currentUser.getUsername();
        }

        PawnTicket pawnTicket = PawnTicket.builder()
                .ticketNumber(generateTicketNumber())
                .user(currentUser)
                .clientName(clientName)
                .itemDescription(dto.getItemDescription())
                .loanAmount(dto.getLoanAmount())
                .interestRate(dto.getInterestRate())
                .expiryDate(dto.getExpiryDate())
                .status(PawnTicket.Status.ACTIVE)
                .build();

        return pawnTicketRepository.save(pawnTicket);
    }

    public List<PawnTicket> getAllPawnTickets() {
        return pawnTicketRepository.findAll();
    }

    public PawnTicket getPawnTicketById(Long id) {
        return pawnTicketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pawn ticket not found with id: " + id));
    }

    public List<PawnTicket> getPawnTicketsByUserId(Long userId) {
        return pawnTicketRepository.findByUserId(userId);
    }

    @Transactional
    public PawnTicket updatePawnTicket(Long id, CreatePawnTicketDto dto) {
        PawnTicket pawnTicket = getPawnTicketById(id);

        pawnTicket.setItemDescription(dto.getItemDescription());
        pawnTicket.setLoanAmount(dto.getLoanAmount());
        pawnTicket.setInterestRate(dto.getInterestRate());
        pawnTicket.setExpiryDate(dto.getExpiryDate());

        // Обновляем clientName если пользователь изменил свои данные
        User user = pawnTicket.getUser();
        String clientName = user.getFirstName() + " " + user.getLastName();
        if (clientName.trim().isEmpty() || clientName.equals(" ")) {
            clientName = user.getUsername();
        }
        pawnTicket.setClientName(clientName);

        return pawnTicketRepository.save(pawnTicket);
    }

    @Transactional
    public PawnTicket prolongPawnTicket(Long id, LocalDateTime prolongUntil) {
        PawnTicket pawnTicket = getPawnTicketById(id);

        if (pawnTicket.getStatus() != PawnTicket.Status.ACTIVE &&
                pawnTicket.getStatus() != PawnTicket.Status.PROLONGED) {
            throw new RuntimeException("Cannot prolong ticket with status: " + pawnTicket.getStatus());
        }

        if (prolongUntil.isBefore(pawnTicket.getExpiryDate())) {
            throw new RuntimeException("Prolongation date must be after current expiry date");
        }

        pawnTicket.setProlongedUntil(prolongUntil);
        pawnTicket.setExpiryDate(prolongUntil);
        pawnTicket.setStatus(PawnTicket.Status.PROLONGED);

        return pawnTicketRepository.save(pawnTicket);
    }

    @Transactional
    public void deletePawnTicket(Long id) {
        if (!pawnTicketRepository.existsById(id)) {
            throw new RuntimeException("Pawn ticket not found with id: " + id);
        }
        pawnTicketRepository.deleteById(id);
    }

    public boolean isOwner(Long ticketId, Long userId) {
        PawnTicket pawnTicket = getPawnTicketById(ticketId);
        return pawnTicket.getUser().getId().equals(userId);
    }

    private String generateTicketNumber() {
        return "PT-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}