package com.service.clique.Controller;

import com.service.clique.Model.Collaboration;
import com.service.clique.Model.Share;
import com.service.clique.Services.CollaborationsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("collaborations")
@Api("Handles User collaborations/shared content")
public class CollaborationController {

    @Autowired
    CollaborationsService collaborationsService;

    @GetMapping
    @ApiOperation("Gets all user collaborations / shared content")
    public Iterable<Collaboration> getSharedContent() {
        Iterable<Collaboration> collaborations= collaborationsService.getAllCollaborations();
        return collaborations;
    }

    @PostMapping
    @ApiOperation("Creates a post collaboration")
    public ResponseEntity<?> createCollaboration(@RequestBody Collaboration collaboration) {
        if (collaboration == null)
            return ResponseEntity.badRequest().body("Model State error");

        Collaboration newCollab = null;
        try {
            newCollab = collaborationsService.addCollaboration(collaboration);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
        return ResponseEntity.created(URI.create("location")).body(newCollab);
    }

    @PatchMapping
    @ApiOperation("Accept Or Decline a share request")
    public ResponseEntity<?> acceptCollaboration(@RequestParam long profileId, @RequestParam long collaborationId) {
        String result = collaborationsService.acceptCollaboration(profileId,collaborationId);

//        if(collaboration == null)
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");

        return ResponseEntity.ok(result);
    }
}
