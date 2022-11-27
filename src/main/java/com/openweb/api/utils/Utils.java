package com.openweb.api.utils;

import com.openweb.api.boat.domain.Boat;
import com.openweb.api.boat.dto.BoatDTO;
import com.openweb.api.security.domain.User;

public class Utils {


    private Utils() {
    }

    /**
     * @param boat
     * @return
     */
    public static BoatDTO buildBoatDTO(Boat boat) {
        return BoatDTO.builder().name(boat.getName()).description(boat.getDescription()).id(boat.getId()).build();
    }

    /**
     * @param newBoat
     * @param authenticatedUser
     * @return
     */
    public static Boat buildNewBoat(BoatDTO newBoat, User authenticatedUser) {
        return Boat.builder().name(newBoat.getName()).description(newBoat.getDescription()).user(authenticatedUser).build();
    }
}
