package ru.softline.pawnticket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.softline.pawnticket.dto.CreatePawnTicketDto;
import ru.softline.pawnticket.dto.PawnTicketResponseDto;
import ru.softline.pawnticket.entity.PawnTicket;
import ru.softline.pawnticket.entity.User;
import ru.softline.pawnticket.mapper.PawnTicketMapper;
import ru.softline.pawnticket.repository.PawnTicketRepository;
import ru.softline.pawnticket.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class PawnTicketService {

    private static final DateTimeFormatter TICKET_FMT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final PawnTicketRepository pawnTicketRepository;
    private final UserRepository       userRepository;
    private final PawnTicketMapper     mapper;

    public PawnTicketResponseDto createPawnTicket(CreatePawnTicketDto dto) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        PawnTicket entity = mapper.toEntity(dto);

        if (entity.getIssueDate() == null) {
            entity.setIssueDate(LocalDateTime.now());
        }
        if (entity.getTicketNumber() == null || entity.getTicketNumber().isBlank()) {
            entity.setTicketNumber("PT-" + LocalDateTime.now().format(TICKET_FMT));
        }

        entity.setUser(owner);
        entity.setStatus(PawnTicket.Status.ACTIVE);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        return mapper.toDto(pawnTicketRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<PawnTicketResponseDto> getAllPawnTickets() {
        return pawnTicketRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PawnTicketResponseDto getPawnTicketById(Long id) {
        return mapper.toDto(findById(id));
    }

    @Transactional(readOnly = true)
    public List<PawnTicketResponseDto> getPawnTicketsByUserId(Long userId) {
        return pawnTicketRepository.findByUserId(userId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public PawnTicketResponseDto updatePawnTicket(Long id, CreatePawnTicketDto dto) {
        PawnTicket entity = findById(id);
        mapper.updateEntity(entity, dto);
        entity.setUpdatedAt(LocalDateTime.now());
        return mapper.toDto(pawnTicketRepository.save(entity));
    }

    public PawnTicketResponseDto prolongPawnTicket(Long id, LocalDateTime prolongUntil) {
        PawnTicket entity = findById(id);

        if (prolongUntil.isBefore(entity.getExpiryDate())) {
            throw new IllegalArgumentException(
                    "Prolongation date must be after current expiry date");
        }

        entity.setProlongedUntil(prolongUntil);
        entity.setStatus(PawnTicket.Status.ACTIVE);
        entity.setUpdatedAt(LocalDateTime.now());

        return mapper.toDto(pawnTicketRepository.save(entity));
    }

    public void deletePawnTicket(Long id) {
        pawnTicketRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean isOwner(Long ticketId, Long principalUserId) {
        return pawnTicketRepository.existsByIdAndUserId(ticketId, principalUserId);
    }

    private PawnTicket findById(Long id) {
        return pawnTicketRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, "Pawn ticket not found (id=" + id + ')'));
    }
}
