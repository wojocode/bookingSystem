package pl.kurs.dictionary.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "value"})
@NoArgsConstructor
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE dictionary_value SET deleted = true WHERE id = ?")
public class DictionaryValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "dictionary_value")
    private String value;

    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "dictionary_id")
    private Dictionary dictionary;


    public DictionaryValue(String value, Dictionary dictionary) {
        this.value = value;
        this.dictionary = dictionary;
    }
}
