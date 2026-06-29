package com.eagle_bank_api.mapper;

import com.eagle_bank_api.model.dto.CreateTransactionRequestDto;
import com.eagle_bank_api.model.dto.TransactionResponseDto;
import com.eagle_bank_api.model.entity.Transaction;
import com.eagle_bank_api.model.entity.TransactionType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "type",
            expression = "java(com.eagle_bank_api.model.entity.TransactionType.valueOf(request.getType().toUpperCase()))")
    Transaction toEntity(CreateTransactionRequestDto request);

    @Mapping(target = "type", expression = "java(tx.getType().name().toLowerCase())")
    @Mapping(target = "userId", source = "user.id")
    TransactionResponseDto toResponse(Transaction tx);
}