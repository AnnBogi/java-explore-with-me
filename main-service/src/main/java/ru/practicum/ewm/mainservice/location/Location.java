package ru.practicum.ewm.mainservice.location;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(LocationKey.class)
@Getter
@Setter
public class Location {

    @Id
    private Float lat;

    @Id
    private Float lon;

}