package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.toItem;
import static ru.practicum.shareit.item.ItemMapper.toItemDto;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto) {
        User owner = findOwner(itemDto);
        return toItemDto(itemRepository.create(toItem(itemDto, owner)));
    }

    @Override
    @Transactional
    public void deleteItem(int itemId) {
        itemRepository.delete(itemId);
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto) {
        User owner = findOwner(itemDto);
        return toItemDto(itemRepository.update(toItem(itemDto, owner)));
    }

    @Override
    @Transactional
    public ItemDto patchItem(ItemDto itemDto) {
        User owner = findOwner(itemDto);
        return toItemDto(itemRepository.patch(toItem(itemDto, owner)));
    }

    @Override
    public List<ItemDto> getAllItems(Integer sharerUserId) {
        return itemRepository.getAll(sharerUserId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto getItem(int itemId) {
        return toItemDto(itemRepository.get(itemId));
    }

    @Override
    public List<ItemDto> searchItem(String searchText) {
        return itemRepository.search(searchText).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    private User findOwner(ItemDto itemDto) {
        Integer ownerId = itemDto.getOwnerId();
        User owner = null;
        if (ownerId != null) {
            owner = userRepository.getUser(ownerId);
        }
        return owner;
    }
}
