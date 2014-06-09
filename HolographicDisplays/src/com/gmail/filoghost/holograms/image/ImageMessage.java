package com.gmail.filoghost.holograms.image;

import org.bukkit.ChatColor;

import com.gmail.filoghost.holograms.Configuration;
import com.gmail.filoghost.holograms.exception.TooWideException;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Huge thanks to bobacadodl for this awesome library!
 * Bukkit thread: https://forums.bukkit.org/threads/lib-imagemessage-v2-1-send-images-to-players-via-the-chat.204902
 */
public class ImageMessage {
	
	private static final int MAX_WIDTH = 100;

    private final Color[] colors = {
            new Color(0, 0, 0),
            new Color(0, 0, 170),
            new Color(0, 170, 0),
            new Color(0, 170, 170),
            new Color(170, 0, 0),
            new Color(170, 0, 170),
            new Color(255, 170, 0),
            new Color(170, 170, 170),
            new Color(85, 85, 85),
            new Color(85, 85, 255),
            new Color(85, 255, 85),
            new Color(85, 255, 255),
            new Color(255, 85, 85),
            new Color(255, 85, 255),
            new Color(255, 255, 85),
            new Color(255, 255, 255),
    };

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
    	return toBufferedImage(originalImage.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING));
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

        int index = 0;
        double best = -1;

        for (int i = 0; i < colors.length; i++) {
            if (areIdentical(colors[i], color)) {
                return ChatColor.values()[i];
            }
        }

        for (int i = 0; i < colors.length; i++) {
            double distance = getDistance(color, colors[i]);
            if (distance < best || best == -1) {
                best = distance;
                index = i;
            }
        }

        // Minecraft has 15 colors
        return ChatColor.values()[index];
    }


    public String[] getLines() {
        return lines;
    }
}
