package com.epam.task9spring.controller;

import com.epam.task9spring.service.RealEstateService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/authorized/realestate")
@RestController
public class RealEstateController {

    RealEstateService realEstateService;

    public RealEstateController(RealEstateService realEstateService) {
        this.realEstateService = realEstateService;
    }

    @PostMapping("/user/create")
    public String registrationRealEstate(@RequestBody String request) {
        realEstateService.saveRealEstate(request);
        return "{\"msg\":\"здание добавлено\"}";
    }

    @GetMapping("/user/read/{name}")
    public String getOneRealEstate(@PathVariable String name) {
        realEstateService.getRealEstateByName(name);
        return "{\"msg\":\"Получите одно здание\"}";
    }

    @PostMapping("/admin/update")
    public String updateRealEstate(@RequestBody String request) {
        realEstateService.updateRealEstate(request);
        return "{\"msg\":\"RealEstate updated\"}";
    }

    @DeleteMapping("/admin/delete")
    public String deleteRealEstate(@RequestBody String request) {
        if (realEstateService.deleteRealEstate(request))
            return "{\"msg\":\"Здание удалено\"}";
        else
            return "{\"msg\":\"Здание не удалено\"}";
    }

    @GetMapping("/admin/all")
    public String getAllRealEstate() {
          return "{\"msg\":"+realEstateService.getAllRealEstate()+"}";
    }

    @PostMapping("/admin/makesale")
    public String makeSale(@RequestBody String request) {
        realEstateService.sale(request);
        return "{\"msg\":\"Здание продано}";
    }

    @GetMapping("/admin/topfiveviewed")
    public String getTopFiveViewed() {
        return "{\"msg\":"+realEstateService.getTopFiveViewed()+"}";
    }
}
