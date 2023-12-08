package com.utc.formut.web.dto;

import com.utc.formut.web.models.Journey;

import java.util.ArrayList;
import java.util.List;

public class ListJourneysDTO {
    private List<JourneyDTO> journeyDTO = new ArrayList<>();

    public ListJourneysDTO(List<JourneyDTO> journeyDTO) {
        this.journeyDTO = journeyDTO;
    }

    public ListJourneysDTO() {}

    public List<JourneyDTO> getjourneyDTO() {
        return journeyDTO;
    }

    public void setJourneys(List<JourneyDTO> journeyDTO) {
        this.journeyDTO.addAll(journeyDTO);
    }
}
