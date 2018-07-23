package com.gmail.filoghost.holographicdisplays.util.io;

import lombok.experimental.UtilityClass;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * File related utilities.
 * TODO: javadoc
 */
@UtilityClass
public class FileUtils {

	public static List<String> readLines(File file) throws IOException {
		if (!file.isFile()) {
			throw new FileNotFoundException(file.getName());
		}
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		List<String> lines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = br.readLine();
			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}
			return lines;
		}
	}

	public static BufferedImage readImage(File file) throws UnreadableImageException, IOException {
		if (!file.isFile()) {
			throw new FileNotFoundException(file.getName());
		}
		BufferedImage image = ImageIO.read(file);
		if (image == null) {
			throw new UnreadableImageException();
		}
		return image;
	}

	public static BufferedImage readImage(URL url) throws UnreadableImageException, IOException {
		BufferedImage image = ImageIO.read(url);
		if (image == null) {
			throw new UnreadableImageException();
		}
		return image;
	}

	public static boolean isParentFolder(File folder, File file) throws IOException {
		File iteratorFile = file.getCanonicalFile();
		folder = folder.getCanonicalFile();
		while ((iteratorFile = iteratorFile.getParentFile()) != null) {
			if (iteratorFile.equals(folder)) {
				return true;
			}
		}
		return false;
	}
}
