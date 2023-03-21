package com.ucc.cloudservice.controller;

import com.ucc.cloudservice.pojo.Client;
import com.ucc.cloudservice.service.ManetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    @Autowired
    private ManetService manetService;

    @PostMapping("/register")
    public ServerResponse register(@RequestBody Client client) {
        return manetService.register(client);
    }

    @PostMapping("/login")
    public ServerResponse login(@RequestBody Map<String, String> userInfo) {
        return manetService.login(userInfo);
    }

    @PostMapping("/manet/create")
    public ServerResponse createManet(@RequestParam("clientid") Integer clientid) {
        return manetService.createManet(clientid);
    }

    @PutMapping("/manet/join")
    public ServerResponse join(@RequestParam("net_id") Integer net_id, @RequestParam("client_id") Integer client_id) {
        return manetService.join(net_id, client_id);
    }

    @PutMapping("/manet/leave")
    public ServerResponse leave(@RequestParam("client_id") Integer client_id) {
        return manetService.leave(client_id);
    }

    @PutMapping("/logout")
    public ServerResponse logout(@RequestParam("client_id") Integer client_id) {
        return manetService.logout(client_id);
    }

    @PutMapping("/manet/update_location")
    public ServerResponse update(@RequestBody Client client){
        return manetService.update(client);
    }

    @GetMapping("/manet/available")
    public ServerResponse getAvailableNetworkIds() {
        return manetService.getAvailableNetworkIds();
    }




}
