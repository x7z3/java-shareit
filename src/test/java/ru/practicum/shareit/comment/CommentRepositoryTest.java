package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan("ru.practicum.shareit")
class CommentRepositoryTest {
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;

    private User itemUser;
    private Item item;
    private User commentUser;

    @Autowired
    public CommentRepositoryTest(CommentRepository commentRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;

        itemUser = userRepository.create(new User(1, "itemUser", "itemUser@mail.ru"));
        commentUser = userRepository.create(new User(2, "commentUser", "commentUser@mail.ru"));
        item = itemRepository.create(new Item(1, "name", "description", true, itemUser, null));
    }

    @Test
    void addComment() {
        Comment comment = new Comment(1, "comment text", item, commentUser, LocalDateTime.now());
        Comment resultComment = commentRepository.addComment(comment);
        assertNotNull(resultComment);
        assertEquals(comment, resultComment);
    }

    @Test
    void getItemComments() {
        commentRepository.addComment(new Comment(1, "comment text", item, commentUser, LocalDateTime.now()));
        List<Comment> itemComments = commentRepository.getItemComments(item.getId());
        assertNotNull(itemComments);
        assertFalse(itemComments.isEmpty());
    }
}