package ru.practicum.ewm.mainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.mainservice.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Location> {
}