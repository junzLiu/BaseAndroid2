package com.baseandroid.assist.tools;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 数值转换及加密工具
 * Created by Mark on 2015/11/3.
 */
public class TransformUtil {

    /**
     *  像素单位转换 dip转px
     * @param context
     * @param dip dip 值
     * @return
     */
    public static int dip2px(Context context, float dip) {
        if (context == null)
            return 0;
        return (int) (0.5F + dip * context.getResources().getDisplayMetrics().density);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        if (context == null)
            return 0;
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 该方法用于将px大小转换为dip大小
     *
     * @param context
     *            上下文对象
     * @param pxValue
     *            px大小值
     * @return 转换后的dip值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /** 将Bitmap转换成InputStream */
    public static InputStream Bitmap2InputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    /** md5加密 */
    public static String md5Encrypt(String key) {
        String encryptStr;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            encryptStr = bytes2HexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            encryptStr = String.valueOf(key.hashCode());
        }
        return encryptStr;
    }

    /**
     * byte转十六进制字符串
     * @param bytes
     * @return
     */
    private static String bytes2HexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 将InputStream转换成某种字符编码的String
     *
     * @param in
     * @param encoding
     * @return
     * @throws Exception
     */
    public static String InputStreamTOString(InputStream in, String encoding) throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int count = -1;
        while ((count = in.read(data, 0, 1024)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return new String(outStream.toByteArray(), encoding);
    }

    /** 内存大小转文字 */
    public static String memorySize2String(long memorySize) {
        String memoryStr = "";
        if (memorySize < 1024l)
            memoryStr = memorySize + "B";
        else if (memorySize < (1024l * 1024l))
            memoryStr = (memorySize / 1024l) + "KB";
        else if (memorySize < (1024l * 1024l * 1024l))
            memoryStr = (memorySize / 1024l / 1024l) + "MB";
        else if (memorySize < (1024l * 1024l * 1024l * 1024l))
            memoryStr = (memorySize / 1024l / 1024l / 1024l) + "GB";
        return memoryStr;
    }

}
