package com.example.mapper;

import com.example.entity.User;
import com.example.entity.dto.ContactInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContactMapper {
    ContactInfoDto userToContactDTO(User user);
}
