package ru.practicum.shareit.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Set;

import static org.springframework.data.jpa.domain.Specification.where;

@Repository
public class ItemRequestRepositoryImpl extends SimpleJpaRepository<ItemRequest, Integer> implements ItemRequestRepository {

    public ItemRequestRepositoryImpl(EntityManager entityManager) {
        super(ItemRequest.class, entityManager);
    }

    @Override
    public ItemRequest createItemRequest(ItemRequest itemRequest) {
        return save(itemRequest);
    }

    @Override
    public List<ItemRequest> getItemRequests(User user) {
        return findAll((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("requestor"), user));
    }

    @Override
    public List<ItemRequest> getItemRequests(Integer from, Integer size, Set<Integer> itemRequestIds) {
        Specification<ItemRequest> whereIdInIdsList = where((root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("id"));
            itemRequestIds.forEach(in::value);
            return in;
        });

        if (from == null && size == null) {
            return findAll(whereIdInIdsList, Sort.by("created").descending());
        }

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("created").descending());
        return findAll(whereIdInIdsList, pageable).toList();
    }

    @Override
    public ItemRequest getItemRequest(Integer itemRequestId) {
        return getReferenceById(itemRequestId);
    }
}
