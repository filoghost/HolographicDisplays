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
package me.filoghost.holographicdisplays.commands.main;

import me.filoghost.holographicdisplays.exception.CommandException;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class HologramSubCommand {
    
    private String name;
    private String permission;
    private String[] aliases;
    
    public HologramSubCommand(String name) {
        this(name, new String[0]);
    }
    
    public HologramSubCommand(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }
    
    public String getName() {
        return name;
    }
    
    public void setPermission(String permission) {
        this.permission = permission;
    }
    
    public String getPermission() {
        return permission;
    }
    
    public final boolean hasPermission(CommandSender sender) {
        if (permission == null) return true;
        return sender.hasPermission(permission);
    }
    
    public abstract String getPossibleArguments();

    public abstract int getMinimumArguments();

    public abstract void execute(CommandSender sender, String label, String[] args) throws CommandException;
    
    public abstract List<String> getTutorial();
    
    public abstract SubCommandType getType();
    
    public enum SubCommandType {
        GENERIC, EDIT_LINES, HIDDEN
    }
    
    
    public final boolean isValidTrigger(String name) {
        if (this.name.equalsIgnoreCase(name)) {
            return true;
        }
        
        if (aliases != null) {
            for (String alias : aliases) {
                if (alias.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        
        return false;
    }

}
