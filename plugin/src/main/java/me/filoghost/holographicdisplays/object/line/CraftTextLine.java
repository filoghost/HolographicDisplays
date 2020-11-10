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
package me.filoghost.holographicdisplays.object.line;

import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.api.handler.TouchHandler;
import me.filoghost.holographicdisplays.api.line.TextLine;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSArmorStand;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSNameable;
import me.filoghost.holographicdisplays.object.CraftHologram;
import me.filoghost.holographicdisplays.placeholder.PlaceholdersManager;
import me.filoghost.holographicdisplays.placeholder.RelativePlaceholder;
import me.filoghost.holographicdisplays.util.Offsets;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CraftTextLine extends CraftTouchableLine implements TextLine {

    private String text;
    private List<RelativePlaceholder> relativePlaceholders;
    private NMSArmorStand nmsNameable;
    
    
    public CraftTextLine(CraftHologram parent, String text) {
        super(0.23, parent);
        setText(text);
    }
    
    
    @Override
    public String getText() {
        return text;
    }
    
    @Override
    public void setText(String text) {
        this.text = text;
        
        if (nmsNameable != null) {
            if (text != null && !text.isEmpty()) {
                nmsNameable.setCustomNameNMS(text);
                if (getParent().isAllowPlaceholders()) {
                    PlaceholdersManager.trackIfNecessary(this);
                }
            } else {
                nmsNameable.setCustomNameNMS(""); // It will not appear
                if (getParent().isAllowPlaceholders()) {
                    PlaceholdersManager.untrack(this);
                }
            }
        }
        
        if (text != null) {
            for (RelativePlaceholder relativePlaceholder : RelativePlaceholder.getRegistry()) {
                if (text.contains(relativePlaceholder.getTextPlaceholder())) {
                    if (relativePlaceholders == null) {
                        relativePlaceholders = new ArrayList<>();
                    }
                    relativePlaceholders.add(relativePlaceholder);
                }
            }
        }
        
        // Deallocate the list if unused
        if (relativePlaceholders != null && relativePlaceholders.isEmpty()) {
            relativePlaceholders = null;
        }
    }
    
    @Override
    public void setTouchHandler(TouchHandler touchHandler) {
        if (nmsNameable != null) {
            Location loc = nmsNameable.getBukkitEntityNMS().getLocation();
            super.setTouchHandler(touchHandler, loc.getWorld(), loc.getX(), loc.getY() - getTextOffset(), loc.getZ());
        } else {
            super.setTouchHandler(touchHandler, null, 0, 0, 0);
        }
    }

    @Override
    public void spawn(World world, double x, double y, double z) {
        super.spawn(world, x, y, z);
            
        nmsNameable = HolographicDisplays.getNMSManager().spawnNMSArmorStand(world, x, y + getTextOffset(), z, this, HolographicDisplays.hasProtocolLibHook());

        if (text != null && !text.isEmpty()) {
            nmsNameable.setCustomNameNMS(text);
        }
    }

    
    @Override
    public void despawn() {
        super.despawn();
        
        if (nmsNameable != null) {
            nmsNameable.killEntityNMS();
            nmsNameable = null;
        }
    }
    
    
    @Override
    public Collection<RelativePlaceholder> getRelativePlaceholders() {
        return relativePlaceholders;
    }

    
    @Override
    public void teleport(double x, double y, double z) {
        super.teleport(x, y, z);
        
        if (nmsNameable != null) {
            nmsNameable.setLocationNMS(x, y + getTextOffset(), z, HolographicDisplays.hasProtocolLibHook());
        }
    }
    
    @Override
    public int[] getEntitiesIDs() {
        if (isSpawned()) {
            if (touchSlime != null) {
                return ArrayUtils.add(touchSlime.getEntitiesIDs(), nmsNameable.getIdNMS());
            } else {
                return new int[] {nmsNameable.getIdNMS()};
            }
        } else {
            return new int[0];
        }
    }

    public NMSNameable getNmsNameable() {
        return nmsNameable;
    }

    private double getTextOffset() {
        return Offsets.ARMOR_STAND_ALONE;
    }

    @Override
    public String toString() {
        return "CraftTextLine [text=" + text + "]";
    }
    
}
