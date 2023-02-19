package ru.practicum.shareit.comment.model;

import lombok.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EnableAutoConfiguration
@ToString
@Table(name = "comment")
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @NotBlank
    @Column(name = "text", nullable = false)
    private String text;

    @NotNull
    @ManyToOne(targetEntity = Item.class)
    private Item item;

    @NotNull
    @ManyToOne(targetEntity = User.class)
    private User user;

    @NotNull
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}
