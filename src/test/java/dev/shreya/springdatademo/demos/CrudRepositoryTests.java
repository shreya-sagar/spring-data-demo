package dev.shreya.springdatademo.demos;

import static org.assertj.core.api.Assertions.assertThat;

import dev.shreya.springdatademo.entity.Flight;
import dev.shreya.springdatademo.repository.FlightRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CrudRepositoryTests {

  @Autowired
  FlightRepository flightRepository;

  @Test
  public void verifyFlightCanBeSavedWithCrudRepo() {
    final Flight flight = new Flight("kolkata","delhi",
        LocalDateTime.parse("2023-09-04T20:11:00"));

    flightRepository.save(flight);
    assertThat(flightRepository.findAll()).hasSize(1).first().isEqualTo(flight);

    flightRepository.deleteById(flight.getId());
    assertThat(flightRepository.count()).isZero();
  }
}
