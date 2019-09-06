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
package com.gmail.filoghost.holographicdisplays.placeholder;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PatternPlaceholderReplacer;
import org.bukkit.plugin.Plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternPlaceholder {

	// The plugin that owns this placeholder.
	private final Plugin owner;

	// The placeholder itself.
	private final Pattern patternPlaceholder;

	// How many tenths of second between each refresh.
	private int tenthsToRefresh;

	// This is the current replacement for this placeholder.
	private String currentReplacement;

	private PatternPlaceholderReplacer replacer;

	public PatternPlaceholder(Plugin owner, Pattern patternPlaceholder, double refreshRate, PatternPlaceholderReplacer replacer) {
		this.owner = owner;
		this.patternPlaceholder = patternPlaceholder;
		this.tenthsToRefresh = refreshRate <= 0.1 ? 1 : (int) (refreshRate * 10.0);
		this.replacer = replacer;
		this.currentReplacement = "";
	}
	
	public Plugin getOwner() {
		return owner;
	}
	
	public int getTenthsToRefresh() {
		return tenthsToRefresh;
	}
	
	public void setTenthsToRefresh(int tenthsToRefresh) {
		this.tenthsToRefresh = tenthsToRefresh;
	}

	public Pattern getPatternPlaceholder() {
		return patternPlaceholder;
	}

	public String getCurrentReplacement() {
		return currentReplacement;
	}

	public void setCurrentReplacement(String replacement) {
		this.currentReplacement = replacement != null ? replacement : "null";
	}
	
	public PatternPlaceholderReplacer getReplacer() {
		return replacer;
	}
	
	public void setReplacer(PatternPlaceholderReplacer replacer) {
		this.replacer = replacer;
	}

	public void update(Matcher matcher) {
		setCurrentReplacement(replacer.update(matcher));
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (obj instanceof PatternPlaceholder) {
			return ((PatternPlaceholder) obj).patternPlaceholder.equals(this.patternPlaceholder);
		}
		
		return false;
	}
	
	
	@Override
	public int hashCode() {
		return patternPlaceholder.hashCode();
	}
	
	
}
