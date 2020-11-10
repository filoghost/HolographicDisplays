/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.bridge.protocollib.current;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Serializer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import me.filoghost.holographicdisplays.util.NMSVersion;

import java.util.List;
import java.util.Optional;

public class MetadataHelper {
    
    private Serializer itemSerializer;
    private Serializer intSerializer;
    private Serializer byteSerializer;
    private Serializer stringSerializer;
    private Serializer booleanSerializer;
    private Serializer chatComponentSerializer;

    private int itemSlotIndex;
    private int entityStatusIndex;
    private int airLevelWatcherIndex;
    private int customNameIndex;
    private int customNameVisibleIndex;
    private int noGravityIndex;
    private int armorStandStatusIndex;
    private int slimeSizeIndex;
    
    
    public MetadataHelper() {
        if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_14_R1)) {
            itemSlotIndex = 7;
        } else if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_10_R1)) {
            itemSlotIndex = 6;
        } else if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_9_R1)) {
            itemSlotIndex = 5;
        } else {
            itemSlotIndex = 10;
        }
        
        if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_15_R1)) {
            armorStandStatusIndex = 14;
        } else {
            armorStandStatusIndex = 11;
        }
        
        entityStatusIndex = 0;
        airLevelWatcherIndex = 1;
        customNameIndex = 2;
        customNameVisibleIndex = 3;
        noGravityIndex = 5;
        slimeSizeIndex = 15;
        
        if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_9_R1)) {
            itemSerializer = Registry.get(MinecraftReflection.getItemStackClass());
            intSerializer = Registry.get(Integer.class);
            byteSerializer = Registry.get(Byte.class);
            stringSerializer = Registry.get(String.class);
            booleanSerializer = Registry.get(Boolean.class);
        }
        
        if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_13_R1)) {
            chatComponentSerializer = Registry.get(MinecraftReflection.getIChatBaseComponentClass(), true);
        }
    }
    
    
    public void setEntityStatus(WrappedDataWatcher dataWatcher, byte statusBitmask) {
        requireMinimumVersion(NMSVersion.v1_9_R1);
        dataWatcher.setObject(new WrappedDataWatcherObject(entityStatusIndex, byteSerializer), statusBitmask);
    }
    

    public WrappedWatchableObject getCustomNameWacthableObject(WrappedDataWatcher metadata) {
        return metadata.getWatchableObject(customNameIndex);
    }
    
    
    public WrappedWatchableObject getCustomNameWatchableObject(List<WrappedWatchableObject> dataWatcherValues) {
        for (int i = 0; i < dataWatcherValues.size(); i++) {
            WrappedWatchableObject watchableObject = dataWatcherValues.get(i);
            
            if (watchableObject.getIndex() == customNameIndex) {
                return watchableObject;
            }
        }
        
        return null;
    }
    
    
    public Object getCustomNameNMSObject(WrappedWatchableObject customNameWatchableObject) {
        Object customNameNMSObject = customNameWatchableObject.getRawValue();
        if (customNameNMSObject == null) {
            return null;
        }
        
        if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_13_R1)) {
            if (!(customNameNMSObject instanceof Optional)) {
                throw new IllegalArgumentException("Expected custom name of type " + Optional.class);
            }
            
            return ((Optional<?>) customNameNMSObject).orElse(null);
            
        } else {            
            if (!(customNameNMSObject instanceof String)) {
                throw new IllegalArgumentException("Expected custom name of type " + String.class);
            }
            
            return (String) customNameNMSObject;
        }
    }
    
    
    public void setCustomNameNMSObject(WrappedWatchableObject customNameWatchableObject, Object customNameNMSObject) {
        if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_13_R1)) {
            customNameWatchableObject.setValue(Optional.ofNullable(customNameNMSObject));
        } else {
            customNameWatchableObject.setValue(customNameNMSObject);
        }
    }

    
    public void setCustomNameNMSObject(WrappedDataWatcher dataWatcher, Object customNameNMSObject) {
        requireMinimumVersion(NMSVersion.v1_9_R1);
        
        if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_13_R1)) {
            dataWatcher.setObject(new WrappedDataWatcherObject(customNameIndex, chatComponentSerializer), Optional.ofNullable(customNameNMSObject));
        } else {
            dataWatcher.setObject(new WrappedDataWatcherObject(customNameIndex, stringSerializer), customNameNMSObject);
        }
    }

    
    public void setCustomNameVisible(WrappedDataWatcher dataWatcher, boolean customNameVisible) {
        requireMinimumVersion(NMSVersion.v1_9_R1);
        dataWatcher.setObject(new WrappedDataWatcherObject(customNameVisibleIndex, booleanSerializer), customNameVisible);
    }

    
    public void setNoGravity(WrappedDataWatcher dataWatcher, boolean noGravity) {
        requireMinimumVersion(NMSVersion.v1_9_R1);
        dataWatcher.setObject(new WrappedDataWatcherObject(noGravityIndex, booleanSerializer), noGravity);
    }

    
    public void setArmorStandStatus(WrappedDataWatcher dataWatcher, byte statusBitmask) {
        requireMinimumVersion(NMSVersion.v1_9_R1);
        dataWatcher.setObject(new WrappedDataWatcherObject(armorStandStatusIndex, byteSerializer), statusBitmask);
    }

    
    public void setItemMetadata(WrappedDataWatcher dataWatcher, Object nmsItemStack) {
        if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_9_R1)) {
            if (NMSVersion.isGreaterEqualThan(NMSVersion.v1_11_R1)) {
                dataWatcher.setObject(new WrappedDataWatcherObject(itemSlotIndex, itemSerializer), nmsItemStack);
            } else {
                dataWatcher.setObject(new WrappedDataWatcherObject(itemSlotIndex, itemSerializer), com.google.common.base.Optional.of(nmsItemStack));
            }
            dataWatcher.setObject(new WrappedDataWatcherObject(airLevelWatcherIndex, intSerializer), 300);
            dataWatcher.setObject(new WrappedDataWatcherObject(entityStatusIndex, byteSerializer), (byte) 0);
        } else {
            dataWatcher.setObject(itemSlotIndex, nmsItemStack);
            dataWatcher.setObject(airLevelWatcherIndex, 300);
            dataWatcher.setObject(entityStatusIndex, (byte) 0);
        }
    }
    
    
    public void setSlimeSize(WrappedDataWatcher dataWatcher, int size) {
        requireMinimumVersion(NMSVersion.v1_15_R1);
        dataWatcher.setObject(new WrappedDataWatcherObject(slimeSizeIndex, intSerializer), size);
    }
    
    
    private static void requireMinimumVersion(NMSVersion minimumVersion) {
        if (!NMSVersion.isGreaterEqualThan(minimumVersion)) {
            throw new UnsupportedOperationException("Method only available from NMS version " + minimumVersion);
        }
    }
    
}
