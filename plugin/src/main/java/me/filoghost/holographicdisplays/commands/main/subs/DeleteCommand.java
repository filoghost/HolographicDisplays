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
package me.filoghost.holographicdisplays.commands.main.subs;

import me.filoghost.holographicdisplays.commands.Colors;
import me.filoghost.holographicdisplays.commands.CommandValidator;
import me.filoghost.holographicdisplays.commands.Strings;
import me.filoghost.holographicdisplays.commands.main.HologramSubCommand;
import me.filoghost.holographicdisplays.disk.HologramDatabase;
import me.filoghost.holographicdisplays.exception.CommandException;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.NamedHologramManager;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class DeleteCommand extends HologramSubCommand {

    public DeleteCommand() {
        super("delete", "remove");
        setPermission(Strings.BASE_PERM + "delete");
    }

    @Override
    public String getPossibleArguments() {
        return "<hologramName>";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }



    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        NamedHologram hologram = CommandValidator.getNamedHologram(args[0]);
        
        hologram.delete();
        NamedHologramManager.removeHologram(hologram);
        HologramDatabase.deleteHologram(hologram.getName());
        
        HologramDatabase.trySaveToDisk();
        sender.sendMessage(Colors.PRIMARY + "You deleted the hologram '" + hologram.getName() + "'.");
    }

    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Deletes a hologram. Cannot be undone.");
    }
    
    @Override
    public SubCommandType getType() {
        return SubCommandType.GENERIC;
    }

}
