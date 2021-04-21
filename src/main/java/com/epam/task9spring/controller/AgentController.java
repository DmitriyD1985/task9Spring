package com.epam.task9spring.controller;


import com.epam.task9spring.service.AgentService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/agent")
@RestController
public class AgentController {

    AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/publics/create")
    public String registrationAgent(@RequestBody String request) {
        agentService.saveAgent(request);
        return "{\"msg\":\"здание добавлено\"}";
    }

    @GetMapping("/publics/read/{name}")
    public String getOneAgent(@PathVariable String name) {
        agentService.getAgentByName(name);
        return "{\"msg\":\"Получите одно здание\"}";
    }

    @PostMapping("/admin/update")
    public String updateAgent(@RequestBody String request) {
        agentService.updateAgent(request);
        return "RealEstate updated";
    }

    @DeleteMapping("/admin/delete")
    public String deleteAgent(@RequestBody String request) {
        if (agentService.deleteAgent(request))
            return "{\"msg\":\"Агент удалено\"}";
        else
            return "{\"msg\":\"Агент не удалено\"}";
    }

    @GetMapping("/admin/all")
    public String getAllAgent() {
        agentService.getAllAgent();
        return "{\"msg\":" + agentService.getAllAgent() + "}";
    }

    @PostMapping("/admin/setagent")
    public String setAgentToRealEstate(@RequestBody String requestBody) {
        agentService.setAgentToEstate(requestBody);
        return "{\"msg\":\"Agent is set to real estate\"}";
    }

    @GetMapping("/admin/gettopfive")
    public String getTopFive() {
        ;
        return "{\"msg\":" + agentService.getTopAgentsBySumOfSales() + "}";
    }
}
