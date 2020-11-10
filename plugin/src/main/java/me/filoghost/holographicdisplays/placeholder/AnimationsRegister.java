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

import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.disk.StringConverter;
import me.filoghost.holographicdisplays.util.ConsoleLogger;
import me.filoghost.holographicdisplays.util.FileUtils;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class AnimationsRegister {
    
    // <fileName, lines>
    private final static Map<String, Placeholder> animations = new HashMap<>();
    
    public static void loadAnimations(Plugin plugin) {
        animations.clear();
        
        File animationFolder = new File(plugin.getDataFolder(), "animations");
        if (!animationFolder.isDirectory()) {
            animationFolder.mkdirs();
            plugin.saveResource("animations/example.txt", false);
            return;
        }
        
        for (File file : animationFolder.listFiles()) {
            
            try {
                List<String> lines = FileUtils.readLines(file);
                if (lines.size() == 0) {
                    continue;
                }
                
                double speed = 0.5;
                boolean validSpeedFound = false;
                
                String firstLine = lines.get(0).trim();
                if (firstLine.toLowerCase().startsWith("speed:")) {
                    
                    // Do not consider it.
                    lines.remove(0);
                    
                    firstLine = firstLine.substring("speed:".length()).trim();
                    
                    try {
                        speed = Double.parseDouble(firstLine);
                        validSpeedFound = true;
                    } catch (NumberFormatException e) {    }
                }
                
                if (!validSpeedFound) {
                    ConsoleLogger.log(Level.WARNING, "Could not find a valid 'speed: <number>' in the first line of the file '" + file.getName() + "'. Default speed of 0.5 seconds will be used.");
                }
                
                if (lines.isEmpty()) {
                    lines.add("[No lines: " + file.getName() + "]");
                    ConsoleLogger.log(Level.WARNING, "Could not find any line in '" + file.getName() + "' (excluding the speed). You should add at least one more line.");
                }
                
                // Replace placeholders.
                for (int i = 0; i < lines.size(); i++) {
                    lines.set(i, StringConverter.toReadableFormat(lines.get(i)));
                }
                
                animations.put(file.getName(), new Placeholder(HolographicDisplays.getInstance(), file.getName(), speed, new CyclicPlaceholderReplacer(lines.toArray(new String[lines.size()]))));
                ConsoleLogger.logDebug(Level.INFO, "Successfully loaded animation '"  + file.getName() + "', speed = " + speed + ".");
                
            } catch (Exception e) {
                ConsoleLogger.log(Level.SEVERE, "Couldn't load the file '" + file.getName() + "'!", e);
            }
        }
    }

    
    public static Map<String, Placeholder> getAnimations() {
        return animations;
    }

    public static Placeholder getAnimation(String name) {
        return animations.get(name);
    }
    
}
