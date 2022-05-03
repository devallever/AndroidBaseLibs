package app.allever.android.lib.core.util;

/**
 * 加解密工具类
 */
public class CryptUtils {
	
	public static byte[] xor(byte[] bytes, byte key) {

		byte[] result = new byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			result[i] = (byte) ((bytes[i]) ^ key);
		}
		return result;
	}
	/**
	 * 循环异或，一般用于加密
	 * @param bytes
	 * @param key
	 * @return
	 */
	public static byte[] xor(byte[] bytes, byte[] key) {
		byte[] result = bytes;
		for (int i = 0; i < key.length; i++) {
			result = xor(result, key[i]);
		}
		return result;
	}
	
	public static String encrypt(String src, String key){
		try{
			byte[] b=xor(src.getBytes("utf-8"), key.getBytes("utf-8"));
			return Base64.encodeBytes(b);
		}catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	/**
	 * 解密 ：使用异或解密后采用utf8编码返回字符串
	 * @param src
	 * @param key
	 * @return
	 */
	public static String decrypt(String src, String key){
		try{			
			byte[] b=xor(Base64.decode(src), key.getBytes("utf-8"));
			return new String(b,"utf-8");
		}catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	/**
	 * 下面两个方法用于图片的加密解密
	 * @param src
	 * @param key
	 * @return
	 */
	
	public static byte[] encrypt(byte[] src, String key){
		try {
			byte[] b=xor(src, key.getBytes("utf-8"));
			return b;
		}catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	/**
	 * 解密 ：使用异或解密后采用utf8编码返回字符串
	 * @param src
	 * @param key
	 * @return
	 */
	public static byte[] decrypt(byte[] src, String key){
		try{			
			byte[] b=xor(src, key.getBytes("utf-8"));
			return b;
		}catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
}
