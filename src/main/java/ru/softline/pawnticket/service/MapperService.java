package ru.softline.pawnticket.service;

import org.springframework.stereotype.Service;
import ru.softline.pawnticket.dto.PawnTicketResponseDto;
import ru.softline.pawnticket.dto.UserResponseDto;
import ru.softline.pawnticket.entity.PawnTicket;
import ru.softline.pawnticket.entity.User;

@Service
public class MapperService {

    public UserResponseDto toUserResponseDto(User user) {
        if (user == null) {
            return null;
        }

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public PawnTicketResponseDto toPawnTicketResponseDto(PawnTicket ticket) {
        if (ticket == null) {
            return null;
        }

        return PawnTicketResponseDto.builder()
                .id(ticket.getId())
                .ticketNumber(ticket.getTicketNumber())
                .userId(ticket.getUser() != null ? ticket.getUser().getId() : null)
                .username(ticket.getUser() != null ? ticket.getUser().getUsername() : null)
                .clientName(ticket.getClientName())
                .itemDescription(ticket.getItemDescription())
                .loanAmount(ticket.getLoanAmount())
                .interestRate(ticket.getInterestRate())
                .issueDate(ticket.getIssueDate())
                .expiryDate(ticket.getExpiryDate())
                .prolongedUntil(ticket.getProlongedUntil())
                .status(ticket.getStatus())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}