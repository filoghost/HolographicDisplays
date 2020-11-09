/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.holographicdisplays.placeholder;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;

public abstract class RelativePlaceholder {
	
	private static final Collection<RelativePlaceholder> registry = new HashSet<>();
	
	// The placeholder itself, something like {player}.
	private final String textPlaceholder;
	
	public RelativePlaceholder(String textPlaceholder) {
		this.textPlaceholder = textPlaceholder;
	}
	
	public String getTextPlaceholder() {
		return textPlaceholder;
	}
	
	public abstract String getReplacement(Player player);
	
	public static void register(RelativePlaceholder relativePlaceholder) {
		for (RelativePlaceholder existingPlaceholder : registry) {
			if (existingPlaceholder.getTextPlaceholder().equals(relativePlaceholder.getTextPlaceholder())) {
				throw new IllegalArgumentException("Relative placeholder already registered.");
			}
		}
		
		registry.add(relativePlaceholder);
	}
	
	public static Collection<RelativePlaceholder> getRegistry() {
		return registry;
	}
	
	static {
		register(new RelativePlaceholder("{player}") {
			
			@Override
			public String getReplacement(Player player) {
				return player.getName();
			}
		});
		
		register(new RelativePlaceholder("{displayname}") {
			
			@Override
			public String getReplacement(Player player) {
				return player.getDisplayName();
			}
		});
	}

}
