package demo.repository;

import demo.TestData;
import demo.model.Person;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository repository;

    @BeforeEach
    void setup() {
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
        Person person = repository.findByName(TestData.person().getName());

        assertThat(person).isNotNull();
        assertThat(person.getName()).isEqualTo(TestData.person().getName());
    }

    @Test
    void shouldFindByTeammatesName() {
        List<Person> teammates = repository.findByTeammatesName(TestData.person().getName());

        assertThat(teammates).isNotEmpty();
        assertThat(teammates).hasSize(2);
        assertThat(teammates).extracting(Person::getName).containsExactlyInAnyOrder(
                TestData.person1().getName(),
                TestData.person2().getName());
    }

    @AfterAll
    static void cleanUp(@Autowired PersonRepository repository) {
        repository.deleteAll();
    }
}
