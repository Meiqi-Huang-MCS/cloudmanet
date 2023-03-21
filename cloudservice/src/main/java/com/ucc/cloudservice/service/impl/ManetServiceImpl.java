package com.ucc.cloudservice.service.impl;

import com.ucc.cloudservice.controller.ResponseCode;
import com.ucc.cloudservice.controller.ServerResponse;
import com.ucc.cloudservice.mapper.ClientMapper;
import com.ucc.cloudservice.mapper.ManetMapper;
import com.ucc.cloudservice.pojo.Client;
import com.ucc.cloudservice.pojo.Manet;
import com.ucc.cloudservice.service.ManetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ManetServiceImpl implements ManetService {

    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private ManetMapper manetMapper;

    @Override
    public ServerResponse register(Client client) {

        try {
            int clientid = clientMapper.insert(client);
            ServerResponse<Integer> serverResponse = new ServerResponse<>();

            if (clientid > 0) {
                serverResponse.setData(client.getId());
                serverResponse.setResponseCode(ResponseCode.REGISTER_SUCCESS);
            } else {
                serverResponse.setData(null);
                serverResponse.setResponseCode(ResponseCode.REGISTER_ERROR);
            }
            return serverResponse;
        }catch (Exception e){
            ServerResponse<Integer> serverResponse = new ServerResponse<>();
            serverResponse.setData(null);
            serverResponse.setResponseCode(ResponseCode.REGISTER_ERROR);
            return serverResponse;
        }

    }

    @Override
    public ServerResponse login(Map<String, String> userInfo) {

        String username = userInfo.get("username");
        String password = userInfo.get("password");

        Client client = clientMapper.selectByUsernameAndPassword(username, password);
        ServerResponse<Client> serverResponse = new ServerResponse<>();

        if (client != null) {
            serverResponse.setData(client);
            serverResponse.setResponseCode(ResponseCode.LOGIN_SUCCESS);
        } else {
            serverResponse.setData(null);
            serverResponse.setResponseCode(ResponseCode.LOGIN_ERROR);
        }

        return serverResponse;
    }

    @Override
    public ServerResponse createManet(Integer clientid) {

        //check client
        Client client = clientMapper.selectByPrimaryKey(clientid);
        if (client == null) {
            ServerResponse serverResponse = new ServerResponse(ResponseCode.CREATE_MANET_ERROR);
            return serverResponse;
        }

        // create manet
        Manet manet = new Manet();
        manet.setNetstatus(true);
        manet.setCapacity(3);
        manet.setConnectAmount(1);
        int i1 = manetMapper.insert(manet);

        // update client
        client.setNetid(manet.getNetid());

        //call mapper
        int i = clientMapper.updateByPrimaryKey(client);
        if (i > 0 && i1 > 0) {
            ServerResponse<Integer> serverResponse = new ServerResponse<>();
            serverResponse.setResponseCode(ResponseCode.CREATE_MANET_SUCCESS);
            serverResponse.setData(manet.getNetid());
            return serverResponse;
        } else {
            ServerResponse<Integer> serverResponse = new ServerResponse<>();
            serverResponse.setResponseCode(ResponseCode.CREATE_MANET_ERROR);
            serverResponse.setData(null);
            return serverResponse;
        }


    }

    @Override
    public ServerResponse join(Integer net_id, Integer client_id) {

        int i1 = -1;
        // check client
        Client client = clientMapper.selectByPrimaryKey(client_id);
        if (client == null) {
            ServerResponse<Integer> serverResponse = new ServerResponse<>();
            serverResponse.setResponseCode(ResponseCode.JOIN_MANET_ERROR);
            serverResponse.setData(null);
            return serverResponse;
        }

        //check nei_id
        System.out.println("net_id"+net_id);
        //check capacity of manet
        Manet manet = manetMapper.selectByPrimaryKey(net_id);
        // if current members of manet is reach to capacity
        if (manet.getCapacity() == manet.getConnectAmount()) {    // create a new manet
            manet = new Manet();
            manet.setNetstatus(true);
            manet.setCapacity(3);
            manet.setConnectAmount(1);
            i1 = manetMapper.insert(manet);
            client.setNetid(manet.getNetid());
        } else {
            // update manet
            manet.setConnectAmount(manet.getConnectAmount() + 1);
            // call mapper
            i1 = manetMapper.updateByPrimaryKey(manet);
            client.setNetid(manet.getNetid());
        }

        // call mapper
        int i = clientMapper.updateByPrimaryKey(client);

        // if update successfully
        if (i > 0 && i1 > 0) {
            ServerResponse<Integer> serverResponse = new ServerResponse<>();
            serverResponse.setResponseCode(ResponseCode.JOIN_MANET_SUCCESS);
            serverResponse.setData(manet.getNetid());
            return serverResponse;
        } else {
            ServerResponse<Integer> serverResponse = new ServerResponse<>();
            serverResponse.setResponseCode(ResponseCode.JOIN_MANET_ERROR);
            serverResponse.setData(null);
            return serverResponse;
        }


    }

    @Override
    public ServerResponse leave(Integer client_id) {

        //check client
        Client client = clientMapper.selectByPrimaryKey(client_id);
        if (client == null || client.getNetid() == -1) {
            ServerResponse serverResponse = new ServerResponse(ResponseCode.LEAVE_MANET_ERROR);
            return serverResponse;
        }

        // check manet
        Manet manet = manetMapper.selectByPrimaryKey(client.getNetid());
        // if capacity larger than 1
        if (manet.getConnectAmount() > 1) {
            // ok;
            manet.setConnectAmount(manet.getConnectAmount() - 1);
            client.setNetid(-1);
        } else {
            // manet is terminated
            manet.setNetstatus(false);
            manet.setConnectAmount(0);
            client.setNetid(-1);
        }

        //call mapper to update
        int i = clientMapper.updateByPrimaryKey(client);
        int i1 = manetMapper.updateByPrimaryKey(manet);
        // if update successfully
        if (i > 0 && i1 > 0) {
            ServerResponse serverResponse = new ServerResponse(ResponseCode.LEAVE_MANET_SUCCESS);
            return serverResponse;
        } else {
            ServerResponse serverResponse = new ServerResponse(ResponseCode.LEAVE_MANET_ERROR);
            return serverResponse;
        }
    }


    @Override
    public ServerResponse logout(Integer client_id) {

        //check client
        Client client = clientMapper.selectByPrimaryKey(client_id);
        if (client == null ) {
            ServerResponse serverResponse = new ServerResponse(ResponseCode.LOGOUT_ERROR);
            return serverResponse;
        }

        if (client.getNetid() == -1) {
            ServerResponse serverResponse = new ServerResponse(ResponseCode.LOGOUT_SUCCESS);
            return serverResponse;
        }

        //check manet
        Manet manet = manetMapper.selectByPrimaryKey(client.getNetid());
        // if connect amount larger than 1
        if (manet.getConnectAmount() > 1) {
            // ok;
            manet.setConnectAmount(manet.getConnectAmount() - 1);
            client.setNetid(-1);
        } else {
            // manet is terminated
            manet.setNetstatus(false);
            manet.setConnectAmount(0);
            client.setNetid(-1);
        }

        //call mapper to update
        int i = clientMapper.updateByPrimaryKey(client);
        int i1 = manetMapper.updateByPrimaryKey(manet);
        // if update successfully
        if (i > 0 && i1 > 0) {
            ServerResponse serverResponse = new ServerResponse(ResponseCode.LOGOUT_SUCCESS);
            return serverResponse;
        } else {
            ServerResponse serverResponse = new ServerResponse(ResponseCode.LOGOUT_ERROR);
            return serverResponse;
        }
    }

    @Override
    public ServerResponse update(Client client) {

        int i = clientMapper.updateByPrimaryKey(client);

        if (i>0)
            return new ServerResponse(ResponseCode.SUCCESS);
        else
            return new ServerResponse(ResponseCode.ERROR);

    }

    @Override
    public ServerResponse getAvailableNetworkIds() {

        List<Manet> manets = manetMapper.selectAll();

        ServerResponse serverResponse = new ServerResponse();
        if (manets!=null){
            serverResponse.setData(manets);
            serverResponse.setResponseCode(ResponseCode.SUCCESS);
        }
        else{
            serverResponse.setData(null);
            serverResponse.setResponseCode(ResponseCode.ERROR);
        }

        return serverResponse;
    }

}
