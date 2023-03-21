package com.ucc.cloudservice.service;

import com.ucc.cloudservice.controller.ServerResponse;
import com.ucc.cloudservice.pojo.Client;
import java.util.Map;

public interface ManetService {

    //send = username, password; receive = id.
    ServerResponse register (Client client);

    //send = username, password; receive = ack.
    ServerResponse login(Map<String,String> userInfo);

    //send = MANET; receive = net_id.
    ServerResponse createManet(Integer clientid);

    //send = join (net_id, any local, global); receive = ack (net_id).
    ServerResponse join(Integer net_id, Integer client_id);

    //  leave = bye; receive = ack
    ServerResponse leave(Integer client_id);

    //send = username, password; receive = ack.
    ServerResponse logout(Integer client_id);

    //update location
    ServerResponse update(Client client);

    //get available network ids
    ServerResponse getAvailableNetworkIds();
}
