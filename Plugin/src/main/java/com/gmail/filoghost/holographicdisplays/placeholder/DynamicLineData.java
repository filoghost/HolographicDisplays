package com.gmail.filoghost.holographicdisplays.placeholder;

import java.util.Map;
import java.util.Set;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import com.gmail.filoghost.holographicdisplays.nms.interfaces.entity.NMSNameable;
import com.gmail.filoghost.holographicdisplays.util.Utils;
import com.gmail.filoghost.holographicdisplays.util.Validator;

public class DynamicLineData {
	
	private final NMSNameable entity;
	private final String originalName;
	
	private Set<Placeholder> placeholders;
	private final Map<String, Placeholder> animations;
	private final Map<String, PlaceholderReplacer> replacers;
	
	public DynamicLineData(NMSNameable entity, String originalName) {
		Validator.notNull(entity, "entity");
		
		this.entity = entity;
		this.originalName = originalName;
		placeholders = Utils.newSet();
		animations = Utils.newMap();
		replacers = Utils.newMap();
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
