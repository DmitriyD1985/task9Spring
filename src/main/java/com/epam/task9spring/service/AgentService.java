package com.epam.task9spring.service;

import com.epam.task9spring.model.Agent;


public interface AgentService {

    Agent saveAgent(String JsonInfo);

    String getAgentByName(String firstName);

    String getAllAgent();

    Agent updateAgent(String JsonInfo);

    boolean deleteAgent(String name);

    boolean setAgentToEstate(String requestBody);

    String getTopAgentsBySumOfSales();

}
