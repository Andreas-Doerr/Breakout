package de.tudarmstadt.informatik.fop.breakout.handlers;

import java.io.*;

/**
 * Created by PC - Andreas on 21.03.2017.
 *
 * @author Andreas Dörr
 */
public class FileHandler {

	//TODO create folders (highscore) if they do not exist on startup

	public static String[] read(String ref, int noOfEntries) throws FileNotFoundException{

		String[] content = new String[noOfEntries];

		BufferedReader br = new BufferedReader(new FileReader(new File(ref)));
		try {
			int i = 0;
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
					try {
						content[i] = line;
					} catch (ArrayIndexOutOfBoundsException e) {
						System.err.println("ERROR: FileHandler was told the file should be smaller!");
					}

					i++;
				} else {
					br.readLine();
				}
			}
			br.close();
		}catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return content;
	}

	public static void write(String ref, String toWrite) throws FileNotFoundException {
		try {
			BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ref)));
			bwr.write(toWrite);
			bwr.flush();
			bwr.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

}
