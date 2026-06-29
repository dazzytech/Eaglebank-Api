package com.eagle_bank_api.mapper;

import com.eagle_bank_api.model.dto.BankAccountResponseDto;
import com.eagle_bank_api.model.dto.CreateBankAccountRequestDto;
import com.eagle_bank_api.model.entity.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "sortCode", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "updatedTimestamp", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(
            target = "accountType",
            expression = "java(com.eagle_bank_api.model.entity.AccountType.PERSONAL)"
    )
    BankAccount toEntity(CreateBankAccountRequestDto request);

    BankAccountResponseDto toResponse(BankAccount account);
}