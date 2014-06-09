package com.gmail.filoghost.holograms.nms.interfaces;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.JSONException;
import org.json.JSONWriter;

public interface FancyMessage {

	public FancyMessage color(final ChatColor color);

	public FancyMessage style(final ChatColor... styles);
	
	public FancyMessage file(final String path);
	
	public FancyMessage link(final String url);
	
	public FancyMessage suggest(final String command);
	
	public FancyMessage command(final String command);
	
	public FancyMessage achievementTooltip(final String name);
	
	public FancyMessage itemTooltip(final String itemJSON);
	
	public FancyMessage itemTooltip(final ItemStack itemStack);
	
	public FancyMessage tooltip(final String text);

	public FancyMessage then(final Object obj);
	
	public String toJSONString();
	
	public void send(Player player);
	
	public static class MessagePart {

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
		
		public JSONWriter writeJson(final JSONWriter json) throws JSONException {
			json.object().key("text").value(text);
			if (color != null) {
				json.key("color").value(color.name().toLowerCase());
			}
			if (styles != null) {
				for (final ChatColor style : styles) {
					json.key(style == ChatColor.UNDERLINE ? "underlined" : style.name().toLowerCase()).value(true);
				}
			}
			if (clickActionName != null && clickActionData != null) {
				json.key("clickEvent")
					.object()
						.key("action").value(clickActionName)
						.key("value").value(clickActionData)
					.endObject();
			}
			if (hoverActionName != null && hoverActionData != null) {
				json.key("hoverEvent")
					.object()
						.key("action").value(hoverActionName)
						.key("value").value(hoverActionData)
					.endObject();
			}
			return json.endObject();
		}
		
	}
	
}
