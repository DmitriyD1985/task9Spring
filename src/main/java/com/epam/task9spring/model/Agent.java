package com.epam.task9spring.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "agent")
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "salescount")
    private int salesCount;

    @OneToMany(mappedBy = "agent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<RealEstate> realEstates;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalescount() {
        return salesCount;
    }

    public void setSalescount(int salescount) {
        this.salesCount += salescount;
    }

    public Set<RealEstate> getRealEstates() {
        return realEstates;
    }

    public void setRealEstates(Set<RealEstate> realEstates) {
        this.realEstates = realEstates;
    }

    public Agent(String name) {
        this.name = name;
    }

    public Agent() {
    }

    public Set<RealEstate> getSoldRealEstate() {
        return realEstates.stream().filter(RealEstate::isSoldSign).collect(Collectors.toUnmodifiableSet());
    }
}
