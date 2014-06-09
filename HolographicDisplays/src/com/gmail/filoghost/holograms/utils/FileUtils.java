package com.gmail.filoghost.holograms.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.gmail.filoghost.holograms.HolographicDisplays;
import com.gmail.filoghost.holograms.exception.UnreadableImageException;

public class FileUtils {

	public static List<String> readLines(String fileName) throws FileNotFoundException, IOException, Exception {
		return readLines( new File(HolographicDisplays.getInstance().getDataFolder(), fileName));
	}
	
	public static List<String> readLines(File file) throws FileNotFoundException, IOException, Exception {

		BufferedReader br = null;

		try {

			List<String> lines = new ArrayList<String>();

			if (!file.exists()) {
				throw new FileNotFoundException();
			}

			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();

			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}

			return lines;

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
					HolographicDisplays.getInstance().getLogger().severe("Unable to close file stream!");
				}
			}
		}
	}
	
	public static BufferedImage readImage(String fileName) throws FileNotFoundException, UnreadableImageException, IOException, Exception {

		File file = new File(HolographicDisplays.getInstance().getDataFolder(), fileName);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
			
		BufferedImage image = ImageIO.read(file);
		
		if (image == null) {
			throw new UnreadableImageException();
		}
			
		return image;
	}
}
