/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.util;

import java.util.Date;

import forex.genetic.manager.PropertiesManager;

/**
 *
 * @author ricardorq85
 */
public class LogUtil {

	private static boolean LOG_ENTER = false;

	/**
	 *
	 * @param name
	 * @param logLevel
	 */
	public static void logTime(String name, int logLevel) {
		LogUtil.logTime(name, logLevel, 0);
	}

	/**
	 *
	 * @param name
	 * @param logLevel
	 * @param tabLevel
	 */
	public static void logTime(String name, int logLevel, int tabLevel) {
		logEnterBySwitch(logLevel);
		if (logLevel <= PropertiesManager.getPropertyInt(Constants.LOG_LEVEL)) {
			StringBuilder buffer = new StringBuilder();
			// buffer.append("<log> ");
			buffer.append(name);
			buffer.append(" <- ");
			buffer.append(DateUtil.getDateString(new Date()));
			for (int i = 0; i < tabLevel; i++) {
				buffer.append("\t");
			}
			System.out.println(buffer.toString());
		}
	}

	public static void logAvance(int logLevel) {
		logAvance(".", logLevel);
	}

	public static void logAvance(String c, int logLevel) {
		LOG_ENTER = true;
		if (logLevel <= PropertiesManager.getPropertyInt(Constants.LOG_LEVEL)) {
			StringBuilder buffer = new StringBuilder();
			buffer.append(c);
			System.out.print(buffer.toString());
		}

	}

	public static void logEnterBySwitch(int logLevel) {
		if (logLevel <= PropertiesManager.getPropertyInt(Constants.LOG_LEVEL)) {
			if (LOG_ENTER) {
				System.out.println("");
				LOG_ENTER = false;
			}			
		}
	}

	public static void logEnter(int logLevel) {
		if (logLevel <= PropertiesManager.getPropertyInt(Constants.LOG_LEVEL)) {
			System.out.println("");
		}
	}

}
