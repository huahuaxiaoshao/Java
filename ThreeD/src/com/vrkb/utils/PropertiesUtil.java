package com.vrkb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * 
 * java.util.Properties������ 1����װ�˶������ļ��ļ򵥶�д 2����չ�����˶�List��Map���ݸ�ʽ��֧��
 * 
 * ע�⣺ 1������������ֵ��ȡ�����У��ַ��ָ��ǲ��á������ַ�+�����ԡ��ķ�ʽ
 * ���Ա��ָ��key/value�����������ԡ�\����Ϊ��β����ǰ�߼���replace����׺�ġ�\������
 *
 */
public class PropertiesUtil {

	/**
	 * �����Զ�д
	 * 
	 * @param filePath
	 *            properties�ļ�·��
	 * @param fileName
	 *            properties�ļ���
	 * @param propertyName
	 *            key
	 * @param propertyValue
	 *            value
	 * @return
	 */
	public static boolean setProperty(String filePath, String fileName, String propertyName, String propertyValue) {
		try {
			Properties p = loadPropertyInstance(filePath, fileName);
			p.setProperty(propertyName, propertyValue);
			String comment = "Update '" + propertyName + "' value";
			return storePropertyInstance(filePath, fileName, p, comment);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * �������
	 * 
	 * @param filePath
	 * @param fileName
	 * @param propertyName
	 * @return
	 */
	public static boolean clearProperty(String filePath, String fileName, String propertyName) {
		try {
			Properties p = loadPropertyInstance(filePath, fileName);
			p.setProperty(propertyName, "");
			String comment = propertyName;
			return storePropertyInstance(filePath, fileName, p, comment);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean removeProperty(String filePath, String fileName, String propertyName) {
		try {
			Properties p = loadPropertyInstance(filePath, fileName);
			p.remove(propertyName);
			String comment = propertyName;
			return storePropertyInstance(filePath, fileName, p, comment);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ��ȡ����
	 * 
	 * @param filePath
	 * @param fileName
	 * @param propertyName
	 * @return
	 */
	public static String getProperty(String filePath, String fileName, String propertyName) {
		try {
			Properties p = loadPropertyInstance(filePath, fileName);
			return p.getProperty(propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getProperty(String filePath, String fileName, String propertyName, String defaultValue) {
		try {
			Properties p = loadPropertyInstance(filePath, fileName);
			return p.getProperty(propertyName, defaultValue);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �����Զ�д
	 * 
	 * @param filePath
	 * @param fileName
	 * @param propertyMap
	 * @return
	 */

	public static boolean setProperty(String filePath, String fileName, Map<String, String> propertyMap) {
		try {
			Properties p = loadPropertyInstance(filePath, fileName);
			for (String name : propertyMap.keySet()) {
				p.setProperty(name, propertyMap.get(name));
			}
			String comment = "Update '" + propertyMap.keySet().toString() + "' value";
			return storePropertyInstance(filePath, fileName, p, comment);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ��ô���Ĵ��룬ȴ�Ǽ��õģ�
	 * 
	 * @param filePath
	 * @param fileName
	 * @param propertyArray
	 * @return
	 */
	public static boolean setProperty(String filePath, String fileName, String... propertyArray) {
		if (propertyArray == null || propertyArray.length % 2 != 0) {
			throw new IllegalArgumentException("make sure 'propertyArray' argument is 'ket/value' pairs");
		}
		try {
			Properties p = loadPropertyInstance(filePath, fileName);
			for (int i = 0; i < propertyArray.length / 2; i++) {
				p.setProperty(propertyArray[i * 2], propertyArray[i * 2 + 1]);
			}
			String comment = "Update '" + propertyArray[0] + "..." + "' value";
			return storePropertyInstance(filePath, fileName, p, comment);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * �������ز���ֵ��Ϊtrueʱ��ʾ�ɹ� ��ϸ���ݼ��������propertyMap���˴����ô����õķ�ʽ��
	 * 
	 * @param filePath
	 * @param fileName
	 * @param propertyMap
	 * @return
	 */
	public static boolean getProperty(String filePath, String fileName, Map<String, String> propertyMap) {
		try {
			Properties p = loadPropertyInstance(filePath, fileName);
			for (String name : propertyMap.keySet()) {
				propertyMap.put(name, p.getProperty(name, propertyMap.get(name)));
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * List����ֵ��д
	 */

	public static boolean setProperty(String filePath, String fileName, String propertyName,
			List<String> propertyValueList) {
		try {
			Properties p = loadPropertyInstance(filePath, fileName);
			StringBuilder propertyValue = new StringBuilder();
			if (propertyValueList != null && propertyValueList.size() > 0) {
				for (String value : propertyValueList) {
					propertyValue.append(
							value.replaceAll("(\\\\)+$", "").replaceAll("\\\\", "\\\\\\\\").replaceAll(";", "\\\\;")
									+ ";");
				}
			}
			p.setProperty(propertyName, propertyValue.toString());
			String comment = "Update '" + propertyName + "' value";
			return storePropertyInstance(filePath, fileName, p, comment);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean appendProperty(String filePath, String fileName, String propertyName,
			List<String> propertyValueList) {
		try {
			Properties p = loadPropertyInstance(filePath, fileName);
			StringBuilder propertyValue = new StringBuilder();
			for (String value : propertyValueList) {
				propertyValue.append(
						value.replaceAll("(\\\\)+$", "").replaceAll("\\\\", "\\\\\\\\").replaceAll(";", "\\\\;") + ";");
			}
			p.setProperty(propertyName, p.getProperty(propertyName) + propertyValue.substring(1));
			String comment = "Update '" + propertyName + "' value";
			return storePropertyInstance(filePath, fileName, p, comment);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean appendProperty(String filePath, String fileName, String propertyName, String propertyValue) {
		try {
			Properties p = loadPropertyInstance(filePath, fileName);
			p.setProperty(propertyName, p.getProperty(propertyName, "")
					+ propertyValue.replaceAll("(\\\\)+$", "").replaceAll("\\\\", "\\\\\\\\").replaceAll(";", "\\\\;")
					+ ";");
			String comment = "Update '" + propertyName + "' value";
			return storePropertyInstance(filePath, fileName, p, comment);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static List<String> getPropertyList(String filePath, String fileName, String propertyName,
			String defaultValue) {
		try {
			Properties p = loadPropertyInstance(filePath, fileName);
			String v = p.getProperty(propertyName, defaultValue);
			String[] iA = v.split("(?<!\\\\);");
			for (int i = 0; i < iA.length; i++) {
				iA[i] = iA[i].replaceAll("(\\\\)+$", "").replaceAll("\\\\;", ";").replaceAll("\\\\\\\\", "\\\\");
			}
			return Arrays.asList(iA);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * MAP����ֵ��д
	 */

	public static boolean setProperty(String filePath, String fileName, String propertyName,
			Map<String, String> propertyValueMap) {
		try {
			Properties p = loadPropertyInstance(filePath, fileName);
			StringBuilder propertyValue = new StringBuilder();
			if (propertyValueMap != null && propertyValueMap.size() > 0) {
				for (String key : propertyValueMap.keySet()) {
					propertyValue.append(key.replaceAll("\\\\", "\\\\\\\\").replaceAll("(\\\\)+$", "")
							.replaceAll("\\,", "\\\\,").replaceAll(";", "\\\\;") + ","
							+ propertyValueMap.get(key).replaceAll("(\\\\)+$", "").replaceAll("\\\\", "\\\\\\\\")
									.replaceAll("\\,", "\\\\,").replaceAll(";", "\\\\;")
							+ ";");
				}
			}
			p.setProperty(propertyName, propertyValue.toString());
			String comment = "Update '" + propertyName + "' value";
			return storePropertyInstance(filePath, fileName, p, comment);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean appendProperty(String filePath, String fileName, String propertyName,
			Map<String, String> propertyValueMap) {
		try {
			Map<String, String> combinePropertyValueMap = getPropertyMap(filePath, fileName, propertyName, "");
			combinePropertyValueMap.putAll(propertyValueMap);
			return setProperty(filePath, fileName, propertyName, combinePropertyValueMap);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean appendProperty(String filePath, String fileName, String propertyName, String propertyKey,
			String propertyValue) {
		try {
			Properties p = loadPropertyInstance(filePath, fileName);
			p.setProperty(propertyName,
					p.getProperty(propertyName, "")
							+ propertyKey.replaceAll("(\\\\)+$", "").replaceAll("\\\\", "\\\\\\\\")
									.replaceAll("\\,", "\\\\,").replaceAll(";", "\\\\;")
							+ "," + propertyValue.replaceAll("(\\\\)+$", "").replaceAll("\\\\", "\\\\\\\\")
									.replaceAll("\\,", "\\\\,").replaceAll(";", "\\\\;")
							+ ";");
			String comment = "Update '" + propertyName + "." + propertyKey + "' value";
			return storePropertyInstance(filePath, fileName, p, comment);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ����Map��ʽ��װ������ֵ ���У�Map��HashMap��������Ҫ������Ļ����Ƽ��ⲿ����
	 * 
	 * @param filePath
	 * @param fileName
	 * @param propertyName
	 * @param defaultValue
	 * @return
	 */
	public static Map<String, String> getPropertyMap(String filePath, String fileName, String propertyName,
			String defaultValue) {
		try {
			Properties p = loadPropertyInstance(filePath, fileName);
			String v = p.getProperty(propertyName, defaultValue);

			Map<String, String> retMap = new HashMap<String, String>();
			String[] iA = v.split("(?<!\\\\);");
			for (String i : iA) {
				String[] jA = i.split("(?<!\\\\),");
				if (jA.length == 2) {
					retMap.put(
							jA[0].replaceAll("(\\\\)+$", "").replaceAll("\\\\\\,", "\\,").replaceAll("\\\\;", ";")
									.replaceAll("\\\\\\\\", "\\\\"),
							jA[1].replaceAll("(\\\\)+$", "").replaceAll("\\\\\\,", "\\,").replaceAll("\\\\;", ";")
									.replaceAll("\\\\\\\\", "\\\\"));
				}
			}
			return retMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * ����
	 */

	public static Properties loadPropertyInstance(String filePath, String fileName) {
		try {
			File d = new File(filePath);
			if (!d.exists()) {
				d.mkdirs();
			}
			File f = new File(d, fileName);
			if (!f.exists()) {
				f.createNewFile();
			}
			Properties p = new Properties();
			InputStream is = new FileInputStream(f);
			p.load(is);
			is.close();
			return p;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean storePropertyInstance(String filePath, String fileName, Properties p, String comment) {
		try {
			File d = new File(filePath);
			if (!d.exists()) {
				d.mkdirs();
			}
			File f = new File(d, fileName);
			if (!f.exists()) {
				f.createNewFile();
			}
			OutputStream os = new FileOutputStream(f);
			p.store(os, comment);
			os.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}