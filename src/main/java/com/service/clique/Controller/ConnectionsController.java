package com.service.clique.Controller;

import com.service.clique.Model.Connection;
import com.service.clique.Services.ConnectionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/connections")
@Api("Handles User Connections")
public class ConnectionsController {

    @Autowired
    ConnectionsService connectionsService;

    @GetMapping
    @ApiOperation("Retrieves all connections")
    public Iterable<Connection> GetAllConnectionbs() {
        return connectionsService.GetAllConnections();
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieves a Single User's List of connections")
    public Iterable<Connection> getUserConnections(@PathVariable long id) {
        return connectionsService.getUserConnections(id);
    }

    @GetMapping("/requests/{id}")
    @ApiOperation("Retrieves a Single User's List of connection requests")
    public Iterable<Connection> connectionRequests(@PathVariable long id) {
        return connectionsService.getPendingConnections(id);
    }


    @PostMapping("{requesterId}/{receiverId}")
    @ApiOperation("Adds a new connection request")
    public ResponseEntity newConnection(@PathVariable long requesterId, @PathVariable long receiverId) {
        Connection newConnection = null;
        try {
            newConnection = connectionsService.addConnection(requesterId, receiverId);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newConnection);
    }

    @PatchMapping("request")
    @ApiOperation("Accept Or Decline a connection request")
    public ResponseEntity<?> updateConnectionStatus(@RequestParam long cid) {
        try {
            Connection connection = connectionsService.acceptConnectionRequest(cid);

            if (connection == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Connection Request Not Found");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!");
        }
        return ResponseEntity.ok("resource address updated");
    }



}
