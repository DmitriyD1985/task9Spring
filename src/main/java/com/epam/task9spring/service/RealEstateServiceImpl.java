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
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RealEstateServiceImpl implements RealEstateService {

    private ObjectNode node = null;
    private final RealEstateRepository realEstateRepository;
    private final AgentRepository agentRepository;

    public RealEstateServiceImpl(RealEstateRepository realEstateRepository, AgentRepository agentRepository) {
        this.realEstateRepository = realEstateRepository;
        this.agentRepository = agentRepository;
    }

    @Override
    public RealEstate saveRealEstate(String jsonInfo) {
        try {
            node = new ObjectMapper().readValue(jsonInfo, ObjectNode.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String name = node.get("name").asText();
        String price = node.get("price").asText();
        RealEstate realEstate = new RealEstate(name);
        realEstate.setViews(0);
        realEstate.setAgent(null);
        realEstate.setPrice(new BigDecimal(price));
        realEstateRepository.save(realEstate);
        return realEstate;
    }

    @Override
    public String getRealEstateByName(String name) {
        RealEstate showingRealEstate = realEstateRepository.findByName(name).orElseThrow();
        showingRealEstate.setViews(showingRealEstate.getViews() + 1);
        realEstateRepository.save(showingRealEstate);
        return new JSONObject(showingRealEstate).toString();
    }

    @Override
    public String getAllRealEstate() {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(realEstateRepository.findAll());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public RealEstate updateRealEstate(String jsonInfo) {
        try {
            node = new ObjectMapper().readValue(jsonInfo, ObjectNode.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RealEstate realEstate = realEstateRepository.findByName(node.get("oldname").asText()).orElseThrow();
        realEstate.setName(node.get("newname").asText());
        realEstateRepository.save(realEstate);
        return realEstate;
    }

    @Override
    public boolean deleteRealEstate(String name) {
        realEstateRepository.delete(realEstateRepository.findByName(name).orElseThrow());
        return true;
    }

    @Override
    public RealEstate sale(String request) {
        try {
            node = new ObjectMapper().readValue(request, ObjectNode.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Agent agent = agentRepository.findByName(node.get("agentname").asText()).orElseThrow();
        RealEstate realEstateForSale = agent.getRealEstates().stream().filter(a->a.getName().equals(node.get("realestatename").asText())).findFirst().orElseThrow();
        realEstateForSale.setSoldSign(true);
        return realEstateForSale;
    }

    @Override
    public String getTopFiveViewed() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonAnswer = "";
        try {
            jsonAnswer = mapper.writeValueAsString(realEstateRepository.findAll().stream()
                    .sorted(Comparator.comparingInt(RealEstate::getViews))
                    .limit(5).collect(Collectors.toSet()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonAnswer;
    }
}
