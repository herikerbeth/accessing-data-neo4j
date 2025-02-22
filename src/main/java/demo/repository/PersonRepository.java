package demo.repository;

import demo.model.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Person findByName(String name);

    @Query("MATCH (p:Person)-[:TEAMMATE]-(t:Person) WHERE t.name = $name RETURN p")
    List<Person> findByTeammatesName(String name);
}
