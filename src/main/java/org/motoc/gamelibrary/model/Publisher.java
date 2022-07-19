package org.motoc.gamelibrary.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * The publisher of a game
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "publisher", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = "lower_case_name"))
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Name cannot be null or blank")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    @Column(nullable = false)
    private String name;

    @ToString.Exclude
    @Column(name = "lower_case_name", nullable = false)
    private String lowerCaseName;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "publisher")
    private Set<GameCopy> copies = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_contact")
    private Contact contact;

    // Helper methods

    /**
     * This is for adding a case-insensitive entry in database
     */
    @PrePersist
    @PreUpdate
    public void toLowerCase() {
        this.lowerCaseName = name.toLowerCase();
    }

    public void addCopy(GameCopy copy) {
        this.copies.add(copy);
        copy.setPublisher(this);
    }

    public void addContact(Contact contact) {
        this.setContact(contact);
        contact.setPublisher(this);
    }

    /**
     * This helper method is to use before deleting the contact of a publisher.
     */
    public void removeContact(Contact contact) {
        this.setContact(null);
        contact.setPublisher(null);
    }
}
