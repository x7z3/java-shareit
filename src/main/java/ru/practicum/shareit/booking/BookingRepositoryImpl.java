package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

public class BookingRepositoryImpl extends SimpleJpaRepository<Booking, Integer> implements BookingRepository {
    private static final String STATUS_FIELD_NAME = "status";
    private static final String END_TIME_FIELD_NAME = "endTime";
    private static final String START_TIME_FIELD_NAME = "startTime";

    public BookingRepositoryImpl(EntityManager entityManager) {
        super(Booking.class, entityManager);
    }

    @Override
    public Booking createBooking(Booking booking) {
        return save(booking);
    }

    @Override
    public List<Booking> getBookingsByItems(BookingState state, List<Item> items, Integer from, Integer size) {
        Specification<Booking> bookings = (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Object> inCriteria = criteriaBuilder.in(root.get("item"));
            for (Item item : items) {
                inCriteria.value(item);
            }
            return inCriteria;
        };

        Specification<Booking> statusEqual = getStatusSpecification(state);
        if (statusEqual != null) return findAll(where(bookings).and(statusEqual), Sort.by("id").descending());

        if (from == null && size == null) {
            return findAll(where(bookings), Sort.by("id").descending());
        } else {
            return findAll(where(bookings), PageRequest.of(from / size, size, Sort.by("id").descending())).toList();
        }
    }

    @Override
    public List<Booking> get(BookingState state, Integer userId, Integer from, Integer size) {
        Specification<Booking> bookerIdEqual = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("booker"), userId);
        Specification<Booking> statusEqual;

        statusEqual = getStatusSpecification(state);

        if (statusEqual != null) return findAll(
                where(bookerIdEqual).and(statusEqual),
                state.equals(BookingState.CURRENT) ? Sort.by("id").ascending() : Sort.by("id").descending()
        );

        if (from == null && size == null) {
            return findAll(where(bookerIdEqual), Sort.by("id").descending());
        } else {
            return findAll(where(bookerIdEqual), PageRequest.of(from / size, size, Sort.by("id").descending())).toList();
        }
    }

    private static Specification<Booking> getStatusSpecification(BookingState state) {
        LocalDateTime currentTime = LocalDateTime.now();
        Specification<Booking> statusEqual;
        switch (state) {
            case PAST:
                statusEqual = (root, query, criteriaBuilder) ->
                        criteriaBuilder.and(
                                criteriaBuilder.lessThan(root.get(END_TIME_FIELD_NAME), currentTime),
                                criteriaBuilder.equal(root.get(STATUS_FIELD_NAME), BookingStatus.APPROVED),
                                criteriaBuilder.notEqual(root.get(STATUS_FIELD_NAME), BookingStatus.REJECTED),
                                criteriaBuilder.notEqual(root.get(STATUS_FIELD_NAME), BookingStatus.CANCELED),
                                criteriaBuilder.notEqual(root.get(STATUS_FIELD_NAME), BookingStatus.WAITING)
                        );
                break;
            case CURRENT:
                statusEqual = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.get(START_TIME_FIELD_NAME), currentTime),
                        criteriaBuilder.greaterThan(root.get(END_TIME_FIELD_NAME), currentTime)
                );
                break;
            case FUTURE:
                statusEqual = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                        criteriaBuilder.greaterThan(root.get(START_TIME_FIELD_NAME), currentTime),
                        criteriaBuilder.notEqual(root.get(STATUS_FIELD_NAME), BookingStatus.REJECTED),
                        criteriaBuilder.notEqual(root.get(STATUS_FIELD_NAME), BookingStatus.CANCELED)
                );
                break;
            case WAITING:
                statusEqual = (root, query, criteriaBuilder) -> criteriaBuilder.and(
                        criteriaBuilder.greaterThan(root.get(START_TIME_FIELD_NAME), currentTime),
                        criteriaBuilder.equal(root.get(STATUS_FIELD_NAME), BookingStatus.WAITING)
                );
                break;
            case REJECTED:
                statusEqual = (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(STATUS_FIELD_NAME), BookingStatus.REJECTED)
                ;
                break;
            default:
                statusEqual = null;
        }
        return statusEqual;
    }

    @Override
    public Booking get(Integer bookingId) {
        return findById(bookingId).orElseThrow(() -> new BookingNotFoundException(bookingId));
    }

    @Override
    public List<Booking> getBookingsBy(Integer itemId, Integer userId) {
        return findAll((root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("item"), itemId),
                criteriaBuilder.equal(root.get("booker"), userId)
        ));
    }

    @Override
    public List<Booking> getLastNextBooking(int itemId, Integer userId) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Booking> lastBookings = findAll((root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("item").get("owner"), userId),
                criteriaBuilder.equal(root.get("item").get("id"), itemId),
                criteriaBuilder.lessThanOrEqualTo(root.get(START_TIME_FIELD_NAME), currentTime),
                criteriaBuilder.equal(root.get(STATUS_FIELD_NAME), BookingStatus.APPROVED)
        ), Sort.by("id").descending());

        List<Booking> nextBookings = findAll((root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("item").get("owner"), userId),
                criteriaBuilder.equal(root.get("item").get("id"), itemId),
                criteriaBuilder.greaterThan(root.get(START_TIME_FIELD_NAME), currentTime),
                criteriaBuilder.equal(root.get(STATUS_FIELD_NAME), BookingStatus.APPROVED)
        ), Sort.by("id").descending());

        ArrayList<Booking> bookings = new ArrayList<>();
        bookings.add(lastBookings.isEmpty() ? null : lastBookings.get(0));
        bookings.add(nextBookings.isEmpty() ? null : nextBookings.get(0));
        return bookings;
    }
}
