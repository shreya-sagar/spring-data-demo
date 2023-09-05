
package dev.shreya.springdatademo.repository;

import dev.shreya.springdatademo.entity.Flight;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FlightRepository extends CrudRepository<Flight, Long>, PagingAndSortingRepository<Flight, Long> {

  List<Flight> findByOrigin(String origin);
  Page<Flight> findByOriginIgnoreCase(String origin, Pageable pageable);
  List<Flight> findByOriginIn(String... origin);
  List<Flight> findByOriginInIgnoreCase(String... origin);
  List<Flight> findByOriginAndDestination(String origin, String destination);
  List<Flight> findByDestination(String destination);
}

