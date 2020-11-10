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
import me.filoghost.holographicdisplays.event.NamedHologramEditedEvent;
import me.filoghost.holographicdisplays.exception.CommandException;
import me.filoghost.holographicdisplays.object.NamedHologram;
import me.filoghost.holographicdisplays.object.line.CraftHologramLine;
import me.filoghost.holographicdisplays.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class SetlineCommand extends HologramSubCommand {

    public SetlineCommand() {
        super("setline");
        setPermission(Strings.BASE_PERM + "setline");
    }

    @Override
    public String getPossibleArguments() {
        return "<hologramName> <lineNumber> <newText>";
    }

    @Override
    public int getMinimumArguments() {
        return 3;
    }


    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        NamedHologram hologram = CommandValidator.getNamedHologram(args[0]);
        String serializedLine = Utils.join(args, " ", 2, args.length);
        
        int lineNumber = CommandValidator.getInteger(args[1]);
        CommandValidator.isTrue(lineNumber >= 1 && lineNumber <= hologram.size(), "The line number must be between 1 and " + hologram.size() + ".");
        int index = lineNumber - 1;
        
        CraftHologramLine line = CommandValidator.parseHologramLine(hologram, serializedLine, true);
        
        hologram.getLinesUnsafe().get(index).despawn();
        hologram.getLinesUnsafe().set(index, line);
        hologram.refreshAll();

        HologramDatabase.saveHologram(hologram);
        HologramDatabase.trySaveToDisk();
        Bukkit.getPluginManager().callEvent(new NamedHologramEditedEvent(hologram));
        
        sender.sendMessage(Colors.PRIMARY + "Line " + lineNumber + " changed!");
        EditCommand.sendQuickEditCommands(sender, label, hologram.getName());
        
    }

    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Changes a line of a hologram.");
    }
    
    @Override
    public SubCommandType getType() {
        return SubCommandType.EDIT_LINES;
    }

}
