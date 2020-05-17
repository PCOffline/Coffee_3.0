package modules;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Memory Module of the bot.
 * Features read and write methods to text files as well as more advanced methods
 * such as find, replace, write to line, read from and to line.
 */

@SuppressWarnings("unused")
public class Memory {
	
	private static final Logger logger = LoggerFactory.getLogger(Memory.class);
	
	private Memory () {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Read a file and return its contents
	 *
	 * @param path Path of file
	 *
	 * @return Contents of file
	 */
	
	@NotNull
	public static String readFile (String path) {
		StringBuilder raw;
		String line;
		String text = null;
		int count = 0;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			raw  = new StringBuilder();
			line = reader.readLine();
			
			while (line != null) {
				raw.append(line);
				
				if (count < countLines(path))
					raw.append("\n");
				
				line = reader.readLine();
				count++;
			}
			
			text = raw.toString();
			
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		return text != null ? text : "";
	}
	
	/**
	 * Read a file and return a string with all the content of the file until the end
	 *
	 * @param path Path of the file to read
	 * @param end  The last line to read
	 *
	 * @return String with all the content of the file until the end
	 */
	
	@NotNull
	public static String readFile (String path, long end) {
		StringBuilder raw;
		String line;
		String text = null;
		int count = 0;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			raw  = new StringBuilder();
			line = reader.readLine();
			
			while (line != null && count < end) {
				raw.append(line);
				
				if (count < countLines(path))
					raw.append("\n");
				
				line = reader.readLine();
				count++;
			}
			
			text = raw.toString();
			
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		return text != null ? text : "";
	}
	
	/**
	 * Read a file and return a string with all the content of the file from the start
	 *
	 * @param path  Path of the file to read
	 * @param start The first line to read
	 *
	 * @return String with all the content of the file from the start
	 */
	
	@NotNull
	public static String readFile (long start, String path) {
		StringBuilder raw;
		String line;
		String text = null;
		int count = 0;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			raw  = new StringBuilder();
			line = reader.readLine();
			
			while (line != null && count < start - 1) {
				line = reader.readLine();
				count++;
			}
			
			while (line != null) {
				raw.append(line);
				
				if (count < countLines(path))
					raw.append("\n");
				
				line = reader.readLine();
				count++;
			}
			
			text = raw.toString();
			
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		return text != null ? text : "";
	}
	
	@NotNull
	public static String readFile (String path, long start, long end) {
		StringBuilder res = new StringBuilder();
		for (long i = start; i <= end; i++) {
			res.append(goLine(path, i)).append(goLine(path, i).contains("\n") ? "\n" : "");
		}
		return res.toString();
	}
	
	/**
	 * Write a value into the last line of a file
	 *
	 * @param path  Path of the file to write to
	 * @param value The value to write in the file
	 */
	
	public static void writeFile (String path, String value) {
		String file = readFile(path);
		
		try (FileOutputStream outputStream = new FileOutputStream(path)) {
			outputStream.write((file + "\n" + value).getBytes());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Write a value into the selected line
	 *
	 * @param path  Path of the file to write to
	 * @param value The value to write in the file
	 * @param line  line to write the value into the selected line
	 */
	
	public static void writeFile (String path, String value, long line, boolean overwrite) {
		String before;
		if (overwrite)
			before = line > 1 ? readFile(path, line - 1) : "";
		else
			before = line >= 1 ? readFile(path, line) : "";
		
		String after = readFile(line + 1, path);
		
		try (FileOutputStream outputStream = new FileOutputStream(path)) {
			byte[] b = (before + value + "\n" + after).getBytes();
			outputStream.write(b);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Delete line of a file
	 *
	 * @param path Path of the file where the line is deleted
	 * @param line The line number to be deleted
	 */
	
	public static void deleteLine (String path, long line) {
		
		try (FileOutputStream outputStream = new FileOutputStream(path)) {
			outputStream.write((readFile(path, line - 1) + "\n" + readFile(line + 1, path)).getBytes());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Checks if a keyword exists in a file
	 *
	 * @param path          Path of file
	 * @param keyword       The keyword to search
	 * @param ignoreCase    Should the search ignore case or not
	 * @param keywordIsLine Is the keyword a whole line or a part of a line in the file
	 *
	 * @return True if keyword is found
	 */
	
	public static boolean find (String path, String keyword, boolean ignoreCase, boolean keywordIsLine) {
		String line;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			line = reader.readLine();
			
			while (line != null) {
				if (keywordIsLine) {
					if (line.equals(keyword) || ignoreCase && line.equalsIgnoreCase(keyword))
						return true;
				} else if (line.contains(keyword) || line.toLowerCase().contains(keyword.toLowerCase())) {
					return true;
				}
				
				line = reader.readLine();
			}
			
			return false;
		} catch (java.io.IOException e) {
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * Checks if a keyword exists in a file
	 *
	 * @param path          Path of file
	 * @param keyword       The keyword to search
	 * @param ignoreCase    Should the search ignore case or not
	 * @param keywordIsLine Is the keyword a whole line or a part of a line in the file
	 * @param start         Line to start the search from
	 *
	 * @return True if keyword is found
	 */
	
	public static boolean find (String path, String keyword, boolean ignoreCase, boolean keywordIsLine, long start) {
		String line;
		int count = 0;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			line = reader.readLine();
			
			while (line != null) {
				
				while (count < start) {
					line = reader.readLine();
					count++;
				}
				
				if (keywordIsLine) {
					if (line.equals(keyword) || (ignoreCase && line.equalsIgnoreCase(keyword)))
						return true;
				} else if (line.contains(keyword) || line.toLowerCase().contains(keyword.toLowerCase())) {
					return true;
				}
				
				line = reader.readLine();
			}
			
			return false;
		} catch (java.io.IOException e) {
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * Find the line of the first occurrence of a keyword as a whole line or as a part of a line in the file
	 *
	 * @param path          Path of the file to find the line of the keyword in
	 * @param keyword       The keyword to find the line of
	 * @param ignoreCase    Should the search ignore case or not
	 * @param keywordIsLine Is the keyword a whole line or a part of a line in the file
	 *
	 * @return The line of the first occurrence of the keyword as a whole line or as a part of a line in the file
	 */
	
	public static long findLine (String path, String keyword, boolean ignoreCase, boolean keywordIsLine) {
		if (!find(path, keyword, ignoreCase, keywordIsLine))
			return -1;
		
		String line;
		int count = 1;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			
			line = reader.readLine();
			
			while (line != null) {
				if (findInLine(line, keyword, ignoreCase, keywordIsLine))
					return count;
				
				line = reader.readLine();
				count++;
			}
			
			return -1;
		} catch (IOException e) {
			logger.error(e.getMessage());
			return -1;
		}
	}
	
	/**
	 * Searches for a keyword is in line, in accordance to the parameters.
	 *
	 * @param line          the line to search in.
	 * @param keyword       the keyword to search for.
	 * @param ignoreCase    should the search ignore case or not
	 * @param keywordIsLine is the keyword a whole line or a part of a line in the file
	 *
	 * @return whether the keyword is in line or not, in accordance to the parameters.
	 */
	
	public static boolean findInLine (String line, String keyword, boolean ignoreCase, boolean keywordIsLine) {
		if (ignoreCase)
			return keywordIsLine ? line.toLowerCase().contains(keyword.toLowerCase()) : line.equalsIgnoreCase(keyword);
		return keywordIsLine ? line.contains(keyword) : line.equals(keyword);
	}
	
	/**
	 * Find the line of the first occurrence of a keyword as a whole line or as a part of a line in the file from the
	 * line inputted
	 *
	 * @param path          Path of the file to find the line of the keyword in
	 * @param keyword       The keyword to find the line of
	 * @param ignoreCase    Should the search ignore case or not
	 * @param keywordIsLine Is the keyword a whole line or a part of a line in the file
	 * @param start         Line to start from
	 *
	 * @return The line of the first occurrence of the keyword as a whole line or as a part of a line in the file
	 */
	
	public static long findLine (String path, String keyword, boolean ignoreCase, boolean keywordIsLine, long start) {
		if (!find(path, keyword, ignoreCase, keywordIsLine))
			return -1;
		
		String line;
		int count = 1;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			line = startFrom(path, start);
			
			while (line != null) {
				if (findInLine(line, keyword, ignoreCase, keywordIsLine))
					return count;
				
				line = reader.readLine();
				count++;
			}
			
			return -1;
		} catch (IOException e) {
			logger.error(e.getMessage());
			return -1;
		}
	}
	
	private static String startFrom (String path, long start) {
		String line = null;
		int count = 1;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			while (count < start) {
				line = reader.readLine();
				count++;
			}
			
			return line;
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	public static void clearFile (String path) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
			writer.write("");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	public static long findLast (String path, String keyword, boolean ignoreCase, boolean keywordIsLine) {
		if (!find(path, keyword, ignoreCase, keywordIsLine))
			return -1;
		
		String line;
		int count = 1;
		int res = 1;
		
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			
			line = reader.readLine();
			
			while (line != null) {
				if (findInLine(line, keyword, ignoreCase, keywordIsLine))
					return count;
				
				line = reader.readLine();
				count++;
			}
			
			return res;
			
		} catch (IOException e) {
			logger.error(e.getMessage());
			return -1;
		}
	}
	
	/**
	 * Returns the content of a specific line
	 *
	 * @param path Path of the file that has the line
	 * @param line Number of the line to get the content of
	 *
	 * @return The content of a specific line
	 */
	
	public static String goLine (String path, long line) {
		return readFile(line, path).split("\n")[(int) line];
	}
	
	public static boolean replace (String path, String oldValue, String newValue, boolean ignoreCase,
	                               boolean keywordIsLine) {
		if (!find(path, oldValue, ignoreCase, keywordIsLine)) {
			logger.info("Value {} wasn't found", oldValue);
			return false;
		}
		
		long line = findLine(path, oldValue, ignoreCase, keywordIsLine);
		String readLine = readFile(line, path).split("\n")[0];
		
		clearFile(path);
		writeFile(path,
		          ignoreCase
		          ? readLine.toLowerCase().replace(oldValue.toLowerCase(), newValue.toLowerCase())
		          : readLine.replace(oldValue, newValue),
		          line,
		          true);
		
		return true;
	}
	
	public static boolean replace (String path, String oldValue, String newValue, boolean ignoreCase,
	                               boolean keywordIsLine, long line) {
		if (!find(path, oldValue, ignoreCase, keywordIsLine)) {
			logger.info("Value {} wasn't found", oldValue);
			return false;
		}
		
		String readLine = readFile(line, path).split("\n")[0];
		
		clearFile(path);
		writeFile(path,
		          ignoreCase
		          ? readLine.toLowerCase().replace(oldValue.toLowerCase(), newValue.toLowerCase())
		          : readLine.replace(oldValue, newValue),
		          line,
		          true);
		
		return true;
	}
	
	public static boolean replaceFrom (String path, String oldValue, String newValue, boolean ignoreCase,
	                                   boolean keywordIsLine, long start) {
		if (!find(path, oldValue, ignoreCase, keywordIsLine)) {
			logger.info("Value {} wasn't found", oldValue);
			return false;
		}
		
		long line = findLine(path, oldValue, ignoreCase, keywordIsLine, start);
		String readLine = readFile(line, path).split("\n")[0];
		
		clearFile(path);
		writeFile(path,
		          ignoreCase
		          ? readLine.toLowerCase().replace(oldValue.toLowerCase(), newValue.toLowerCase())
		          : readLine.replace(oldValue, newValue),
		          line,
		          true);
		
		return true;
	}
	
	public static boolean replaceAll (String path, String oldValue, String newValue, boolean ignoreCase,
	                                  boolean keywordIsLine) {
		if (!find(path, oldValue, ignoreCase, keywordIsLine)) {
			logger.info("Value {} wasn't found", oldValue);
			return false;
		}
		
		long line = findLine(path, oldValue, ignoreCase, keywordIsLine);
		
		while (line != -1) {
			String readLine = readFile(line, path).split("\n")[0];
			
			clearFile(path);
			writeFile(path,
			          ignoreCase
			          ? readLine.toLowerCase().replace(oldValue.toLowerCase(), newValue.toLowerCase())
			          : readLine.replace(oldValue, newValue),
			          line,
			          true);
			
			line = findLine(path, oldValue, ignoreCase, keywordIsLine, line + 1);
		}
		
		return true;
	}
	
	/**
	 * Returns the amount of lines a file has.
	 *
	 * @param path the path of the file.
	 *
	 * @return the amount of lines a file has.
	 *
	 * @throws IOException if the file doesn't exist.
	 */
	
	public static int countLines (String path) throws IOException {
		try (InputStream is = new BufferedInputStream(new FileInputStream(path))) {
			byte[] c = new byte[1024];
			
			int readChars = is.read(c);
			if (readChars == -1) {
				// bail out if nothing to read
				return 0;
			}
			
			// make it easy for the optimizer to tune this loop
			int count = 0;
			while (readChars == 1024) {
				for (int i = 0; i < 1024; i++) {
					if (c[i] == '\n') {
						++count;
					}
				}
				readChars = is.read(c);
			}
			
			// count remaining characters
			while (readChars != -1) {
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
				readChars = is.read(c);
			}
			
			return count == 0 ? 1 : count;
		}
	}
}
