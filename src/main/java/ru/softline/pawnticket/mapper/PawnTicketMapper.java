package ru.softline.pawnticket.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.softline.pawnticket.dto.CreatePawnTicketDto;
import ru.softline.pawnticket.dto.PawnTicketResponseDto;
import ru.softline.pawnticket.entity.PawnTicket;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE   // игнорируем всё, что не описано явно
)
public interface PawnTicketMapper {

    @Mapping(source = "user.id",       target = "userId")
    @Mapping(source = "user.username", target = "username")
    PawnTicketResponseDto toDto(PawnTicket entity);

    @Mapping(target = "id",             ignore = true)
    @Mapping(target = "user",           ignore = true)
    @Mapping(target = "status",         ignore = true)
    @Mapping(target = "createdAt",      ignore = true)
    @Mapping(target = "updatedAt",      ignore = true)
    @Mapping(target = "prolongedUntil", ignore = true)
    PawnTicket toEntity(CreatePawnTicketDto dto);

    @Mapping(target = "id",             ignore = true)
    @Mapping(target = "user",           ignore = true)
    @Mapping(target = "status",         ignore = true)
    @Mapping(target = "createdAt",      ignore = true)
    @Mapping(target = "updatedAt",      ignore = true)
    @Mapping(target = "prolongedUntil", ignore = true)
    @Mapping(target = "ticketNumber",   ignore = true)
    void updateEntity(@MappingTarget PawnTicket entity, CreatePawnTicketDto dto);
}
