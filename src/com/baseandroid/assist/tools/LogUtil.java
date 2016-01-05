package com.baseandroid.assist.tools;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class LogUtil {
	/**
	 * debug or not when release, set debug 'false'
	 */
	private static boolean debug = true;
	private static long max_size = 5 * 1024 * 1024;

	private static String getFunctionName() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();

		if (sts == null) {
			return null;
		}

		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}

			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}

			return "[" + Thread.currentThread().getName() + "(" + Thread.currentThread().getId() + "): " + st.getFileName() + ":"
					+ st.getMethodName() + ":" + st.getLineNumber() + "]";
		}

		return null;
	}

	/** 日志初始化 */
	public static void init() {
		try {
			File dirFile = new File("logDir");
			if (!dirFile.exists())
				dirFile.mkdirs();
			File logfile = new File("logDir" + File.separator + "weichi.log");
			if (!logfile.exists())
				logfile.createNewFile();
			if (logfile.length() > max_size) {
				if (logfile.delete()) {
					logfile.createNewFile();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	/** 写日志文件 */
	private static void writeLog(int logType, String tag, String msg) {
		StringBuffer sb = new StringBuffer();
		sb.append(new Date().toString());
		sb.append("<--->");
		if (logType == Log.ERROR)
			switch (logType) {
			case Log.ASSERT:
				sb.append("ASSERT");
				break;
			case Log.DEBUG:
				sb.append("DEBUG");
				break;
			case Log.ERROR:
				sb.append("ERROR");
				break;
			case Log.INFO:
				sb.append("INFO");
				break;
			case Log.VERBOSE:
				sb.append("VERBOSE");
				break;
			case Log.WARN:
				sb.append("WARN");
				break;
			}
		sb.append("<--->");
		sb.append(tag);
		sb.append("<--->");
		sb.append(msg);
		sb.append("\r\n");
		FileOutputStream fos = null;
		try {
			File logfile = new File("logDir" + File.separator + "weichi.log");
			fos = new FileOutputStream(logfile, true);
			fos.write(sb.toString().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if(fos != null)
				fos.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				if(fos != null)
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static String createMessage(String msg) {
		String functionName = getFunctionName();
		String message = (functionName == null ? msg : (functionName + " - " + msg));
		return message;
	}

	/**
	 * log.i
	 */
	public static void i(String tag, String msg) {
		if (debug) {
			String message = createMessage(msg);
			Log.i(tag, message);
			writeLog(Log.INFO, tag, message);
		}
	}

	/**
	 * log.v
	 */
	public static void v(String tag, String msg) {
		if (debug) {
			String message = createMessage(msg);
			Log.v(tag, message);
			writeLog(Log.VERBOSE, tag, message);
		}
	}

	/**
	 * log.d
	 */
	public static void d(String tag, String msg) {
		if (debug) {
			String message = createMessage(msg);
			Log.d(tag, message);
			writeLog(Log.DEBUG, tag, message);
		}
	}

	/**
	 * log.e
	 */
	public static void e(String tag, String msg) {
		if (debug) {
			String message = createMessage(msg);
			Log.e(tag, message);
			writeLog(Log.ERROR, tag, message);
		}
	}

	/**
	 * log.error
	 */
	public static void error(String tag, Exception e) {
		if (debug) {
			StringBuffer sb = new StringBuffer();
			String name = getFunctionName();
			StackTraceElement[] sts = e.getStackTrace();

			if (name != null) {
				sb.append(name + " - " + e + "\r\n");
			} else {
				sb.append(e + "\r\n");
			}
			if (sts != null && sts.length > 0) {
				for (StackTraceElement st : sts) {
					if (st != null) {
						sb.append("[ " + st.getFileName() + ":" + st.getLineNumber() + " ]\r\n");
					}
				}
			}
			Log.e(tag, sb.toString());
		}
	}

	/**
	 * log.w
	 */
	public static void w(String tag, String msg) {
		if (debug) {
			String message = createMessage(msg);
			Log.w(tag, message);
		}
	}

	/**
	 * set debug false
	 */
	public static void setDebug(boolean d) {
		debug = d;
	}

}
