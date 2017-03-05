package com.yegor.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by YegorKost on 27.02.2017.
 */
@Entity
@Table(name = "service_station")
public class ServiceStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_station_id")
    private Integer id;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "serviceStation", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Mechanic> mechanics = new HashSet<>();

    @ManyToMany(mappedBy = "serviceStations", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Car> cars = new HashSet<>();

    public ServiceStation() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Mechanic> getMechanics() {
        return mechanics;
    }

    public void setMechanics(Set<Mechanic> mechanics) {
        this.mechanics = mechanics;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceStation that = (ServiceStation) o;

        if (!id.equals(that.id)) return false;
        return address != null ? address.equals(that.address) : that.address == null;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceStation{" +
                "id=" + id +
                ", address='" + address + '\'' +
                '}';
    }
}
