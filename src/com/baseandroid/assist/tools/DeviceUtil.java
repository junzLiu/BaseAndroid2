package com.baseandroid.assist.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;

import android.Manifest.permission;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class DeviceUtil {
	private Context mContext;
	private static DeviceUtil instance;
	private boolean DEBUG = false;

	public static DeviceUtil newInstance(Context con) {
		if (instance == null)
			instance = new DeviceUtil(con);
		return instance;
	}

	private DeviceUtil(Context con) {
		this.mContext = con.getApplicationContext();
	}

	/** mac地址 */
	public String getMacAddr() {
		WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (manager == null)
			return null;
		WifiInfo wifiInfo = manager.getConnectionInfo();
		if (wifiInfo != null) {
			String macAddress = wifiInfo.getMacAddress();
			if (DEBUG)
				System.out.println("mac address:" + macAddress);
			return macAddress;
		}
		return null;
	}

	/** 设备型号 */
	public String getModel() {
		if (DEBUG)
			System.out.println("MODEL:" + Build.MODEL);
		return Build.MODEL;
	}

	/** 设备制造商 */
	public String getDevManufacturer() {
		if (DEBUG)
			System.out.println("MANUFACTURER:" + Build.MANUFACTURER);
		return Build.MANUFACTURER;
	}

	/** 设备id,序列号或者IMEI号 */
	public String getId() {
		String id = getDeviceId();
		if (id == null)
			return getDevSerialno();
		return id;
	}

	/** 设备id,可能是 CDMA 、GSM的IMEI号 */
	public String getDeviceId() {
		TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (manager == null)
			return null;
		String deviceId = manager.getDeviceId();
		String str2 = "";
		if (deviceId != null) {
			str2 = new String(deviceId);
			str2 = str2.replace("0", "");
		}
		if ((deviceId == null) || (str2.length() <= 0))
			return null;
		if (DEBUG)
			System.out.println("deviceId:" + deviceId);
		return deviceId;
	}

	/** 设备序列号 */
	public String getDevSerialno() {
		String serialno = null;
		if (Build.VERSION.SDK_INT >= 9)
			try {
				Class clazz = Class.forName("android.os.SystemProperties");
				Method method = clazz.getMethod("get", String.class, String.class);
				serialno = (String) method.invoke(clazz, "ro.serialno", "unknown");
			} catch (Exception e) {
				e.printStackTrace();
				serialno = null;
			}
		if (DEBUG)
			System.out.println("serialno:" + serialno);
		return serialno;
	}

	


	/** 设备系统版本号 */
	public String getCode() {
		if (DEBUG)
			System.out.println("SDK_INT:" + Build.VERSION.SDK_INT);
		return String.valueOf(Build.VERSION.SDK_INT);
	}

	/** 设备系统版本名 */
	public String getVersion() {
		if (DEBUG)
			System.out.println("RELEASE:" + Build.VERSION.RELEASE);
		return Build.VERSION.RELEASE;
	}

	/** 屏幕分辨率字符串，格式：1024x768 */
	public String getDisplayResolution() {
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		if (mContext.getResources().getConfiguration().orientation == 1)
			return metrics.widthPixels + "x" + metrics.heightPixels;
		if (DEBUG)
			System.out.println("Resolution:" + metrics.heightPixels + "x" + metrics.widthPixels);
		return metrics.heightPixels + "x" + metrics.widthPixels;
	}
    /** 屏幕Dpi */
	public int getDpi(){
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		return metrics.densityDpi;
	}
	
	/** sim卡编号，mobile country code + mobile network code */
	public String getSimCode() {
		TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (manager == null)
			return "-1";
		String code = manager.getSimOperator();
		if (TextUtils.isEmpty(code))
			code = "-1";
		if (DEBUG)
			System.out.println("SimCode:" + code);
		return code;
	}

	/** 获取系统服务对象 */
	private Object getSystemService(String serName) {
		try {
			return mContext.getSystemService(serName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 获得当前网络类型 */
	public String getActiveNetworkType() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null)
			return "none";
		NetworkInfo info = manager.getActiveNetworkInfo();
		if ((info == null) || (!info.isAvailable()))
			return "none";
		int type = info.getType();
		switch (type) {
		case ConnectivityManager.TYPE_WIFI:
			return "wifi";
		case ConnectivityManager.TYPE_MOBILE:
			String host = Proxy.getDefaultHost();
			String wap = "";
			if ((host != null) && (host.length() > 0))
				wap = " wap";
			return (isSupport3G() ? "3G" : "2G") + wap;
		case ConnectivityManager.TYPE_BLUETOOTH:
			return "bluetooth";
		case ConnectivityManager.TYPE_DUMMY:
			return "dummy";
		case ConnectivityManager.TYPE_ETHERNET:
			return "ethernet";
		case ConnectivityManager.TYPE_WIMAX:
			return "wimax";
		case 2:
		case 3:
		case 4:
		case 5:
		}
		return "none";
	}

	/** 获得当前网络类型 */
	public String getNetworkType() {
		String str = getActiveNetworkType();
		if ((TextUtils.isEmpty(str)) || ("none".equals(str)))
			return "none";
		if ((str.startsWith("3G")) || (str.startsWith("2G")))
			return "cell";
		if (str.startsWith("wifi"))
			return "wifi";
		return "other";
	}

	public int o() {
		return 1;
	}

	/** 是否是3G网络 */
	private boolean isSupport3G() {
		TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (manager == null)
			return false;
		switch (manager.getNetworkType()) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return false;
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return false;
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return false;
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return true;
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return true;
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return false;
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return true;
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return true;
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return true;
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return true;
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return true;
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			return true;
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return true;
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return false;
		case TelephonyManager.NETWORK_TYPE_LTE:
			return true;
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return false;
		}
		return false;
	}

	/** 得到运行时的App名，构成的JSONArray */
	public JSONArray getRunningAppName() {
		JSONArray jArray = new JSONArray();
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		if (manager == null)
			return jArray;
		List<RunningAppProcessInfo> runningAppInfo = manager.getRunningAppProcesses();
		if (runningAppInfo == null)
			return jArray;
		Iterator<RunningAppProcessInfo> iter = runningAppInfo.iterator();
		while (iter.hasNext()) {
			RunningAppProcessInfo appInfo = iter.next();
			jArray.put(appInfo.processName);
		}
		return jArray;
	}

	// 获取手机MAC地址  
	private String getMacAddress() {  
	    String result = "";  
	    WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
	    WifiInfo wifiInfo = wifiManager.getConnectionInfo();  
	    result = wifiInfo.getMacAddress();  
	    // Log.i(TAG, "macAdd:" + result);  
	    return result;  
	}  
	

	/** 读取缓存的设备数据 */
	private String readString() throws IOException, ClassNotFoundException {
		if (!hasSDcard())
			return null;
		String sdcard = getExternalStoragePath();
		File dir = new File(sdcard, "ShareSDK");
		if (!dir.exists())
			return null;
		File dk = new File(dir, ".dk");
		if (!dk.exists())
			return null;

		ObjectInputStream in = new ObjectInputStream(new FileInputStream(dk));
		Object object = in.readObject();
		String deviceInfo = null;
		if ((object != null) && ((object instanceof char[]))) {
			char[] arrayOfChar = (char[]) object;
			deviceInfo = String.valueOf(arrayOfChar);
		}
		in.close();
		return deviceInfo;
	}

	/** 保存设备数据 */
	private void saveString(String deviceInfo) throws IOException {
		if (!hasSDcard())
			return;
		String sdcard = getExternalStoragePath();
		File dir = new File(sdcard, "ShareSDK");
		if (!dir.exists())
			dir.mkdirs();
		File dk = new File(dir, ".dk");
		if (dk.exists())
			dk.delete();
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dk));
		char[] arrayOfChar = deviceInfo.toCharArray();
		out.writeObject(arrayOfChar);
		out.flush();
		out.close();
	}

	/** 包名 */
	public String getPackageName() {
		return mContext.getPackageName();
	}

	/** 当前应用程序名 ，参考：android:name；android:label */
	public String getAppName() {
		String str = mContext.getApplicationInfo().name;
		if (str != null)
			return str;
		int i = mContext.getApplicationInfo().labelRes;
		if (i > 0)
			str = mContext.getString(i);
		return str;
	}

	/** 当前应用程序版本号 */
	public int getVersionCode() {
		try {
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
			return info.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/** 当前应用程序版本名 */
	public String getVersionName() {
		try {
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "1.0";
	}

	/** 获得手机中app信息 */
	public ArrayList<HashMap<String, String>> getApplicationInfo(boolean b) {
		try {
			PackageManager manager = mContext.getPackageManager();
			List<PackageInfo> packageInfos = manager.getInstalledPackages(0);
			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
			Iterator<PackageInfo> iter = packageInfos.iterator();
			while (iter.hasNext()) {
				PackageInfo localPackageInfo = iter.next();
				if (!b && isSystemApp(localPackageInfo))
					continue;
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("pkg", localPackageInfo.packageName);
				map.put("name", localPackageInfo.applicationInfo.loadLabel(manager).toString());
				map.put("version", localPackageInfo.versionName);
				result.add(map);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<HashMap<String, String>>();
	}

	/** 判断应用是否安装在System分区 */
	private boolean isSystemApp(PackageInfo paramPackageInfo) {
		int system = (paramPackageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1 ? 1 : 0;
		int update = (paramPackageInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1 ? 1 : 0;
		return (system != 0) || (update != 0);
	}

	/** 返回注册网络的数字名称 */
	public String getNetworkOperator() {
		TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (manager == null)
			return null;
		String str = manager.getNetworkOperator();
		return str;
	}

	/** 判断app是否具有某个权限 */
	public boolean hasPermission(String permission) {
		int i = mContext.getPackageManager().checkPermission(permission, getPackageName());
		return i == 0;
	}

	/** 运行时的包名 */
	public String getRuntimePackageName() {
		boolean canGetTask = false;
		try {
			canGetTask = hasPermission(permission.GET_TASKS);
		} catch (Exception e) {
			e.printStackTrace();
			canGetTask = false;
		}
		if (canGetTask)
			try {
				ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
				if (manager == null)
					return null;
				ActivityManager.RunningTaskInfo localRunningTaskInfo = manager.getRunningTasks(
						1).get(0);
				return localRunningTaskInfo.topActivity.getPackageName();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}

	/** 支持外部SD卡 */
	public boolean hasSDcard() {
		try {
			return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/** 外部存储路径 */
	public String getExternalStoragePath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
}
