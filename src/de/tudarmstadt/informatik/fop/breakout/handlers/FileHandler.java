package de.tudarmstadt.informatik.fop.breakout.handlers;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by PC - Andreas on 21.03.2017.
 *
 * @author Andreas Dörr
 */
public class FileHandler {

	//TODO create folders (highscore) if they do not exist on startup

	public static String[] read(String ref) throws IOException {

		List<String> contentList = new LinkedList<>();

		BufferedReader br = new BufferedReader(new FileReader(new File(ref)));
		while (br.ready()) {
			// ignores lines starting with "#"(35)
			int firstChar = br.read();
			if (firstChar != 35) {
				String rest = br.readLine();
				String line;
				if (rest != null) {
					line = (char) firstChar + rest;
				} else {
					line = (char) firstChar + "";
				}
				contentList.add(line);
			} else {
				br.readLine();
			}
		}
		br.close();

		// put the contentList into s String Array
		String[] content = new String[contentList.size()];
		int i = 0;
		for (String line : contentList) {
			content[i] = line;
			i++;
		}

		return content;
	}

	public static void write(String folder, String file, String toWrite) {
		try {
			// create Folder
			File folderPath = new File(folder);
			if (!folderPath.exists() && !folder.equals("")) {
				folderPath.mkdir();
				System.out.println("INFO: created folder:" + folder);
			}

			// write file
			Writer wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(folder + file)));
			wr.write(toWrite);
			wr.close();
		} catch (IOException e) {
			System.err.println("ERROR: could not save file: " + folder + file);
			e.printStackTrace();
		}
	}

}
