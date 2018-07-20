package com.gmail.filoghost.holographicdisplays.nms.interfaces;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

// TODO: javadocs
public interface FancyMessage {

    FancyMessage color(final ChatColor color);

    FancyMessage style(final ChatColor... styles);

    FancyMessage file(final String path);

    FancyMessage link(final String url);

    FancyMessage suggest(final String command);

    FancyMessage command(final String command);

    FancyMessage tooltip(final String text);

    FancyMessage then(final Object obj);

    String toJSONString();

    void send(Player player);
}
