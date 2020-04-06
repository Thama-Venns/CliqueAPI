package com.service.clique.Services;

import com.service.clique.Model.Connection;
import com.service.clique.Model.Group;
import com.service.clique.Model.Profile;
import com.service.clique.Repositories.ConnectionsRepository;
import com.service.clique.Repositories.GroupRepository;
import com.service.clique.Repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionsService {

    @Autowired ConnectionsRepository connectionsRepository;
    @Autowired ProfileRepository profileRepository;
    @Autowired GroupRepository groupRepository;

    //Gets all connections
    public Iterable<Connection> GetAllConnections() {
        return connectionsRepository.findAll();
    }

    //Gets a user's list of connections
    public Iterable<Connection> getUserConnections(long id) {
        Iterable<Connection>  userConnections = connectionsRepository.findByUserId(id);
        return userConnections;
    }

    //Gets Pending connection requests
    public Iterable<Connection> getPendingConnections(long id) {
        return connectionsRepository.findPendingByUserId(id);
    }


    //Add new connection
    public Connection addConnection(long user_1_Id, long user_2_Id) {
        Connection newConnection = null;
        try {
            Profile profile1;
            Profile profile2;
            profile1 = profileRepository.findById(user_1_Id).get();
            profile2 = profileRepository.findById(user_2_Id).get();
            Connection connection = new Connection();

            if(profile1 != null && profile2 != null)
                connection.setRequester(profile1);
                connection.setReceiver(profile2);

            newConnection = connectionsRepository.save(connection);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return newConnection;
    }

    //Accept Or Decline a connection request
    public Connection acceptConnectionRequest(long connectionId) {
        Connection connection = null;
        try {
            connection = connectionsRepository.findById(connectionId).get();

            if(connection != null) {
                if (!connection.isAccepted())
                    connection.setAccepted(true);
            }

            connectionsRepository.save(connection);

            // add to friends list
            Group receiverGroup = groupRepository.findByGroupNameAndProfileId("Friends", connection.getReceiver().getId());
            receiverGroup.getMembers().add(connection.getRequester());

            Group requesterGroup = groupRepository.findByGroupNameAndProfileId("Friends", connection.getRequester().getId());
            requesterGroup.getMembers().add(connection.getReceiver());

            groupRepository.save(receiverGroup);
            groupRepository.save(requesterGroup);

        } catch (Exception e) {

        }
        return connection;
    }

}
