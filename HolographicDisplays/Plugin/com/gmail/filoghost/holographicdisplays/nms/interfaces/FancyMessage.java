package com.gmail.filoghost.holographicdisplays.nms.interfaces;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.com.google.gson.stream.JsonWriter;
import org.bukkit.entity.Player;

public interface FancyMessage {

	public FancyMessage color(final ChatColor color);

	public FancyMessage style(final ChatColor... styles);
	
	public FancyMessage file(final String path);
	
	public FancyMessage link(final String url);
	
	public FancyMessage suggest(final String command);
	
	public FancyMessage command(final String command);
	
	public FancyMessage tooltip(final String text);

	public FancyMessage then(final Object obj);
	
	public String toJSONString();
	
	public void send(Player player);
	
	static class MessagePart {

		public ChatColor color = null;
		public ChatColor[] styles = null;
		public String clickActionName = null;
		public String clickActionData = null;
		public String hoverActionName = null;
		public String hoverActionData = null;
		public final String text;
		
		public MessagePart(final String text) {
			this.text = text;
		}
		
		public JsonWriter writeJson(final JsonWriter json) throws IOException {
			json.beginObject().name("text").value(text);
			if (color != null) {
				json.name("color").value(color.name().toLowerCase());
			}
			if (styles != null) {
				for (final ChatColor style : styles) {
					json.name(style == ChatColor.UNDERLINE ? "underlined" : style.name().toLowerCase()).value(true);
				}
			}
			if (clickActionName != null && clickActionData != null) {
				json.name("clickEvent")
					.beginObject()
						.name("action").value(clickActionName)
						.name("value").value(clickActionData)
					.endObject();
			}
			if (hoverActionName != null && hoverActionData != null) {
				json.name("hoverEvent")
					.beginObject()
						.name("action").value(hoverActionName)
						.name("value").value(hoverActionData)
					.endObject();
			}
			return json.endObject();
		}
		
	}
	
}
