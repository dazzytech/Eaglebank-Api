package com.eagle_bank_api.mapper;

import com.eagle_bank_api.model.dto.*;
import com.eagle_bank_api.model.entity.Address;
import com.eagle_bank_api.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTimestamp", ignore = true)
    @Mapping(target = "updatedTimestamp", ignore = true)
    @Mapping(target = "address", source = "address")
    User toEntity(CreateUserRequestDto request);

    Address toEntity(AddressDto dto);

    AddressDto toDto(Address address);

    @Mapping(target = "address", source = "address")
    UserResponseDto toResponse(User user);
}