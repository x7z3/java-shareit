package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "item_requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "description", unique = true, nullable = false)
    private String description;

    @ManyToOne(targetEntity = User.class)
    private User requestor;

    @ManyToOne(targetEntity = Item.class)
    private Item item;

    @Column(name = "created")
    private LocalDateTime created;
}