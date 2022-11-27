package com.openweb.api.boat.service;

import com.openweb.api.boat.domain.Boat;
import com.openweb.api.boat.dto.BoatDTO;

import java.util.List;

public interface BoatService {


    /**
     * @return
     */
    List<BoatDTO> findAllUserBoats();

    /**
     * @param boatId
     * @return
     */
    BoatDTO find(Long boatId);

    /**
     * @param boatDTO
     * @return
     */
    Boat save(BoatDTO boatDTO);

    /**
     * @param boatId
     */
    void delete(Long boatId);

    /**
     * @param newBoat
     * @param id
     * @return
     */
    Boat update(BoatDTO newBoat, Long id);
}
