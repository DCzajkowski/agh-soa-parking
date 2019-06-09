package com.gnd.parking.Requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gnd.parking.Contracts.Services.Tickets.PurchaseTicketRequestInterface;

import java.util.Date;

public class PurchaseTicketRequest implements PurchaseTicketRequestInterface {
    @JsonProperty("valid_to")
    private Date validTo;

    @JsonProperty("parking_spot_id")
    private Integer parkingSpotId;

    @Override
    public Integer getParkingSpotId() {
        return parkingSpotId;
    }

    @Override
    public void setParkingSpotId(Integer parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
    }

    @Override
    public Date getValidTo() {
        return validTo;
    }

    @Override
    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

}
