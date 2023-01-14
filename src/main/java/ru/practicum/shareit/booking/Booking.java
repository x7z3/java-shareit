package ru.practicum.shareit.booking;

import lombok.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EnableAutoConfiguration
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @NotBlank
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotBlank
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @NotBlank
    @ManyToOne(targetEntity = Item.class)
    private Item item;

    @NotBlank
    @ManyToOne(targetEntity = User.class)
    private User booker;

    @NotBlank
    @Column(name = "status", nullable = false)
    private BookingStatus status;
}
