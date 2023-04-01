package ru.practicum.shareit.item;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ChangingItemOwnerException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Repository
public class ItemRepositoryImpl extends SimpleJpaRepository<Item, Integer> implements ItemRepository {
    public ItemRepositoryImpl(EntityManager em) {
        super(Item.class, em);
    }

    @Override
    public Item create(Item item) {
        return super.save(item);
    }

    @Override
    public void delete(int itemId) {
        super.delete(findItemById(itemId));
    }

    @Override
    public Item update(Item item) {
        Item foundItem = findItemById(item.getId());
        foundItem.setName(item.getName());
        foundItem.setDescription(item.getDescription());
        foundItem.setAvailable(item.getAvailable());
        foundItem.setOwner(item.getOwner());
        foundItem.setItemRequest(item.getItemRequest());
        return foundItem;
    }

    @Override
    public Item patch(Item item) {
        Item foundItem = findItemById(item.getId());

        if (!item.getOwner().equals(foundItem.getOwner())) throw new ChangingItemOwnerException(foundItem);

        if (item.getName() != null) foundItem.setName(item.getName());
        if (item.getDescription() != null) foundItem.setDescription(item.getDescription());
        if (item.getAvailable() != null) foundItem.setAvailable(item.getAvailable());
        if (item.getOwner() != null) foundItem.setOwner(item.getOwner());
        if (item.getItemRequest() != null) foundItem.setItemRequest(item.getItemRequest());
        return foundItem;
    }

    @Override
    public List<Item> getAll(Integer sharerUserId) {
        return super.findAll((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner"),
                sharerUserId), Sort.by("id").ascending());
    }

    @Override
    public Item get(int itemId) {
        return findItemById(itemId);
    }

    @Override
    public List<Item> search(String searchText) {
        if (searchText.isBlank()) return new ArrayList<>();

        Specification<Item> nameLike = (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.upper(root.get("name")), "%" + searchText.toUpperCase() + "%"
        );

        Specification<Item> descriptionLike = (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.upper(root.get("description")), "%" + searchText.toUpperCase() + "%"
        );

        Specification<Item> availableTrue = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("available"), true);

        return super.findAll(where(nameLike).or(descriptionLike).and(availableTrue));
    }

    @Override
    public List<Item> getItemByRequest(ItemRequest itemRequest) {
        return findAll((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("itemRequest"), itemRequest));
    }

    private Item findItemById(Integer itemId) {
        return findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
    }
}
