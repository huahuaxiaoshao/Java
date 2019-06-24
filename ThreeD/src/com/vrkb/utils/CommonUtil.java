package com.vrkb.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.vrkb.bean.ModelBean;

/**
 * 各种工具
 */
public class CommonUtil {

	/**
	 * 缩略图
	 */
	public static final int FILE_TYPE_THUMB = 0;
	/**
	 * 3d模型源文件
	 */
	public static final int FILE_TYPE_SRC = 1;
	/**
	 * 图片格式材质文件
	 */
	public static final int FILE_TYPE_TEXTURE = 2;
	/**
	 * js文件
	 */
	public static final int FILE_TYPE_JS = 3;
	/**
	 * bin文件
	 */
	public static final int FILE_TYPE_BIN = 4;
	/**
	 * obj文件
	 */
	public static final int FILE_TYPE_OBJ = 5;
	/**
	 * mtl文件
	 */
	public static final int FILE_TYPE_MTL = 6;

	public static int getFileType(String fileName) {
		if (fileName == null || fileName.length() > 255)
			return -1;
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		if ("jpg|jpeg|png|bmp|gif".contains(prefix)) {
			return FILE_TYPE_THUMB;
		}
		if ("rar|zip|7z|tar|gz|iso|cab".contains(prefix)) {
			return FILE_TYPE_SRC;
		}
		if ("js".equals(prefix)) {
			return FILE_TYPE_JS;
		}
		if ("bin".equals(prefix)) {
			return FILE_TYPE_BIN;
		}
		if ("obj".equals(prefix)) {
			return FILE_TYPE_OBJ;
		}
		if ("mtl".equals(prefix)) {
			return FILE_TYPE_MTL;
		}
		return -1;
	}

	public static boolean isEmpty(String s) {
		return s == null || s.trim().equals("");
	}

	public static boolean isEmpty(Object o) {
		return o == null || isEmpty(o.toString());
	}

	/**
	 * 计算文件大小
	 * 
	 * @param size
	 *            文件字节长度
	 * @param unit
	 *            计算单位 'B','K','M','G'
	 * @return
	 */
	public static String getFileSize(long size, char unit) {
		if (unit == 'M') {
			double v = formatNumber(size / 1024.0 / 1024.0, 2);
			if (v > 1024) {
				return getFileSize(size, 'G');
			} else {
				return v + "M";
			}
		} else if (unit == 'G') {
			return formatNumber(size / 1024.0 / 1024.0 / 1024.0, 2) + "G";
		} else if (unit == 'K') {
			double v = formatNumber(size / 1024.0, 2);
			if (v > 1024) {
				return getFileSize(size, 'M');
			} else {
				return v + "K";
			}
		} else if (unit == 'B') {
			if (size > 1024) {
				return getFileSize(size, 'K');
			} else {
				return size + "B";
			}
		}
		return "" + 0 + unit;
	}

	public static double formatNumber(double value, int l) {
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(l);
		format.setGroupingUsed(false);
		return new Double(format.format(value));
	}

	public static boolean isInteger(String v) {
		if (isEmpty(v))
			return false;
		return v.matches("^\\d+$");
	}

	public static String formatDate(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(new java.util.Date(time));
	}

	public static String convertPath(String path) {
		return path != null ? path.replace("\\", "/") : "";
	}

	/**
	 * 递归删除文件和文件夹
	 * 
	 * @author LiShuai
	 * @date 2017年4月17日 下午3:07:59
	 * @param file
	 */
	public static void delete(File file) {
		try {
			if (file.exists()) {
				if (file.isFile()) {
					file.delete();
				} else if (file.isDirectory()) {
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						delete(files[i]);
					}
					file.delete();
				}
			} else {
				System.out.println("所删除的文件不存在！" + '\n');
			}
		} catch (Exception e) {
			System.out.print("unable to delete the folder!");
		}
	}

	/**
	 * 递归算法复制文件(以超快的速度复制文件)
	 * @description
	 * @param srcFile
	 *            源文件或目录 e.g. D:\\webBundle
	 * @param desFile
	 *            目标目录 e.g. D:\\11
	 * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回-1
	 */
	public static long copyFile(File srcFile, File desFile,ModelBean model) {
		long copySizes = 0;
		if (!srcFile.exists()) {
			System.out.println("源文件不存在");
			copySizes = -1;
		} else {
			if (srcFile.isDirectory()) {
				desFile.mkdir();
				for (File tmpFile : srcFile.listFiles()) {
					copyFile(tmpFile, new File(desFile, tmpFile.getName()),model);
					if(tmpFile.getName().endsWith(".obj")){
						model.setObjName(tmpFile.getName());
					}
					if(tmpFile.getName().endsWith(".mtl")){
						model.setMtlName(tmpFile.getName());
					}
				}
			} else if (srcFile.isFile()) {
				try {
					FileInputStream fin = new FileInputStream(srcFile);
					FileOutputStream fout = new FileOutputStream(desFile);
					System.out.println(desFile.getAbsolutePath());
					FileChannel fcin = fin.getChannel();
					FileChannel fcout = fout.getChannel();
					long size = fcin.size();
					fcin.transferTo(0, fcin.size(), fcout);
					fcin.close();
					fcout.close();
					fin.close();
					fout.close();
					copySizes = size;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return copySizes;
	}

	/**
	 * 递归查找包含关键字的文件
	 * @description
	 * @param folder
	 *            要搜索的目录
	 * @param keyWord
	 *            关键字
	 * @return
	 */
	public static File[] searchFile(File folder, final String keyWord) {

		File[] subFolders = folder.listFiles(new FileFilter() {// 运用内部匿名类获得文件
			@Override
			public boolean accept(File pathname) {// 实现FileFilter类的accept方法
				if (pathname.isDirectory() || (pathname.isFile() && pathname.getName().toLowerCase().contains(keyWord.toLowerCase())))// 目录或文件包含关键字
					return true;
				return false;
			}
		});

		List<File> result = new ArrayList<File>();// 声明一个集合
		for (int i = 0; i < subFolders.length; i++) {// 循环显示文件夹或文件
			if (subFolders[i].isFile()) {// 如果是文件则将文件添加到结果列表中
				result.add(subFolders[i]);
			} else {// 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
				File[] foldResult = searchFile(subFolders[i], keyWord);
				for (int j = 0; j < foldResult.length; j++) {// 循环显示文件
					result.add(foldResult[j]);// 文件保存到集合中
				}
			}
		}

		File files[] = new File[result.size()];// 声明文件数组，长度为集合的长度
		result.toArray(files);// 集合数组化
		return files;
	}

	public static void main(String... args) {
		// System.out.println(getFileType("filejs"));
		// System.out.println(getFileType("file.jpg"));
		// System.out.println(getFileType("file.jpeG"));
		// System.out.println(getFileType("file.OBJ"));
		// System.out.println(getFileType("file.Bin"));
		// System.out.println(getFileType("file.GIF"));
		File[] f = searchFile(new File("d:\\ee"), "resource1.unity3d");
		System.out.println(f.length);
		System.out.println(new File("\\e").getAbsolutePath());
	}

}
