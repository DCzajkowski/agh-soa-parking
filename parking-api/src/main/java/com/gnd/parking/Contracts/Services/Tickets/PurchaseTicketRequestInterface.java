package com.gnd.parking.Contracts.Services.Tickets;

import java.io.Serializable;
import java.util.Date;

public interface PurchaseTicketRequestInterface extends Serializable {
    Integer getParkingSpotId();

    void setParkingSpotId(Integer parkingSpotId);

    Date getValidTo();

    void setValidTo(Date validTo);
}
