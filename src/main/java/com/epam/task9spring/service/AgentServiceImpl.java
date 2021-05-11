package com.epam.task9spring.service;

import com.epam.task9spring.model.Agent;
import com.epam.task9spring.model.RealEstate;
import com.epam.task9spring.repository.AgentRepository;
import com.epam.task9spring.repository.RealEstateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;


@Service
public class AgentServiceImpl implements AgentService {

    private ObjectNode node = null;
    private final AgentRepository agentRepository;
    private final RealEstateRepository realEstateRepository;

    public AgentServiceImpl(AgentRepository agentRepository, RealEstateRepository realEstateRepository) {
        this.agentRepository = agentRepository;
        this.realEstateRepository = realEstateRepository;
    }

    @Override
    public Agent saveAgent(String jsonInfo) {
        try {
            node = new ObjectMapper().readValue(jsonInfo, ObjectNode.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String name = node.get("name").asText();
        Agent agent = new Agent(name);
        agent.setSalescount(0);
        agent.setRealEstates(null);
        agentRepository.save(agent);
        return agent;
    }

    @Override
    public String getAgentByName(String name) {
        return new JSONObject(agentRepository.findByName(name).orElseThrow()).toString();
    }

    @Override
    public String getAllAgent() {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(agentRepository.findAll());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public Agent updateAgent(String jsonInfo) {
        try {
            node = new ObjectMapper().readValue(jsonInfo, ObjectNode.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Agent agent = agentRepository.findByName(node.get("oldname").asText()).orElseThrow();
        agent.setName(node.get("newname").asText());
        agentRepository.save(agent);
        return agent;
    }

    @Override
    public boolean deleteAgent(String name) {
        agentRepository.delete(agentRepository.findByName(name).orElseThrow());
        return true;
    }

    @Override
    public boolean setAgentToEstate(String requestBody) {
        try {
            node = new ObjectMapper().readValue(requestBody, ObjectNode.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            realEstateRepository
                    .findByName(node.get("realEstateName").asText()).orElseThrow()
                    .setAgent(agentRepository.findByName(node.get("agentName").asText()).orElseThrow());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getTopAgentsBySumOfSales() {
        Map<BigDecimal, Agent> sales = new TreeMap<>();
        agentRepository.findAll().forEach(a -> {
            sales.put(a.getSoldRealEstate().stream().map(RealEstate::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add), agentRepository.findById(a.getId()).orElseThrow());
        });

        ObjectMapper mapper = new ObjectMapper();
        String jsonAnswer = "";
        try {
            jsonAnswer = mapper.writeValueAsString(sales);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonAnswer;
    }
}