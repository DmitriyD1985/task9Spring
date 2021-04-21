package com.epam.task9spring.service;

import com.epam.task9spring.model.RealEstate;

public interface RealEstateService {
    RealEstate saveRealEstate(String request);

    String getRealEstateByName(String firstName);

    String getAllRealEstate();

    RealEstate updateRealEstate(String request);

    boolean deleteRealEstate(String name);

    RealEstate sale(String request);

    String getTopFiveViewed();
}
