package com.service.clique.Controller;

import com.service.clique.Model.Post;
import com.service.clique.Services.PostsService;
import com.service.clique.Services.ShareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/posts")
@Api("Handles User posts")
public class PostsController {

    @Autowired PostsService postsService;
    @Autowired ShareService shareService;

    //Post Retrievals (GETS)
    @GetMapping
    @ApiOperation("Gets all user posts")
    public ResponseEntity getAllPost() {
        Iterable<Post> posts = null;
        try {
            posts =  postsService.getAllPost();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something Went Wrong");
        }
        return ResponseEntity.ok().body(posts);

    }

    @GetMapping(value = "/{postId}")
    @ApiOperation("Gets a single user post")
    public Post getPostById(@PathVariable long postId) {
        return postsService.getPostById(postId);
    }

    @GetMapping("/profile/{uid}")
    @ApiOperation("Gets a single user's List of posts")
    public ResponseEntity<?> GetSingleUserPosts(@PathVariable long uid) {
        Collection<Post> profilePosts = null;
        try {
            profilePosts = postsService.GetSingleUserPosts(uid);

            if(profilePosts == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No content");

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
        return ResponseEntity.ok(profilePosts);
    }

    @GetMapping("user")
    @ApiOperation("Gets a single user's List of posts by username")
    public ResponseEntity<?> getPostsByUsername(@RequestParam String username) {

        Collection<Post> posts = postsService.getPostsByUsername(username);

        if(posts.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No posts found for this user");

        return ResponseEntity.ok(posts);
    }

    @GetMapping("groups/{groupId}")
    @ApiOperation("Gets all posts by the group Id")
    public ResponseEntity<?> getGroupAllPosts(@PathVariable long groupId) {
        Iterable<Post> groups = null;
        try {
            groups = postsService.getGroupPosts(groupId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error!");
        }
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/group/profile/{profileId}")
    @ApiOperation("Gets posts in groups where user is a member")
    public ResponseEntity<?> getGroupMemberPosts(@PathVariable long profileId) {
        Iterable<Post> groupPosts = null;
        try {
            groupPosts = postsService.GetGroupMemberPosts(profileId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong" + e.getMessage());
        }
        return ResponseEntity.ok(groupPosts);
    }


    @PostMapping
    @ApiOperation("Creates a new user post")
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        if(post.getPostText() == null)
            return ResponseEntity.badRequest().body("Request not accepted");

        boolean detailMatchFound = postsService.matchProfileDetails();
        Collection<Post> matchingPosts = postsService.match(post.getProfile().getId(), post.getPostText());

        if(detailMatchFound) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("There are private user details found in your post");
        }

        if(!matchingPosts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This post has a conflict with another user's post");
        }

        Post save = postsService.createPost(post);
        return  ResponseEntity.ok(save);
    }

    @PatchMapping("like/{postId}")
    @ApiOperation("Handle post Likes")
    public ResponseEntity<?> likePost(@PathVariable("postId") long postId) {
        try {
            long  like = postsService.likePost(postId);
            if (like == 0)
                ResponseEntity.notFound();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
        return ResponseEntity.ok("liked");
    }

    @DeleteMapping("delete/{postId}")
    @ApiOperation("Handle post delete")
    public  ResponseEntity<?> deletePost(@PathVariable("postId") long postId) {
        String result = postsService.removePost(postId);
        return ResponseEntity.ok(result);
    }
}
