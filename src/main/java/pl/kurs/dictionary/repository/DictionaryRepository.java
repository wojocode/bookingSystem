package pl.kurs.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.dictionary.model.Dictionary;

import java.util.Optional;

public interface DictionaryRepository extends JpaRepository<Dictionary, Integer> {
    Optional<Dictionary> findByName(String name);

    void deleteByName(String name);
}
