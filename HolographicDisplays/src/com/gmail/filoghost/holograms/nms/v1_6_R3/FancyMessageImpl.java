package com.gmail.filoghost.holograms.nms.v1_6_R3;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holograms.nms.interfaces.FancyMessage;

public class FancyMessageImpl implements FancyMessage {
	
	private final List<MessagePart> messageParts;
	
	public FancyMessageImpl(final String firstPartText) {
		messageParts = new ArrayList<MessagePart>();
		messageParts.add(new MessagePart(firstPartText));
	}
	
	public FancyMessageImpl color(final ChatColor color) {
		if (!color.isColor()) {
			throw new IllegalArgumentException(color.name() + " is not a color");
		}
		latest().color = color;
		return this;
	}
	
	public FancyMessageImpl style(final ChatColor... styles) {
		for (final ChatColor style : styles) {
			if (!style.isFormat()) {
				throw new IllegalArgumentException(style.name() + " is not a style");
			}
		}
		latest().styles = styles;
		return this;
	}
	
	public FancyMessageImpl file(final String path) {
		return this;
	}
	
	public FancyMessageImpl link(final String url) {
		return this;
	}
	
	public FancyMessageImpl suggest(final String command) {
		return this;
	}
	
	public FancyMessageImpl command(final String command) {
		return this;
	}
	
	public FancyMessageImpl achievementTooltip(final String name) {
		return this;
	}
	
	public FancyMessageImpl itemTooltip(final String itemJSON) {
		return this;
	}
	
	public FancyMessageImpl itemTooltip(final ItemStack itemStack) {
		return this;
	}
	
	public FancyMessageImpl tooltip(final String text) {
		return this;
	}

	public FancyMessageImpl then(final Object obj) {
		messageParts.add(new MessagePart(obj.toString()));
		return this;
	}
	
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
	
	public void send(Player player) {
		player.sendMessage(toJSONString());
	}
	
	private MessagePart latest() {
		return messageParts.get(messageParts.size() - 1);
	}
}
