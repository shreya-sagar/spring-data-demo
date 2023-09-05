package dev.shreya.springdatademo.demos;

import static org.assertj.core.api.Assertions.assertThat;

import dev.shreya.springdatademo.entity.Flight;
import dev.shreya.springdatademo.repository.FlightRepository;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@DataJpaTest
public class PagingAndSortingTests {
  @Autowired
  FlightRepository flightRepository;

  @BeforeEach
  public void setup() {
    flightRepository.deleteAll();
  }

  @Test
  public void shouldSortFlightsByDestinationDesc() {
    final Flight madrid = createFlight("Madrid");
    final Flight london = createFlight("London");
    final Flight paris = createFlight("Paris");

    flightRepository.save(madrid);
    flightRepository.save(london);
    flightRepository.save(paris);

    Iterable<Flight> flights = flightRepository.findAll(Sort.by(Direction.DESC,"destination"));
    Iterator<Flight> flightIterator = flights.iterator();
    assertThat(flightIterator.next()).isEqualTo(paris);
    assertThat(flightIterator.next()).isEqualTo(madrid);
    assertThat(flightIterator.next()).isEqualTo(london);
  }

  @Test
  public void shouldSortFlightsByDestinationThenScheduledAtDesc() {
    LocalDateTime now = LocalDateTime.now();
    final Flight madrid = createFlight("Madrid",now.plusHours(1));
    final Flight london = createFlight("London",now.plusMinutes(1));
    final Flight paris = createFlight("Paris",now.plusSeconds(1));
    final Flight parisPlus = createFlight("Paris",now.plusSeconds(2));
    final Flight delhi = createFlight("Delhi",now.plusDays(1));
    final Flight delhiMinus = createFlight("Delhi",now.minusDays(1));

    flightRepository.save(madrid);
    flightRepository.save(london);
    flightRepository.save(paris);
    flightRepository.save(parisPlus);
    flightRepository.save(delhi);
    flightRepository.save(delhiMinus);

    Iterable<Flight> flights = flightRepository.findAll(Sort.by(Direction.DESC,"destination","scheduledAt"));
    Iterator<Flight> flightIterator = flights.iterator();

    assertThat(flightIterator.next()).usingRecursiveComparison().isEqualTo(parisPlus);
    assertThat(flightIterator.next()).isEqualTo(paris);
    assertThat(flightIterator.next()).isEqualTo(madrid);
    assertThat(flightIterator.next()).isEqualTo(london);
    assertThat(flightIterator.next()).isEqualTo(delhi);
    assertThat(flightIterator.next()).isEqualTo(delhiMinus);
  }

  @Test
  public void shouldSortFlightsByScheduledAtThenDestinationDesc() {
    LocalDateTime now = LocalDateTime.now();
    final Flight paris = createFlight("Paris",now.plusSeconds(1));
    final Flight parisPlus = createFlight("Paris",now.plusSeconds(2));
    final Flight parisPlusDay = createFlight("Paris",now.plusDays(1));

    final Flight delhi = createFlight("Delhi",now.plusDays(1));
    final Flight delhiMinus = createFlight("Delhi",now.minusDays(1));

    flightRepository.save(paris);
    flightRepository.save(parisPlus);
    flightRepository.save(parisPlusDay);
    flightRepository.save(delhi);
    flightRepository.save(delhiMinus);

    Iterable<Flight> flights = flightRepository.findAll(Sort.by(Direction.DESC,"scheduledAt","destination"));
    Iterator<Flight> flightIterator = flights.iterator();

    assertThat(flightIterator.next()).usingRecursiveComparison().isEqualTo(parisPlusDay);
    assertThat(flightIterator.next()).isEqualTo(delhi);
    assertThat(flightIterator.next()).isEqualTo(parisPlus);
    assertThat(flightIterator.next()).isEqualTo(paris);
    assertThat(flightIterator.next()).isEqualTo(delhiMinus);
  }

  @Test
  public void shouldPageResults() {
    List<Flight> flights = IntStream.range(0,50)
        .mapToObj(i -> flightRepository.save(createFlight("delhi" + i)))
        .toList();
    Page<Flight> flightPage = flightRepository.findAll(PageRequest.of(2,5));
    assertThat(flightPage.getTotalElements()).isEqualTo(50);
    assertThat(flightPage.getTotalPages()).isEqualTo(10);
    assertThat(flightPage.getNumberOfElements()).isEqualTo(5);
    assertThat(flightPage.getContent())
        .extracting(Flight::getDestination)
        .containsExactly("delhi10","delhi11","delhi12","delhi13","delhi14");
  }

  @Test
  public void shouldPageAndSortResults() {
    List<Flight> flights = IntStream.range(0,50)
        .mapToObj(i -> flightRepository.save(createFlight("delhi" + i)))
        .toList();
    Page<Flight> flightPage = flightRepository.findAll(PageRequest.of(2,5,Sort.by(Direction.DESC,"destination")));
    assertThat(flightPage.getTotalElements()).isEqualTo(50);
    assertThat(flightPage.getTotalPages()).isEqualTo(10);
    assertThat(flightPage.getNumberOfElements()).isEqualTo(5);
    assertThat(flightPage.getContent())
        .extracting(Flight::getDestination)
        .containsExactly("delhi44","delhi43","delhi42","delhi41","delhi40");
  }

  @Test
  public void shouldPageAndSortADerivedQuery() {
    List<Flight> flightListfromLondon = IntStream.range(0,10)
        .mapToObj(i -> {
          Flight flight = createFlight("delhi" + i);
          flight.setOrigin("London");
          return flightRepository.save(flight);
        })
        .toList();

    List<Flight> flightListFromKolkata = IntStream.range(0,10)
        .mapToObj(i -> {
          Flight flight = createFlight("delhi" + i);
          flight.setOrigin("Kolkata");
          return flightRepository.save(flight);
        })
        .toList();

    Page<Flight> flightsFromLondon = flightRepository.findByOriginIgnoreCase("london",
        PageRequest.of(0,5,Sort.by(Direction.DESC,"destination")));

    Page<Flight> flightsFromKolkata = flightRepository.findByOriginIgnoreCase("kolkata",
        PageRequest.of(0,5,Sort.by(Direction.DESC,"destination")));

    assertThat(flightsFromLondon.getTotalElements()).isEqualTo(10);
    assertThat(flightsFromLondon.getTotalPages()).isEqualTo(2);
    assertThat(flightsFromLondon.getNumberOfElements()).isEqualTo(5);
    assertThat(flightsFromLondon.getContent())
        .extracting(Flight::getDestination)
        .containsExactly("delhi9","delhi8","delhi7","delhi6","delhi5");

    assertThat(flightsFromKolkata.getTotalElements()).isEqualTo(10);
    assertThat(flightsFromKolkata.getTotalPages()).isEqualTo(2);
    assertThat(flightsFromKolkata.getNumberOfElements()).isEqualTo(5);
    assertThat(flightsFromKolkata.getContent())
        .extracting(Flight::getDestination)
        .containsExactly("delhi9","delhi8","delhi7","delhi6","delhi5");
  }

  private Flight createFlight(String destination, LocalDateTime scheduledAt) {
    Flight flight = new Flight();
    flight.setDestination(destination);
    flight.setOrigin("London");
    flight.setScheduledAt(scheduledAt);
    return flight;
  }

  private Flight createFlight(String destination) {
    return createFlight(destination, LocalDateTime.parse("2011-12-13T12:12:00"));
  }
}
