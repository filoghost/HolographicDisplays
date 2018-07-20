package com.gmail.filoghost.holographicdisplays.nms.interfaces.entity;

public interface NMSCanMount extends NMSEntityBase {

    /**
     * Sets the passenger of this entity through NMS.
     *
     * @param vehicleBase the new passenger.
     */
    void setPassengerOfNMS(NMSEntityBase vehicleBase);
}
