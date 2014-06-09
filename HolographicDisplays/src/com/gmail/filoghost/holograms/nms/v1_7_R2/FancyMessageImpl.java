package com.gmail.filoghost.holograms.nms.v1_7_R2;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_7_R2.ChatSerializer;
import net.minecraft.server.v1_7_R2.NBTTagCompound;
import net.minecraft.server.v1_7_R2.PacketPlayOutChat;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.JSONException;
import org.json.JSONStringer;

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
		onClick("open_file", path);
		return this;
	}
	
	public FancyMessageImpl link(final String url) {
		onClick("open_url", url);
		return this;
	}
	
	public FancyMessageImpl suggest(final String command) {
		onClick("suggest_command", command);
		return this;
	}
	
	public FancyMessageImpl command(final String command) {
		onClick("run_command", command);
		return this;
	}
	
	public FancyMessageImpl achievementTooltip(final String name) {
		onHover("show_achievement", "achievement." + name);
		return this;
	}
	
	public FancyMessageImpl itemTooltip(final String itemJSON) {
		onHover("show_item", itemJSON);
		return this;
	}
	
	public FancyMessageImpl itemTooltip(final ItemStack itemStack) {
		return itemTooltip(CraftItemStack.asNMSCopy(itemStack).save(new NBTTagCompound()).toString());
	}
	
	public FancyMessageImpl tooltip(final String text) {
		final String[] lines = text.split("\\n");
		if (lines.length <= 1) {
			onHover("show_text", text);
		} else {
			itemTooltip(makeMultilineTooltip(lines));
		}
		return this;
	}

	public FancyMessageImpl then(final Object obj) {
		messageParts.add(new MessagePart(obj.toString()));
		return this;
	}
	
	public String toJSONString() {
		final JSONStringer json = new JSONStringer();
		try {
			if (messageParts.size() == 1) {
				latest().writeJson(json);
			} else {
				json.object().key("text").value("").key("extra").array();
				for (final MessagePart part : messageParts) {
					part.writeJson(json);
				}
				json.endArray().endObject();
			}
		} catch (final JSONException e) {
			throw new RuntimeException("invalid message");
		}
		return json.toString();
	}
	
	public void send(Player player){
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(toJSONString())));
	}
	
	private MessagePart latest() {
		return messageParts.get(messageParts.size() - 1);
	}
	
	private String makeMultilineTooltip(final String[] lines) {
		final JSONStringer json = new JSONStringer();
		try {
			json.object().key("id").value(1);
			json.key("tag").object().key("display").object();
			json.key("Name").value("\\u00A7f" + lines[0].replace("\"", "\\\""));
			json.key("Lore").array();
			for (int i = 1; i < lines.length; i++) {
				final String line = lines[i];
				json.value(line.isEmpty() ? " " : line.replace("\"", "\\\""));
			}
			json.endArray().endObject().endObject().endObject();
		} catch (final JSONException e) {
			throw new RuntimeException("invalid tooltip");
		}
		return json.toString();
	}
	
	private void onClick(final String name, final String data) {
		final MessagePart latest = latest();
		latest.clickActionName = name;
		latest.clickActionData = data;
	}
	
	private void onHover(final String name, final String data) {
		final MessagePart latest = latest();
		latest.hoverActionName = name;
		latest.hoverActionData = data;
	}
	
}
