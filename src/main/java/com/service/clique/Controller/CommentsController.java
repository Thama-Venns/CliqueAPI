package com.service.clique.Controller;

import com.service.clique.Model.Comment;
import com.service.clique.Services.CommentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController()
@RequestMapping("/comment")
@Api("Handles post comments")
public class CommentsController {

    @Autowired
    CommentsService commentsService;

    @PostMapping
    @ApiOperation("Creates a new post")
    public ResponseEntity<?> createPost(@RequestBody Comment comment) {

        if(comment == null)
            return ResponseEntity.badRequest().body("Model state error");

        Comment newComment;
        try {
            newComment = commentsService.addComment(comment);

            if (newComment == null)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Comment could not be created");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something wen wrong");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
