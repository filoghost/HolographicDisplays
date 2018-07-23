package com.gmail.filoghost.holographicdisplays.util.message;

import com.gmail.filoghost.holographicdisplays.util.Validator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class FancyMessage {

	private List<FancyComponent> components;

	private FancyMessage(List<FancyComponent> components) {
		this.components = components;
	}

	public static class FancyMessageBuilder {
		private List<FancyComponent> components;

		private FancyMessageBuilder() {
			components = new ArrayList<>();
		}

		public FancyMessageBuilder component(FancyComponent component) {
			Validator.notNull(component, "Component can't be null!");
			components.add(component);
			return this;
		}

		public FancyMessage build() {
			return new FancyMessage(components);
		}
	}

	public static FancyMessageBuilder builder() {
		return new FancyMessageBuilder();
	}

	public JsonObject toJson() {
		if (components.size() == 1) {
			return components.get(0).toJson();
		}
		JsonObject json = new JsonObject();
		json.addProperty("text", "");
		if (!components.isEmpty()) {
			JsonArray extra = new JsonArray();
			components.forEach(component -> extra.add(component.toJson()));
			json.add("extra", extra);
		}
		return json;
	}
}
