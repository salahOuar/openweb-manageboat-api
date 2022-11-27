package com.openweb.api.boat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.openweb.api.boat.domain.Boat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoatDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;

    @NotBlank
    private String name;


    @NotBlank
    private String description;

    public BoatDTO(Boat boat) {
        this.id = boat.getId();
        this.name = boat.getName();
        this.description = boat.getDescription();
    }
}
