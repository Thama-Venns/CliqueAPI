package com.service.clique.Services;

import com.service.clique.Model.Comment;
import com.service.clique.Repositories.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentsService {

    @Autowired
    CommentsRepository commentsRepository;

    public Comment addComment(Comment comment) {
        try {
            commentsRepository.save(comment);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return comment;
    }
}
