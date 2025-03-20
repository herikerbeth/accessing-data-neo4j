package demo.repository;

import demo.TestData;
import demo.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PersonRepositoryTest {

    @MockitoBean
    private PersonRepository repository;

    @Test
    void shouldFindByName() {
        Person person = TestData.person();
        when(repository.findByName("Greg")).thenReturn(person);

        Person personResult = repository.findByName("Greg");

        assertThat(personResult).isNotNull();
        assertThat(personResult.getName()).isEqualTo(person.getName());
        verify(repository, times(1)).findByName("Greg");
    }

    @Test
    void shouldFindByTeammatesName() {
        Person person = TestData.person();
        Person person1 = TestData.person1();
        Person person2 = TestData.person2();

        person.worksWith(person1);
        person.worksWith(person2);
        person1.worksWith(person2);

        when(repository.findByTeammatesName("Greg")).thenReturn(List.of(person1, person2));

        List<Person> teammates = repository.findByTeammatesName("Greg");

        assertThat(teammates).isNotEmpty();
        assertThat(teammates).hasSize(2);
        assertThat(teammates).extracting(Person::getName).containsExactlyInAnyOrder(
                TestData.person1().getName(),
                TestData.person2().getName());
    }
}
