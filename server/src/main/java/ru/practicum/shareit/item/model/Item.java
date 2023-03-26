package ru.practicum.shareit.item.model;

import lombok.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@EnableAutoConfiguration
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "available", nullable = false)
    private Boolean available;

    @ManyToOne(targetEntity = User.class)
    private User owner;

    @ManyToOne(targetEntity = ItemRequest.class)
    private ItemRequest itemRequest;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return id.equals(item.id) && owner.equals(item.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner);
    }
}
