package com.gmail.filoghost.holographicdisplays.nms.interfaces;

import org.bukkit.ChatColor;
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
	
}
