package com.gmail.filoghost.holographicdisplays.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;

import com.gmail.filoghost.holographicdisplays.disk.Configuration;
import com.gmail.filoghost.holographicdisplays.exception.TooWideException;
import com.gmail.filoghost.holographicdisplays.util.Utils;

/**
 * Huge thanks to bobacadodl for this awesome library!
 * Bukkit thread: https://forums.bukkit.org/threads/lib-imagemessage-v2-1-send-images-to-players-via-the-chat.204902
 */
public class ImageMessage {
	
	public static final int MAX_WIDTH = 150;

	private static final Map<ChatColor, Color> colorsMap = Utils.newMap();
	
	private static final Map<ChatColor, Color> graysMap = Utils.newMap();
	
	
	static {
		colorsMap.put(ChatColor.DARK_BLUE, new Color(0, 0, 170));
		colorsMap.put(ChatColor.DARK_GREEN, new Color(0, 170, 0));
		colorsMap.put(ChatColor.DARK_AQUA, new Color(0, 170, 170));
		colorsMap.put(ChatColor.DARK_RED, new Color(170, 0, 0));
		colorsMap.put(ChatColor.DARK_PURPLE,  new Color(170, 0, 170));
		colorsMap.put(ChatColor.GOLD, new Color(255, 170, 0));
		colorsMap.put(ChatColor.BLUE, new Color(85, 85, 255));
		colorsMap.put(ChatColor.GREEN, new Color(85, 255, 85));
		colorsMap.put(ChatColor.AQUA, new Color(85, 255, 255));
		colorsMap.put(ChatColor.RED, new Color(255, 85, 85));
		colorsMap.put(ChatColor.LIGHT_PURPLE, new Color(255, 85, 255));
		colorsMap.put(ChatColor.YELLOW, new Color(255, 255, 85));
		
		graysMap.put(ChatColor.BLACK, new Color(0, 0, 0));
		graysMap.put(ChatColor.DARK_GRAY, new Color(85, 85, 85));
		graysMap.put(ChatColor.GRAY, new Color(170, 170, 170));
		graysMap.put(ChatColor.WHITE, new Color(255, 255, 255));
	}
	

    private String[] lines;

    public ImageMessage(BufferedImage image, int width) throws TooWideException {
        ChatColor[][] chatColors = toChatColorArray(image, width);
        lines = toImgMessage(chatColors);
    }

    private ChatColor[][] toChatColorArray(BufferedImage image, int width) throws TooWideException {
        double ratio = (double) image.getHeight() / image.getWidth();
        int height = (int) (((double)width) * ratio);
        if (height == 0) {
        	height = 1;
        }
        
        if (width > MAX_WIDTH) {
        	throw new TooWideException(width);
        }

        BufferedImage resized = resizeImage(image, width, height);
        
        ChatColor[][] chatImg = new ChatColor[resized.getWidth()][resized.getHeight()];
        for (int x = 0; x < resized.getWidth(); x++) {
            for (int y = 0; y < resized.getHeight(); y++) {
                int rgb = resized.getRGB(x, y);
                chatImg[x][y] = getClosestChatColor(new Color(rgb, true));
            }
        }
        return chatImg;
    }

    private String[] toImgMessage(ChatColor[][] colors) {
    	
        String[] lines = new String[colors[0].length];
        ChatColor transparencyColor = Configuration.transparencyColor;
        String transparencySymbol = Configuration.transparencySymbol;
        String imageSymbol = Configuration.imageSymbol;
        
        for (int y = 0; y < colors[0].length; y++) {
        	
            StringBuffer line = new StringBuffer();
            
            ChatColor previous = ChatColor.RESET;
            
            for (int x = 0; x < colors.length; x++) {
            	
                ChatColor currentColor = colors[x][y];
                
                if (currentColor == null) {
                	
                	// Use the trasparent char
                	if (previous != transparencyColor) {
                		
                		// Change the previous chat color and append the newer
                		line.append(transparencyColor);
                		previous = transparencyColor;
                		
                	}
                	line.append(transparencySymbol);
                	
                } else {
                	
                	if (previous != currentColor) {
                		line.append(currentColor.toString());
                		previous = currentColor;
                	}
                	
                	line.append(imageSymbol);
                }
            }
            
            lines[y] = line.toString();
        }
        
        return lines;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
    	return toBufferedImage(originalImage.getScaledInstance(width, height, Image.SCALE_DEFAULT));
    }
    
    private BufferedImage toBufferedImage(Image img) {

        // Creates a buffered image with transparency.
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draws the image on to the buffered image.
        Graphics2D graphics = bimage.createGraphics();
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();

        // Returns the buffered image.
        return bimage;
    }

    private double getDistance(Color c1, Color c2) {
        double rmean = (c1.getRed() + c2.getRed()) / 2.0;
        double r = c1.getRed() - c2.getRed();
        double g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        double weightR = 2 + rmean / 256.0;
        double weightG = 4.0;
        double weightB = 2 + (255 - rmean) / 256.0;
        return weightR * r * r + weightG * g * g + weightB * b * b;
    }

    private boolean areIdentical(Color c1, Color c2) {
        return Math.abs(c1.getRed() - c2.getRed()) <= 5 &&
               Math.abs(c1.getGreen() - c2.getGreen()) <= 5 &&
               Math.abs(c1.getBlue() - c2.getBlue()) <= 5;

    }

    private ChatColor getClosestChatColor(Color color) {
    	if (color.getAlpha() < 80) return null;

        for (Entry<ChatColor, Color> entry : colorsMap.entrySet()) {
        	if (areIdentical(entry.getValue(), color)) {
        		return entry.getKey();
        	}
        }
        
        double bestGrayDistance = -1;
        ChatColor bestGrayMatch = null;
        
        for (Entry<ChatColor, Color> entry : graysMap.entrySet()) {
        	double distance = getDistance(color, entry.getValue());
        	
        	if (distance < bestGrayDistance || bestGrayDistance == -1) {
                bestGrayDistance = distance;
                bestGrayMatch = entry.getKey();
            }
        }
        
        if (bestGrayDistance < 17500) {
        	return bestGrayMatch;
        }
        
        double bestColorDistance = -1;
        ChatColor bestColorMatch = null;
        
        for (Entry<ChatColor, Color> entry : colorsMap.entrySet()) {
        	double distance = getDistance(color, entry.getValue());
        	
        	if (distance < bestColorDistance || bestColorDistance == -1) {
                bestColorDistance = distance;
                bestColorMatch = entry.getKey();
            }
        }

        // Minecraft has 15 colors
        return bestColorMatch;
    }


    public String[] getLines() {
        return lines;
    }
}
