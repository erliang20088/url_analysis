package com.vaolan.extkey.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * 读取字典时的I/O工具类
 * 
 * @author zel
 * 
 */
public class IOUtil {
	private static Logger logger = Logger.getLogger(IOUtil.class);

	public static String readDirOrFile(String filePath, String fileEncoding) {
		File f = new File(filePath);
		String temp = "";
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (File temp_file : files) {
				temp += readDirOrFile(temp_file.getAbsolutePath(), fileEncoding);
			}
			return temp;
		}
		return readFile(filePath, fileEncoding);
	}

	public static ArrayList<String> readDirOrFileToList(String filePath,
			String fileEncoding, ArrayList<String> linkList) {
		File f = new File(filePath);
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			for (File temp_file : files) {
				linkList.add(readFileWithRegexFilter(
						temp_file.getAbsolutePath(), fileEncoding));
			}
			return linkList;
		} else {
			linkList.add(readFileWithRegexFilter(filePath, fileEncoding));
		}
		return linkList;
	}

	/**
	 * fileEncoding若为null,则采用系统默认编码
	 * 
	 * @param filePath
	 * @param fileEncoding
	 * @return
	 */
	public static String readFile(String filePath, String fileEncoding) {
		if (fileEncoding == null) {
			fileEncoding = System.getProperty("file.encoding");
		}
		File file = new File(filePath);
		BufferedReader br = null;

		String line = null;

		StringBuilder sb = new StringBuilder();

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file), fileEncoding));
			while ((line = br.readLine()) != null) {
				line=line.trim();
				if (!line.startsWith("#")) {
					sb.append(line + "\n");
				}
			}
			return sb.toString();
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.info(e.getLocalizedMessage());
					logger.info("关闭IOUtil流时出现错误!");
				}
			}
		}
		return null;
	}

	// 将按行读出的字符串，统一加入到set中，直接去重
	public static Set<String> readFileToSet(String filePath, String fileEncoding) {
		if (fileEncoding == null) {
			fileEncoding = System.getProperty("file.encoding");
		}
		File file = new File(filePath);
		BufferedReader br = null;

		String line = null;

		Set<String> lineSet = new HashSet<String>();

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file), fileEncoding));
			while ((line = br.readLine()) != null) {
				lineSet.add(line);
			}
			return lineSet;
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.info(e.getLocalizedMessage());
					logger.info("关闭IOUtil流时出现错误!");
				}
			}
		}
		return null;
	}

	public static String readFile(String filePath, String fileEncoding,
			int begin_line_number, int end_line_number) {
		if (fileEncoding == null) {
			fileEncoding = System.getProperty("file.encoding");
		}
		File file = new File(filePath);
		BufferedReader br = null;

		String line = null;

		StringBuilder sb = new StringBuilder();

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file), fileEncoding));
			int i = 0;
			// 空读完前边的行
			while ((line = br.readLine()) != null && i < begin_line_number) {
				i++;
			}

			while ((line = br.readLine()) != null
					&& begin_line_number < end_line_number) {
				sb.append(line + "\n");
				begin_line_number++;
			}
			// System.out.println("line---"+line);
			return sb.toString();
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.info(e.getLocalizedMessage());
					logger.info("关闭IOUtil流时出现错误!");
				}
			}
		}
		return null;
	}

	public static String readFileWithRegexFilter(String filePath,
			String fileEncoding) {
		if (fileEncoding == null) {
			fileEncoding = System.getProperty("file.encoding");
		}
		File file = new File(filePath);
		BufferedReader br = null;

		String line = null;

		StringBuilder sb = new StringBuilder();

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file), fileEncoding));
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			return sb.toString();
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.info(e.getLocalizedMessage());
					logger.info("关闭IOUtil流时出现错误!");
				}
			}
		}
		return null;
	}

	/**
	 * 将一个字符串写入到一个文件
	 * 
	 * @param path
	 *            储存的文件路径
	 * @param value
	 *            储存的文件内容
	 * @throws IOException
	 */
	public static void writeFile(String path, String value, String encoding) {
		File f = new File(path);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			fos.write(value.getBytes(encoding));
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 以追加的方式向文件中写入
	 * 
	 * @param path
	 * @param value
	 */
	public static void writeFile(String path, String value, boolean isAppend,
			String encoding) {
		File f = new File(path);
		FileOutputStream fos = null;
		try {
			if (isAppend) {
				fos = new FileOutputStream(f, isAppend);
			} else {
				fos = new FileOutputStream(f);
			}
			fos.write(value.getBytes(encoding));
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static List<String> tranBatchLineToList(String source) {
		if (source == null || source.isEmpty()) {
			return null;
		}
		List<String> lineList = new LinkedList<String>();
		StringReader sb = new StringReader(source);
		BufferedReader br = new BufferedReader(sb);

		String temp_line = null;
		try {
			while ((temp_line = br.readLine()) != null) {
				lineList.add(temp_line);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lineList;
	}

	public static List<String> tranBatchLineToList(String source,
			int begin_line_number, int end_line_number) {
		if (source == null || source.isEmpty()) {
			return null;
		}
		List<String> lineList = new LinkedList<String>();
		StringReader sb = new StringReader(source);
		BufferedReader br = new BufferedReader(sb);

		String temp_line = null;
		try {
			int i = 0;
			// 空读完前边的行
			while (i < begin_line_number && (temp_line = br.readLine()) != null) {
				i++;
			}
			while ((temp_line = br.readLine()) != null
					&& begin_line_number < end_line_number) {
				lineList.add(temp_line);
				begin_line_number++;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lineList;
	}

	public static String tranSetToString(Set<String> strSet) {
		if (strSet == null || strSet.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (String line : strSet) {
			sb.append(line + StaticValue.separator_next_line);
		}
		return sb.toString();
	}

	public static String tranListToString(List list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (Object line : list) {
			sb.append(line + StaticValue.separator_next_line);
		}
		return sb.toString();
	}

	public static String tranSetToStringWithFixNature(Set<String> strSet) {
		if (strSet == null || strSet.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (String line : strSet) {
			// sb.append(line +
			// StaticValue.separator_tab+StaticValue.default_nature+StaticValue.separator_tab+StaticValue.default_term_freq+
			// StaticValue.separator_next_line);
		}
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		// String source=readFile("resource/library.dic",null);
		// String source = readFile(ReadConfigUtil.getValue("dic.path"), null);
		// String source = readDirOrFile("d://temp", "gbk");
		// System.out.println(source);

		String source_string = IOUtil.readFile("d:\\test\\new_words2.txt",
				"utf-8");
		StringReader sr = new StringReader(source_string);
		BufferedReader br = new BufferedReader(sr);
		String temp = null;
		StringBuilder sb = new StringBuilder();

		HashSet<String> hashSet = new HashSet<String>();

		while ((temp = br.readLine()) != null) {
			if (temp.trim().length() > 1 && temp.trim().length() <= 4) {
				if (!hashSet.contains(temp)) {
					sb.append(temp + "\n");
				} else {
					hashSet.add(temp);
				}
			}
		}
		IOUtil.writeFile("d:\\test\\new_words3.txt", sb.toString(),
				StaticValue.default_encoding);
	}
}
