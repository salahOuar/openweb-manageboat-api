package com.openweb.api.boat.repository;

import com.openweb.api.boat.domain.Boat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoatRepository extends JpaRepository<Boat, Long> {


    @Query(value = "SELECT b FROM Boat b WHERE b.user.id = :userId")
    List<Boat> findAllUserBoats(Long userId);

    @Query(value = "SELECT b FROM Boat b WHERE b.id = :boatId and b.user.id = :userId")
    Optional<Boat> findUserBoatById(Long boatId, Long userId);
}
