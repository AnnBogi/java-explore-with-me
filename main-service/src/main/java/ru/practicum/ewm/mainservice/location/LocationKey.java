package ru.practicum.ewm.mainservice.location;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class LocationKey implements Serializable {
    private float lat;
    private float lon;
}