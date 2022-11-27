package com.openweb.api.boat.service.impl;

import com.openweb.api.boat.domain.Boat;
import com.openweb.api.boat.dto.BoatDTO;
import com.openweb.api.boat.exception.BoatNotFoundException;
import com.openweb.api.boat.repository.BoatRepository;
import com.openweb.api.boat.service.BoatService;
import com.openweb.api.security.domain.User;
import com.openweb.api.security.service.UserService;
import com.openweb.api.utils.Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoatServiceImpl implements BoatService {


    private final BoatRepository boatRepository;

    /**
     *
     */
    private final UserService userService;

    public BoatServiceImpl(BoatRepository boatRepository, UserService userService) {
        this.boatRepository = boatRepository;
        this.userService = userService;
    }

    /**
     * @return
     */
    @Override
    public List<BoatDTO> findAllUserBoats() {
        User user = getAuthenticatedUser();
        return boatRepository.findAllUserBoats(user.getId()).stream().map(BoatDTO::new).collect(Collectors.toList());
    }

    /**
     * @param boatId
     * @return
     */
    @Override
    public BoatDTO find(Long boatId) {
        User user = getAuthenticatedUser();
        return boatRepository.findUserBoatById(boatId, user.getId()).map(BoatDTO::new).orElseThrow(() -> new BoatNotFoundException("Boat with id " + boatId + " was not found"));
    }

    /**
     * @param boatDTO
     * @return
     */
    @Override
    @Transactional
    public Boat save(BoatDTO boatDTO) {
        User authenticatedUser = this.getAuthenticatedUser();
        Boat newBoat = Utils.buildNewBoat(boatDTO, authenticatedUser);
        return boatRepository.save(newBoat);
    }

    /**
     * @param boatId
     */
    @Override
    @Transactional
    public void delete(Long boatId) {
        User user = getAuthenticatedUser();
        Boat boat = boatRepository.findUserBoatById(boatId, user.getId()).orElseThrow(() -> new BoatNotFoundException("Boat with id " + boatId + " was not found"));
        boatRepository.deleteById(boat.getId());
    }

    /**
     * @param newBoat
     * @param boatId
     * @return
     */
    @Override
    @Transactional
    public Boat update(BoatDTO newBoat, Long boatId) {
        User user = getAuthenticatedUser();
        Boat boat = boatRepository.findUserBoatById(boatId, user.getId()).orElseThrow(() -> new BoatNotFoundException("Boat with id " + boatId + " was not found"));
        boat.setName(newBoat.getName());
        boat.setDescription(newBoat.getDescription());
        return this.boatRepository.save(boat);
    }

    /**
     * @return
     */
    private User getAuthenticatedUser() {
        return userService.getUser();
    }
}
