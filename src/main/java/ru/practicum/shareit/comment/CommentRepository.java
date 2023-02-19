package ru.practicum.shareit.comment;

import ru.practicum.shareit.comment.model.Comment;

import java.util.List;

public interface CommentRepository {
    Comment addComment(Comment comment);

    List<Comment> getItemComments(Integer itemId);
}
