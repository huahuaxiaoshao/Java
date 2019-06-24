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
 * ���ֹ���
 */
public class CommonUtil {

	/**
	 * ����ͼ
	 */
	public static final int FILE_TYPE_THUMB = 0;
	/**
	 * 3dģ��Դ�ļ�
	 */
	public static final int FILE_TYPE_SRC = 1;
	/**
	 * ͼƬ��ʽ�����ļ�
	 */
	public static final int FILE_TYPE_TEXTURE = 2;
	/**
	 * js�ļ�
	 */
	public static final int FILE_TYPE_JS = 3;
	/**
	 * bin�ļ�
	 */
	public static final int FILE_TYPE_BIN = 4;
	/**
	 * obj�ļ�
	 */
	public static final int FILE_TYPE_OBJ = 5;
	/**
	 * mtl�ļ�
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
	 * �����ļ���С
	 * 
	 * @param size
	 *            �ļ��ֽڳ���
	 * @param unit
	 *            ���㵥λ 'B','K','M','G'
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
	 * �ݹ�ɾ���ļ����ļ���
	 * 
	 * @author LiShuai
	 * @date 2017��4��17�� ����3:07:59
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
				System.out.println("��ɾ�����ļ������ڣ�" + '\n');
			}
		} catch (Exception e) {
			System.out.print("unable to delete the folder!");
		}
	}

	/**
	 * �ݹ��㷨�����ļ�(�Գ�����ٶȸ����ļ�)
	 * @description
	 * @param srcFile
	 *            Դ�ļ���Ŀ¼ e.g. D:\\webBundle
	 * @param desFile
	 *            Ŀ��Ŀ¼ e.g. D:\\11
	 * @return ʵ�ʸ��Ƶ��ֽ���������ļ���Ŀ¼�����ڡ��ļ�Ϊnull���߷���IO�쳣������-1
	 */
	public static long copyFile(File srcFile, File desFile,ModelBean model) {
		long copySizes = 0;
		if (!srcFile.exists()) {
			System.out.println("Դ�ļ�������");
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
	 * �ݹ���Ұ����ؼ��ֵ��ļ�
	 * @description
	 * @param folder
	 *            Ҫ������Ŀ¼
	 * @param keyWord
	 *            �ؼ���
	 * @return
	 */
	public static File[] searchFile(File folder, final String keyWord) {

		File[] subFolders = folder.listFiles(new FileFilter() {// �����ڲ����������ļ�
			@Override
			public boolean accept(File pathname) {// ʵ��FileFilter���accept����
				if (pathname.isDirectory() || (pathname.isFile() && pathname.getName().toLowerCase().contains(keyWord.toLowerCase())))// Ŀ¼���ļ������ؼ���
					return true;
				return false;
			}
		});

		List<File> result = new ArrayList<File>();// ����һ������
		for (int i = 0; i < subFolders.length; i++) {// ѭ����ʾ�ļ��л��ļ�
			if (subFolders[i].isFile()) {// ������ļ����ļ���ӵ�����б���
				result.add(subFolders[i]);
			} else {// ������ļ��У���ݹ���ñ�������Ȼ������е��ļ��ӵ�����б���
				File[] foldResult = searchFile(subFolders[i], keyWord);
				for (int j = 0; j < foldResult.length; j++) {// ѭ����ʾ�ļ�
					result.add(foldResult[j]);// �ļ����浽������
				}
			}
		}

		File files[] = new File[result.size()];// �����ļ����飬����Ϊ���ϵĳ���
		result.toArray(files);// �������黯
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
