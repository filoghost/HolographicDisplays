/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.placeholder;

import me.filoghost.fcommons.Preconditions;
import me.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import me.filoghost.holographicdisplays.core.nms.entity.NMSNameable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DynamicLineData {
    
    private final NMSNameable entity;
    private final String originalName;
    
    private Set<Placeholder> placeholders;
    private final Map<String, Placeholder> animations;
    private final Map<String, PlaceholderReplacer> replacers;
    
    public DynamicLineData(NMSNameable entity, String originalName) {
        Preconditions.notNull(entity, "entity");
        
        this.entity = entity;
        this.originalName = originalName;
        placeholders = new HashSet<>();
        animations = new HashMap<>();
        replacers = new HashMap<>();
    }

    public NMSNameable getEntity() {
        return entity;
    }    

    public String getOriginalName() {
        return originalName;
    }
    
    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<Placeholder> getPlaceholders() {
        return placeholders;
    }

    public Map<String, PlaceholderReplacer> getReplacers() {
        return replacers;
    }
    
    public Map<String, Placeholder> getAnimations() {
        return animations;
    }

    @Override
    public int hashCode() {
        return entity.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DynamicLineData other = (DynamicLineData) obj;
        return this.entity == other.entity;
    }
    
    
    
}
