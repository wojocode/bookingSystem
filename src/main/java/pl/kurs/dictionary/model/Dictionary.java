package pl.kurs.dictionary.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE dictionary SET deleted = true WHERE id = ?")
public class Dictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    private boolean deleted;

    @OneToMany(mappedBy = "dictionary", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<DictionaryValue> dictionaryValuesSet = new HashSet<>();

    public Dictionary(String name) {
        this.name = name;
    }

    public void addValue(String value) {
        dictionaryValuesSet.add(new DictionaryValue(value, this));
    }

    public void removeValue(DictionaryValue dictionaryValue) {
        dictionaryValuesSet.remove(dictionaryValue);
        dictionaryValue.setDictionary(null);
    }


}
