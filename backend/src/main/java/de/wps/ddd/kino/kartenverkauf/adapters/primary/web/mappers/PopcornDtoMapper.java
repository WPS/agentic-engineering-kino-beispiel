package de.wps.ddd.kino.kartenverkauf.adapters.primary.web.mappers;

import de.wps.ddd.kino.kartenverkauf.adapters.primary.web.model.PopcornPortionDto;
import de.wps.ddd.kino.kartenverkauf.application.domain.kartenverkauf.PopcornGeschmack;
import de.wps.ddd.kino.kartenverkauf.application.domain.kartenverkauf.PopcornGroesse;
import de.wps.ddd.kino.kartenverkauf.application.domain.kartenverkauf.PopcornPortion;
import de.wps.ddd.kino.kartenverkauf.application.domain.kartenverkauf.Popcornbestellung;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PopcornDtoMapper {

    public Popcornbestellung toDomain(List<PopcornPortionDto> popcorn) {
        if (popcorn == null || popcorn.isEmpty()) {
            return Popcornbestellung.leer();
        }
        return new Popcornbestellung(popcorn.stream().map(this::toDomain).toList());
    }

    private PopcornPortion toDomain(PopcornPortionDto dto) {
        return new PopcornPortion(
                PopcornGroesse.valueOf(dto.groesse()),
                PopcornGeschmack.valueOf(dto.geschmack()));
    }
}
