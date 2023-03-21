package com.ucc.cloudservice;

import com.ucc.cloudservice.controller.ResponseCode;
import com.ucc.cloudservice.controller.ServerResponse;
import com.ucc.cloudservice.mapper.ClientMapper;
import com.ucc.cloudservice.pojo.Client;
import com.ucc.cloudservice.pojo.Manet;
import com.ucc.cloudservice.service.ManetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class test {

    @Autowired
    private ManetService manetService;

    Client client;

    {
        client = new Client();
        client.setId(1);
        client.setLongitude(40.7128f);
        client.setLatitude(-74.0060f);
        client.setNetid(1);
        client.setUsername("testuser");
        client.setPassword("testpass");
    }

    @Test
    public void testRegister() {

        ServerResponse<Integer> response = manetService.register(client);
        Integer clientId = response.getData();
        //Assert that the server response code is as expected and the client id is not null
        assertEquals(ResponseCode.REGISTER_SUCCESS, response.getResponseCode());
        assertNotNull(clientId);
        System.out.println(clientId);
    }

    @Test
    public void testLogin() {
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", "testuser");
        userInfo.put("password", "testpass");
        ServerResponse<Client> response = manetService.login(userInfo);
        Client loggedinClient = response.getData();
        //Assert that the server response code is as expected and the logged in client's id is not null
        assertEquals(ResponseCode.LOGIN_SUCCESS, response.getResponseCode());
        assertNotNull(loggedinClient.getId());
    }

    @Test
    public void testCreateManet() {
        ServerResponse<Integer> response = manetService.createManet(client.getId());
        Integer manetId = response.getData();
        //Assert that the server response code is as expected and the manet id is not null
        assertEquals(ResponseCode.CREATE_MANET_SUCCESS, response.getResponseCode());
        assertNotNull(manetId);
    }

    @Test
    public void testJoin() {
        Client client = new Client();
        client.setLongitude(42.7128f);
        client.setLatitude(-75.0060f);
        client.setNetid(-1);
        client.setUsername("testuser3");
        client.setPassword("testpass3");
        manetService.register(client);
        Integer manetId = 1;
        ServerResponse joinResponse = manetService.join(manetId, client.getId());
        //Assert that the server response code is as expected
        assertEquals(ResponseCode.JOIN_MANET_SUCCESS, joinResponse.getResponseCode());
    }

    @Test
    public void testLeave() {

        ServerResponse leaveResponse = manetService.leave(client.getId());

        //Assert that the server response code is as expected
        assertEquals(ResponseCode.LEAVE_MANET_SUCCESS, leaveResponse.getResponseCode());
    }

    @Test
    public void testLogout() {
        ServerResponse logoutResponse = manetService.logout(3);
        //Assert that the server response code is as expected
        assertEquals(ResponseCode.LOGOUT_SUCCESS, logoutResponse.getResponseCode());
    }
}

