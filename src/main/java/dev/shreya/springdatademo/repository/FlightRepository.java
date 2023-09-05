package dev.shreya.springdatademo.repository;

import dev.shreya.springdatademo.entity.Flight;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface FlightRepository extends CrudRepository<Flight, Long> {

  List<Flight> findByOrigin(String origin);
  List<Flight> findByOriginIn(String... origin);
  List<Flight> findByOriginInIgnoreCase(String... origin);
  List<Flight> findByOriginAndDestination(String origin, String destination);


}
