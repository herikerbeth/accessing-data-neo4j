package demo.repository;

import demo.TestData;
import demo.model.Person;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PersonRepositoryIT {

    static Neo4jContainer<?> neo4jContainer = new Neo4jContainer<>("neo4j:latest")
            .withAdminPassword("password");

    @BeforeAll
    static void beforeAll() {
        neo4jContainer.start();
    }

    @AfterAll
    static void afterAll() {
        neo4jContainer.stop();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", neo4jContainer::getBoltUrl);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", neo4jContainer::getAdminPassword);
    }

    @Autowired
    private PersonRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        Person person = TestData.person();
        Person person1 = TestData.person1();
        Person person2 = TestData.person2();

        repository.save(person);
        repository.save(person1);
        repository.save(person2);

        person.worksWith(person1);
        person.worksWith(person2);
        person1.worksWith(person2);

        repository.save(person);
        repository.save(person1);
    }

    @Test
    void shouldFindByName() {
        Person personResult = repository.findByName(TestData.person().getName());

        assertThat(personResult).isNotNull();
        assertThat(personResult.getName()).isEqualTo(TestData.person().getName());
    }

    @Test
    void shouldFindByTeammatesName() {
        List<Person> teammates = repository.findByTeammatesName(TestData.person().getName());

        assertThat(teammates).isNotEmpty();
        assertThat(teammates).hasSize(2);
        assertThat(teammates).extracting(Person::getName).containsExactlyInAnyOrder(
                TestData.person1().getName(),
                TestData.person2().getName()
        );
    }
}
