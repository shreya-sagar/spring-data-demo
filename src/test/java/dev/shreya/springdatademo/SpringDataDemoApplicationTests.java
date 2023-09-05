package dev.shreya.springdatademo;

import static org.assertj.core.api.Assertions.assertThat;
import dev.shreya.springdatademo.entity.Flight;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringDataDemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
