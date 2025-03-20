package com.example.mappers;

import com.example.dto.AdresseDto;
import com.example.model.Adresse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AdresseMapper {

    AdresseMapper INSTANCE = Mappers.getMapper(AdresseMapper.class);

    AdresseDto toDto(Adresse adresse);
    Adresse toEntity(AdresseDto adresseDto);

    @Mapping(target = "id", ignore = true) // Si vous voulez ignorer les mises à jour de l'ID
    void updateAdresseFromDto(AdresseDto adresseDto, @MappingTarget Adresse adresse);
}
