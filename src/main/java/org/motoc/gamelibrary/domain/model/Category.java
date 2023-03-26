package org.motoc.gamelibrary.domain.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * A game category
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = "lower_case_title", name = "unique_cat"))
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq_gen")
    @SequenceGenerator(name = "category_seq_gen", sequenceName = "category_sequence", initialValue = 1)
    private Long id;

    @NotBlank(message = "Title cannot be null or blank")
    @Size(max = 50, message = "Name cannot exceed 50")
    @Column(nullable = false, length = 50)
    private String title;

    @ToString.Exclude
    @Column(name = "lower_case_title", nullable = false, length = 50)
    private String lowerCaseTitle;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "categories")
    private Set<Game> games = new HashSet<>();

    // Helper methods

    /**
     * Adding a case-insensitive entry in database
     */
    @PrePersist
    @PreUpdate
    public void toLowerCase() {
        this.lowerCaseTitle = title != null ? title.toLowerCase() : null;
    }

    public void addGame(Game game) {
        games.add(game);
        game.getCategories().add(this);
    }

    public void removeGame(Game game) {
        games.remove(game);
        game.getCategories().remove(this);
    }
}
