package com.service.clique.Controller;

import com.service.clique.Model.Share;
import com.service.clique.Services.ShareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shares")
@Api("Handles post sharing")
public class ShareController {
    @Autowired ShareService shareService;

    @GetMapping
    @ApiOperation("Gets all post shares")
    public Iterable<Share> getAllProfiles() {
        return shareService.getAllShares();
    }

    @GetMapping(value = "/profile")
    @ApiOperation("Gets all user post shares")
    public Iterable<Share> getAUserPostShares(@RequestParam long profileId) {
        return shareService.GetUserShares(profileId);
    }

    @PostMapping("/{profileId}/{postId}/{groupId}")
    @ApiOperation("Allows users to share a post")
    public ResponseEntity<?> shareAPost(@PathVariable long profileId, @PathVariable long postId, @PathVariable long groupId) {
        String shareResult = null;
        try {

            Share share = shareService.findShare(postId, profileId);

            if(share != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your request is awaiting approval.");
            }

            shareResult = shareService.sharePost(profileId, postId, groupId);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(shareResult);

        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong: " + e.getMessage());
        }
        return ResponseEntity.ok(shareResult);
    }

    @PatchMapping("/{profileId}/{shareId}")
    @ApiOperation("Accept Or Decline a share request")
    public ResponseEntity<?> acceptShare(@PathVariable long profileId, @PathVariable long shareId) {
        Share share = shareService.acceptShare(profileId,shareId);

        if(share == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");

        return ResponseEntity.ok("Accepted");
    }
}
