package com.gmail.filoghost.holographicdisplays.util.message;

import com.gmail.filoghost.holographicdisplays.util.Validator;
import com.google.gson.JsonObject;

public class FancyAction {

	private String name;
	private String data;

	public FancyAction(String name, String data) {
		Validator.notNull(name, "Action name can't be null!");
		Validator.notNull(data, "Action data can't be null!");
		this.name = name;
		this.data = data;
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.addProperty("action", name);
		json.addProperty("value", data);
		return json;
	}
}
