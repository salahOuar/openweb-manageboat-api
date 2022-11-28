package com.openweb.api.boat.http;


import com.openweb.api.boat.domain.Boat;
import com.openweb.api.boat.dto.BoatDTO;
import com.openweb.api.boat.guard.OpenWebGuardBoat;
import com.openweb.api.boat.service.BoatService;
import com.openweb.api.utils.Utils;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boats")
@Slf4j
public class BoatResource {

    private final BoatService boatService;

    BoatResource(BoatService boatService) {
        this.boatService = boatService;
    }


    @RateLimiter(name = "rateLimiterApi")
    @Operation(summary = "retrieve user's boats")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully Retrieve user's boats", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BoatDTO.class))}), @ApiResponse(responseCode = "400", description = "Invalid boat data", content = @Content)})
    @GetMapping("")
    public ResponseEntity<List<BoatDTO>> all() {
        List<BoatDTO> boats = boatService.findAllUserBoats();
        return new ResponseEntity<>(boats, HttpStatus.OK);
    }

    @RateLimiter(name = "rateLimiterApi")
    @Operation(summary = "Creation of a new boat")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "The boat was successfully created", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BoatDTO.class))}), @ApiResponse(responseCode = "400", description = "Invalid boat data", content = @Content)})
    @PostMapping("")
    public ResponseEntity<BoatDTO> newBoat(@Validated @RequestBody BoatDTO boatDTO) {
        Boat boat = boatService.save(boatDTO);
        return new ResponseEntity<>(Utils.buildBoatDTO(boat), HttpStatus.CREATED);
    }

    @RateLimiter(name = "rateLimiterApi")
    @Operation(summary = "Get a boat by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found the boat", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BoatDTO.class))}), @ApiResponse(responseCode = "400", description = "Invalid boat data", content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<BoatDTO> findById(@Parameter(description = "id of boat to be searched") @PathVariable("id") long boatId) {
        BoatDTO boatDTO = boatService.find(boatId);
        return new ResponseEntity<>(boatDTO, HttpStatus.OK);
    }

    @OpenWebGuardBoat
    @RateLimiter(name = "rateLimiterApi")
    @Operation(summary = "Update a boat")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "The boat was successfully updated", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BoatDTO.class))}), @ApiResponse(responseCode = "400", description = "Invalid boat data", content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<BoatDTO> updateBoat(@Validated @RequestBody BoatDTO newBoat, @PathVariable("id") long boatId) {
        Boat boat = boatService.update(newBoat, boatId);
        return new ResponseEntity<>(Utils.buildBoatDTO(boat), HttpStatus.OK);
    }

    @OpenWebGuardBoat
    @RateLimiter(name = "rateLimiterApi")
    @Operation(summary = "Delete a boat")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "The boat was successfully deleted", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BoatDTO.class))})})
    @DeleteMapping("/{id}")
    public ResponseEntity deleteBoat(@PathVariable("id") long boatId) {
        boatService.delete(boatId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}