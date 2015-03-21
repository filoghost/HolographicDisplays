package com.gmail.filoghost.holographicdisplays.nms.v1_6_R3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.com.google.gson.stream.JsonWriter;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.nms.interfaces.FancyMessage;

public class FancyMessageImpl implements FancyMessage {
	
	private final List<MessagePart> messageParts;
	
	public FancyMessageImpl(final String firstPartText) {
		messageParts = new ArrayList<MessagePart>();
		messageParts.add(new MessagePart(firstPartText));
	}
	
	@Override
	public FancyMessageImpl color(final ChatColor color) {
		if (!color.isColor()) {
			throw new IllegalArgumentException(color.name() + " is not a color");
		}
		latest().color = color;
		return this;
	}
	
	@Override
	public FancyMessageImpl style(final ChatColor... styles) {
		for (final ChatColor style : styles) {
			if (!style.isFormat()) {
				throw new IllegalArgumentException(style.name() + " is not a style");
			}
		}
		latest().styles = styles;
		return this;
	}
	
	@Override
	public FancyMessageImpl file(final String path) {
		return this;
	}
	
	@Override
	public FancyMessageImpl link(final String url) {
		return this;
	}
	
	@Override
	public FancyMessageImpl suggest(final String command) {
		return this;
	}
	
	@Override
	public FancyMessageImpl command(final String command) {
		return this;
	}
	
	@Override
	public FancyMessageImpl tooltip(final String text) {
		return this;
	}

	@Override
	public FancyMessageImpl then(final Object obj) {
		messageParts.add(new MessagePart(obj.toString()));
		return this;
	}
	
	@Override
	public String toJSONString() {
		StringBuilder sb = new StringBuilder();
		for (MessagePart part : messageParts) {
			if (part.color != null) {
				sb.append(part.color.toString());
			}
			if (part.styles != null && part.styles.length > 0) {
				for (ChatColor style : part.styles) {
					sb.append(style.toString());
				}
			}
			sb.append(part.text);
		}
		return sb.toString();
	}
	
	@Override
	public void send(Player player) {
		player.sendMessage(toJSONString());
	}
	
	private MessagePart latest() {
		return messageParts.get(messageParts.size() - 1);
	}
	
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
