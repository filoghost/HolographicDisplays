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
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class AlignCommand extends HologramSubCommand {

    public AlignCommand() {
        super("align");
        setPermission(Strings.BASE_PERM + "align");
    }

    @Override
    public String getPossibleArguments() {
        return "<X | Y | Z | XZ> <hologram> <referenceHologram>";
    }

    @Override
    public int getMinimumArguments() {
        return 3;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        NamedHologram hologram = CommandValidator.getNamedHologram(args[1]);
        NamedHologram referenceHologram = CommandValidator.getNamedHologram(args[2]);
        
        CommandValidator.isTrue(hologram != referenceHologram, "The hologram must not be the same!");

        Location loc = hologram.getLocation();
        
        if (args[0].equalsIgnoreCase("x")) {
            loc.setX(referenceHologram.getX());
        } else if (args[0].equalsIgnoreCase("y")) {
            loc.setY(referenceHologram.getY());
        } else if (args[0].equalsIgnoreCase("z")) {
            loc.setZ(referenceHologram.getZ());
        } else if (args[0].equalsIgnoreCase("xz")) {
            loc.setX(referenceHologram.getX());
            loc.setZ(referenceHologram.getZ());
        } else {
            throw new CommandException("You must specify either X, Y, Z or XZ, " + args[0] + " is not a valid axis.");
        }

        hologram.teleport(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
        hologram.despawnEntities();
        hologram.refreshAll();
            
        HologramDatabase.saveHologram(hologram);
        HologramDatabase.trySaveToDisk();
        sender.sendMessage(Colors.PRIMARY + "Hologram \"" + hologram.getName() + "\" aligned to the hologram \"" + referenceHologram.getName() + "\" on the " + args[0].toUpperCase() + " axis.");
    }

    @Override
    public List<String> getTutorial() {
        return Arrays.asList("Aligns the first hologram to the second, in the specified axis.");
    }

    @Override
    public SubCommandType getType() {
        return SubCommandType.GENERIC;
    }

}
