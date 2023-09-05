package dev.shreya.springdatademo.demos;

import static org.assertj.core.api.Assertions.assertThat;

import dev.shreya.springdatademo.entity.Flight;
import dev.shreya.springdatademo.repository.FlightRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DerivedQueryTests {

  @Autowired
  FlightRepository flightRepository;

  @BeforeEach
  public void setup() {
    flightRepository.deleteAll();
  }

  @Test
  public void findFlightsFromParis() {
    final Flight flight1 = createFlight("Paris","Delhi");
    final Flight flight2 = createFlight("New York","Delhi");
    final Flight flight3 = createFlight("Paris","Delhi");
    final Flight flight4 = createFlight("Paris","Delhi");

    List<Flight> flightList = new ArrayList<>();
    flightList.add(flight1);
    flightList.add(flight2);
    flightList.add(flight3);
    flightList.add(flight4);

    flightRepository.saveAll(flightList);

    List<Flight> flights = flightRepository.findByOrigin("Paris");
    assertThat(flights).hasSize(3);
    assertThat(flights.get(0)).usingRecursiveComparison().isEqualTo(flight1);
    assertThat(flights.get(1)).isEqualTo(flight3);
  }

  @Test
  public void findFlightsFromParisToDelhi() {
    final Flight flight1 = createFlight("Paris","Delhi");
    final Flight flight2 = createFlight("New York","Delhi");
    final Flight flight3 = createFlight("Paris","Delhi");
    final Flight flight4 = createFlight("Paris","Delhi");

    List<Flight> flightList = new ArrayList<>();
    flightList.add(flight1);
    flightList.add(flight2);
    flightList.add(flight3);
    flightList.add(flight4);

    flightRepository.saveAll(flightList);

    List<Flight> flights = flightRepository.findByOriginAndDestination("Paris","Delhi");
    assertThat(flights).hasSize(3);
    assertThat(flights.get(0)).usingRecursiveComparison().isEqualTo(flight1);
    assertThat(flights.get(1)).isEqualTo(flight3);
    assertThat(flights.get(2)).isEqualTo(flight4);
  }

  @Test
  public void findFlightsFromParisOrNewYork() {
    final Flight flight1 = createFlightFrom("Paris");
    final Flight flight2 = createFlightFrom("New York");
    final Flight flight3 = createFlightFrom("Paris");
    final Flight flight4 = createFlightFrom("New York");
    final Flight flight5 = createFlightFrom("Delhi");

    List<Flight> flightList = new ArrayList<>();
    flightList.add(flight1);
    flightList.add(flight2);
    flightList.add(flight3);
    flightList.add(flight4);
    flightList.add(flight5);

    flightRepository.saveAll(flightList);

    List<Flight> flights = flightRepository.findByOriginIn("Paris","New York");
    assertThat(flights).hasSize(4);
    assertThat(flights.get(0)).usingRecursiveComparison().isEqualTo(flight1);
    assertThat(flights.get(1)).isEqualTo(flight2);
    assertThat(flights.get(2)).isEqualTo(flight3);
  }

  @Test
  public void findFlightsFromParisOrNewYorkIgnoreCase() {
    final Flight flight1 = createFlightFrom("Paris");
    final Flight flight2 = createFlightFrom("new york");
    final Flight flight3 = createFlightFrom("paris");
    final Flight flight4 = createFlightFrom("New York");
    final Flight flight5 = createFlightFrom("Delhi");

    List<Flight> flightList = new ArrayList<>();
    flightList.add(flight1);
    flightList.add(flight2);
    flightList.add(flight3);
    flightList.add(flight4);
    flightList.add(flight5);

    flightRepository.saveAll(flightList);

    List<Flight> flights = flightRepository.findByOriginInIgnoreCase("Paris","New York");
    assertThat(flights).hasSize(4);
    assertThat(flights.get(0)).usingRecursiveComparison().isEqualTo(flight1);
    assertThat(flights.get(1)).isEqualTo(flight2);
    assertThat(flights.get(2)).isEqualTo(flight3);
  }

  private Flight createFlightFrom(String origin) {
    return createFlight(origin, "Madrid");
  }

  private static Flight createFlight(String origin, String destination) {
    final Flight flight = new Flight();
    flight.setOrigin(origin);
    flight.setDestination(destination);
    flight.setScheduledAt(LocalDateTime.parse("2023-09-05T10:35:00"));
    return flight;
  }

}
