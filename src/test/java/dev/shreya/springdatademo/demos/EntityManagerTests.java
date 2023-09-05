
package dev.shreya.springdatademo.demos;

import static org.assertj.core.api.Assertions.assertThat;

import dev.shreya.springdatademo.entity.Flight;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class EntityManagerTests {

  @Autowired
  private EntityManager entityManager;

  @Test
  public void verifyFlightCanBeSaved() {
    final Flight flight = new Flight("kolkata","delhi",
        LocalDateTime.parse("2023-09-04T20:11:00"));
    entityManager.persist(flight);
    final TypedQuery<Flight> flightTypedQuery = entityManager.createQuery("Select f from Flight f", Flight.class);
    final List<Flight> flights = flightTypedQuery.getResultList();

    assertThat(flights).hasSize(1).first().isEqualTo(flight);
  }

}

