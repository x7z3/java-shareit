package ru.practicum.shareit.comment;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.comment.model.Comment;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class CommentsRepositoryImpl extends SimpleJpaRepository<Comment, Integer> implements CommentRepository {
    public CommentsRepositoryImpl(EntityManager entityManager) {
        super(Comment.class, entityManager);
    }

    @Override
    public Comment addComment(Comment comment) {
        return save(comment);
    }

    @Override
    public List<Comment> getItemComments(Integer itemId) {
        return findAll((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("item"), itemId));
    }
}
