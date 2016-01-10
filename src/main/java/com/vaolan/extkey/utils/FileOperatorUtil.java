package com.vaolan.extkey.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 字符串操作工具类
 * 
 * @author zel
 * 
 */
public class FileOperatorUtil {
	public static Logger logger = Logger.getLogger(FileOperatorUtil.class);

	// 创建root_path的所在文件夹的所有路径
	public static boolean createRootDir(String root_path) {
		File f = new File(root_path);
		if (f.exists() && f.isDirectory()) {
			return true;
		} else {
			try {
				if (f.mkdirs()) {
					return true;
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// 创建root_path的所在文件夹的所有路径
	public static void createNewFile(String filePath) {
		File f = new File(filePath);
		if (f.exists()) {// 说明文件已存在
			return;
		} else {
			try {
				String temp=null;
				if((temp=f.getParent())!=null){
					new File(temp).mkdirs();	
				}
				f.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 判断文件是否存在，且是个文件夹
	public static boolean isExistAndDirecory(File file) {
		return file.exists() && file.isDirectory();
	}

	/**
	 * 得到某个文件夹下,某个过滤条件下文件名称值最大的那个文件
	 */
	public static String getLastFileNameStr(String desc_dir,
			final String filter_regex) {
		File dir = new File(desc_dir);

		// 文件夹过滤单元类
		FileFilterUnit fileFilterUnit = new FileFilterUnit() {
			public boolean accept(File pathname) {
				if (pathname.toString().contains(filter_regex)) {
					return true;
				}
				return false;
			}
		};

		File[] files = dir.listFiles(new MyFileFilter(fileFilterUnit));
		if (files == null || files.length == 0) {
			return null;
		}
		Arrays.sort(files, new FileComparator());
		return files[0].toString();
	}

	public static HashSet<String> getStringSetFromFile(String filePath)
			throws Exception {
		HashSet<String> hs = new HashSet<String>();
		File aidFile = new File(filePath);
		if (!aidFile.exists()) {
			return hs;
		}
		FileReader fr = new FileReader(aidFile);
		String line = null;
		BufferedReader br = new BufferedReader(fr);

		while ((line = br.readLine()) != null) {
			hs.add(line);
		}
		br.close();

		return hs;
	}

	public static void writeStrToFile(String aidFile, String source) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(aidFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PrintStream ps = new PrintStream(fos);

		ps.print(source);

		ps.flush();
		ps.close();
	}

	// 加入sourceFile指定的文件夹内的class文件名称
	// subPackPath主要是做各子层的路径导航
	public static List<String> getFirstLevelFileName(File sourceFile) {
		List<String> subFileStrList = new LinkedList<String>();
		// 如是.class则直接加入集合当中
		if (sourceFile.getName().endsWith(".class")) {
			subFileStrList.add(sourceFile.getName());
		} else if (sourceFile.isDirectory()) {
			// 过滤器
			FileFilterUnit fileFilterUnit = new FileFilterUnit() {
				public boolean accept(File pathname) {
					if (pathname.getName().endsWith(".class")
							|| pathname.isDirectory()) {
						return true;
					}
					return false;
				}
			};
			File[] files = sourceFile
					.listFiles(new MyFileFilter(fileFilterUnit));
			for (File file : files) {
				subFileStrList.add(file.getName());
			}
		}
		return subFileStrList;
	}

	public static void main(String[] args) {
		// File f = new File("D:/nutch_test/data_zel/2013-08-13/thread-0-0");
		// System.out.println("f space--" + f.length());
		// System.out.println(264 * 1024);
		createNewFile("kkt.txt");
	}
}

/**
 * 文件名称过滤器
 * 
 * @author zel
 */
class MyFileFilter implements FileFilter {
	private FileFilterUnit fileFilterUnit;

	public MyFileFilter(FileFilterUnit fileFilterUnit) {
		this.fileFilterUnit = fileFilterUnit;
	}

	@Override
	public boolean accept(File pathname) {
		return fileFilterUnit.accept(pathname);
	}
}

class FileFilterUnit {
	public boolean accept(File pathname) {
		return true;
	}
}

/**
 * 逆序排列的文件比较器
 * 
 * @author zel
 * 
 */
class FileComparator implements Comparator<File> {

	@Override
	public int compare(File o1, File o2) {
		String o1_str = o1.toString();
		String o2_str = o2.toString();
		if (o2_str.length() > o1_str.length()) {
			return 1;
		}
		if (o2_str.length() == o1_str.length()) {
			return o2_str.compareTo(o1_str);
		}
		return -1;
	}

}
