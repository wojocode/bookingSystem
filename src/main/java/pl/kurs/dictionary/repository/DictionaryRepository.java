package pl.kurs.dictionary.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.dictionary.model.Dictionary;
import pl.kurs.dictionary.model.dto.DictionaryDto;

import java.util.Optional;

public interface DictionaryRepository extends JpaRepository<Dictionary, Integer> {
    @Query("SELECT d FROM Dictionary d LEFT JOIN FETCH d.dictionaryValuesSet WHERE d.name = :name")
    Optional<Dictionary> findByName(String name);

    @Query(value = "SELECT new pl.kurs.dictionary.model.dto.DictionaryDto(d.id, " +
            "d.name, " +
            "(select count(dv.id) from DictionaryValue dv where dv.dictionary.id = d.id)) " +
            "FROM Dictionary d",
            countQuery = "SELECT count(d.id) FROM Dictionary d")
    Page<DictionaryDto> findAllWithValues(Pageable pageable);


    void deleteByName(String name);
}
