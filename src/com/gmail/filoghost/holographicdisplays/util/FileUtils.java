package com.gmail.filoghost.holographicdisplays.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.gmail.filoghost.holographicdisplays.exception.UnreadableImageException;


public class FileUtils {
	
	public static List<String> readLines(File file) throws IOException, Exception {

		if (!file.isFile()) {
			throw new FileNotFoundException(file.getName());
		}
		
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
				} catch (IOException e) { }
			}
		}
	}
	
	public static BufferedImage readImage(File file) throws UnreadableImageException, IOException, Exception {
		
		if (!file.isFile()) {
			throw new FileNotFoundException(file.getName());
		}
		
		BufferedImage image = ImageIO.read(file);
		
		if (image == null) {
			throw new UnreadableImageException();
		}
			
		return image;
	}
	
	public static BufferedImage readImage(URL url) throws UnreadableImageException, IOException, Exception {
		
		BufferedImage image = ImageIO.read(url);
		
		if (image == null) {
			throw new UnreadableImageException();
		}
			
		return image;
	}
}
