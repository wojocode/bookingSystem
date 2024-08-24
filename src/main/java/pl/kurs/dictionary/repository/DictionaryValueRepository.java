package pl.kurs.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.dictionary.model.DictionaryValue;

import java.util.Optional;

public interface DictionaryValueRepository extends JpaRepository<DictionaryValue, Integer> {

    @Query("SELECT dv FROM DictionaryValue dv LEFT JOIN FETCH dv.dictionary as d WHERE dv.value = :dictionaryValueName AND d.name = :dictionaryName")
    Optional<DictionaryValue> findByDictionaryNameAndValue(String dictionaryName, String dictionaryValueName);
}
