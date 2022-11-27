package com.openweb.api.boat.service;

import com.openweb.api.boat.domain.Boat;
import com.openweb.api.boat.dto.BoatDTO;
import com.openweb.api.boat.exception.BoatNotFoundException;
import com.openweb.api.boat.repository.BoatRepository;
import com.openweb.api.boat.service.impl.BoatServiceImpl;
import com.openweb.api.security.domain.User;
import com.openweb.api.security.service.UserService;
import com.openweb.api.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;


@MockitoSettings(strictness = Strictness.LENIENT)
public class BoatServiceTests {


    @InjectMocks
    private BoatServiceImpl boatService;
    @Mock
    private BoatRepository mockBoatRepository;
    @Mock
    private UserService mockUserService;
    private Boat expectedBoat;
    private User connectedUser;

    @BeforeEach
    public void setUp() {
        connectedUser = new User();
        connectedUser.setId(Long.valueOf(1));
        expectedBoat = Boat.builder().name("L'Hermione").description("L'Hermione, mythique frégate du 18e siècle de La Fayette").id(Long.valueOf(1)).user(connectedUser).build();
        Mockito.when(mockUserService.getUser()).thenReturn(connectedUser);

    }


    @Test
    public void findAllUserBoats() {
        Mockito.when(mockBoatRepository.findAllUserBoats(connectedUser.getId())).thenReturn(Collections.singletonList(expectedBoat));
        List<BoatDTO> boats = boatService.findAllUserBoats();
        assertThat(boats.size()).isEqualTo(1);
        assertThat(boats.get(0).getName()).isEqualTo(expectedBoat.getName());
        assertThat(boats.get(0).getDescription()).isEqualTo(expectedBoat.getDescription());
    }

    @Test
    public void findById() {
        Mockito.when(mockBoatRepository.findUserBoatById(expectedBoat.getId(), connectedUser.getId())).thenReturn(Optional.of(expectedBoat));
        BoatDTO boatDTO = boatService.find(expectedBoat.getId());
        assertThat(boatDTO).isNotNull();
        assertThat(boatDTO.getName()).isEqualTo(expectedBoat.getName());
        assertThat(boatDTO.getDescription()).isEqualTo(expectedBoat.getDescription());
    }

    @Test
    public void findByIdWhenBoatNotFoundThenReturnException() {
        Mockito.when(mockBoatRepository.findUserBoatById(expectedBoat.getId(), connectedUser.getId())).thenReturn(Optional.of(expectedBoat));
        BoatNotFoundException exception = Assertions.assertThrows(BoatNotFoundException.class, () -> boatService.find(Long.valueOf(17)));
        Assertions.assertEquals("Boat with id 17 was not found", exception.getMessage());

    }

    /**
     *
     */
    @Test
    public void deleteById() {
        Mockito.when(mockBoatRepository.findUserBoatById(expectedBoat.getId(), connectedUser.getId())).thenReturn(Optional.of(expectedBoat));
        boatService.delete(expectedBoat.getId());
        verify(mockBoatRepository, atLeastOnce()).deleteById(expectedBoat.getId());
    }

    /**
     *
     */
    @Test
    public void createBoat() {
        expectedBoat.setId(null);
        BoatDTO boatDTO = Utils.buildBoatDTO(expectedBoat);
        boatService.save(boatDTO);
        verify(mockBoatRepository, atLeastOnce()).save(expectedBoat);
    }

    /**
     *
     */
    @Test
    public void updateBoat() {
        Mockito.when(mockBoatRepository.findUserBoatById(expectedBoat.getId(), connectedUser.getId())).thenReturn(Optional.of(expectedBoat));
        BoatDTO boatDTO = Utils.buildBoatDTO(expectedBoat);
        boatService.update(boatDTO, expectedBoat.getId());
        verify(mockBoatRepository, atLeastOnce()).save(expectedBoat);
    }

    /**
     *
     */
    @Test
    public void updateBoatWhenBoatNotFoundThenReturnException() {
        Mockito.when(mockBoatRepository.findById(expectedBoat.getId())).thenReturn(Optional.of(expectedBoat));
        BoatDTO boatDTO = Utils.buildBoatDTO(expectedBoat);
        BoatNotFoundException exception = Assertions.assertThrows(BoatNotFoundException.class, () -> boatService.update(boatDTO, Long.valueOf(17)));
        Assertions.assertEquals("Boat with id 17 was not found", exception.getMessage());
    }
}
