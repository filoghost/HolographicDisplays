package com.gmail.filoghost.holographicdisplays.util.message;

import com.gmail.filoghost.holographicdisplays.util.Validator;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;

import java.util.Collection;

public class FancyComponent {

	private String text;

	// Formatting
	private ChatColor color;
	private ChatColor[] style;
	private FancyAction clickAction;
	private FancyAction hoverAction;

	private FancyComponent(String text, ChatColor color, ChatColor[] style, FancyAction clickAction, FancyAction hoverAction) {
		this.text = text;
		this.color = color;
		this.style = style;
		this.clickAction = clickAction;
		this.hoverAction = hoverAction;
	}

	public static class FancyComponentBuilder {
		private String text;
		private ChatColor color;
		private ChatColor[] style;
		private FancyAction clickAction;
		private FancyAction hoverAction;

		public FancyComponentBuilder text(String text) {
			this.text = text;
			return this;
		}

		public FancyComponentBuilder color(ChatColor color) {
			this.color = color;
			return this;
		}

		public FancyComponentBuilder style(ChatColor... style) {
			this.style = style;
			return this;
		}

		private void onClick(String name, String data) {
			clickAction = new FancyAction(name, data);
		}

		private void onHover(String name, String data) {
			hoverAction = new FancyAction(name, data);
		}

		public FancyComponentBuilder file(String path) {
			onClick("open_file", path);
			return this;
		}

		public FancyComponentBuilder link(String url) {
			onClick("open_url", url);
			return this;
		}

		public FancyComponentBuilder suggest(String command) {
			onClick("suggest_command", command);
			return this;
		}

		public FancyComponentBuilder command(String command) {
			onClick("run_command", command);
			return this;
		}

		public FancyComponentBuilder tooltip(String text) {
			onHover("show_text", text);
			return this;
		}

		public FancyComponentBuilder tooltip(Collection<String> lines) {
			onHover("show_text", String.join("/n", lines));
			return this;
		}

		public FancyComponent build() {
			Validator.notNull(text, "The component text must be defined!");
			return new FancyComponent(text, color, style, clickAction, hoverAction);
		}
	}

	public static FancyComponentBuilder builder() {
		return new FancyComponentBuilder();
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.addProperty("text", text);
		if (color != null) {
			json.addProperty("color", color.name().toLowerCase());
		}
		if (style != null) {
			for (ChatColor current : style) {
				json.addProperty(current == ChatColor.UNDERLINE ? "underlined" : current.name().toLowerCase(), true);
			}
		}
		if (clickAction != null) {
			json.add("clickEvent", clickAction.toJson());
		}
		if (hoverAction != null) {
			json.add("hoverEvent", hoverAction.toJson());
		}
		return json;
	}
}
