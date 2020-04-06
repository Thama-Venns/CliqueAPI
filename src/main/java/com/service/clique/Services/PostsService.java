package com.service.clique.Services;

import com.service.clique.Model.*;
import com.service.clique.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class PostsService {

    @Autowired PostRepository postRepository;
    @Autowired ProfileRepository profileRepository;
    @Autowired CollaborationsService collaborationsService;

    //Gets all user posts
    public Iterable<Post> getAllPost() {
        Iterable<Post> posts = null;
        try {
            posts =  postRepository.findAll();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return posts;

    }

    //Gets a single user posts
    public Post getPostById(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceAccessException("Post not found"));
    }

    //Gets a single user's List of posts
    public Collection<Post> GetSingleUserPosts(long id) {
        Collection<Post> posts = new ArrayList<>();
        try {
            posts = (Collection<Post>)postRepository.findAll();
            posts = posts.stream().filter(a -> a.getProfile().getId() == id).collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return posts;
    }

    // Get groupPosts
    public Iterable<Post> getGroupPosts(long groupId) {
        Iterable<Post> posts = null;
        try {
            posts = postRepository.findByGroupId(groupId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return posts;
    }

    // Gets a list of posts for groups where a user is a member.
    public Iterable<Post> GetGroupMemberPosts(long profileId) {
        Iterable<Post> groupPost  = null;
        try {
            groupPost = postRepository.findAllByGroupMemberId(profileId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return groupPost;
    }

    //Get posts by username
    public Collection<Post> getPostsByUsername(String username) {
        Collection<Post> posts = null;
        try {
            posts = postRepository.findByProfileUsername(username);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return posts;
    }

    //Creates a new user post
    public Post createPost(Post post) {
        Post save = null;
        try {
             save = postRepository.save(post);

            if(save != null && !post.getCollaborations().isEmpty()) {
                collaborationsService.createCollaborations(save);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return  save;
    }

    public long likePost(long postId) {
        Post post = null;
        long likes = 0;
        try {
            post = postRepository.findById(postId).get();
            if(post != null)
                likes = post.getLikes() + 1;
                post.setLikes(likes);
                postRepository.save(post);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return likes;
    }

    // Helpers
    public Collection<Post> match(Long profileId, String postText) {
        Collection<Post> posts = null;

        try {
            posts = postRepository.findMatch(postText);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return posts;
    }

    public boolean matchProfileDetails() {
        boolean foundMatch = false;

        try {
            Collection<Post> posts = postRepository.findDetailsMatch();

            if(!posts.isEmpty()) {
                foundMatch = true;
            }

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return foundMatch;
    }

    public String removePost(long id) {

        String result = "";
        try {
            Post post = postRepository.findById(id).get();
            postRepository.delete(post);

            result = "Post removed";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Internal Server error";
        }
        return result;
    }

//    public Share sharePost(long profileId, long postId) {
//        Share share = new Share();
//        try {
//            Post post = postRepository.findById(postId).get();
//            Profile profile = profileRepository.findById(profileId).get();
//
//            if(post.getProfile().getId() == profile.getId()) {
//                System.err.println("You cannot share your own post");
//                return null;
//            }
//
//            if(post != null)
//                share.setPost(post);
//            share.setProfile(profile);
//
//            shareRepository.save(share);
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//        return share;
//    }
//
//    public Share acceptShare(long id) {
//        Share share = null;
//        try {
//            share = shareRepository.findById(id).get();
//
//            if(!share.isAccepted())
//                share.setAccepted(true);
//                shareRepository.save(share);
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return share;
//    }
}
