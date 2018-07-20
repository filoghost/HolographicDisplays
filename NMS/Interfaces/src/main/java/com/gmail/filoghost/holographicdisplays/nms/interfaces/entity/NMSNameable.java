package com.gmail.filoghost.holographicdisplays.nms.interfaces.entity;

public interface NMSNameable extends NMSEntityBase {

    /**
     * Sets a custom name for this entity.
     *
     * @param name the new entity custom name.
     */
    void setCustomNameNMS(String name);

    /**
     * Returns the custom name of this entity.
     *
     * @return  the entity custom name.
     */
    String getCustomNameNMS();
}
