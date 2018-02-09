package logger.hw.com.javatest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;


public class MD5Helper {
	

	public static void main(String[] s){
		String str = "33333";
		System.out.println(MD5(str));
		System.out.println(reverseMD5(MD5(str)));
	}
	/**
	 * 获取进行MD5加密，反转后的密码
	 * 
	 * @param password passWord
	 * @return
	 */
	public static String reverseMD5(String password) {
		String finalPassword = new StringBuffer("").append(MD5(password))
				.reverse().toString();
		return finalPassword;
	}
	
	/**
	 * MD5加密
	 * 
	 * @param originalPW
	 *            原始密码
	 * @return 加密之后的密码
	 */
	public static String MD5(String originalPW) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = originalPW.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = (md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	/**
	 * 加密算法
	 * 
	 * @throws UnsupportedEncodingException
	 * 
	 * @return加密后字符串
	 */
	public static String EncryptCode(String userID, String str) {
		byte[] idByte = null;
		idByte = MD5(userID).getBytes();
		byte S_key = idByte[idByte.length - 1];// 密钥
		byte[] value;
		try {
			value = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			value = str.getBytes();
		}
		int[] valueXOR = new int[value.length];// 异或之后的字符串
		StringBuffer result = new StringBuffer("");// 结果字符串
		for (int i = 0; i < value.length; i++) {
			// value[i]与S_key异或运算
			// Log.i("加密", "异或之后 " + (value[i] ^ S_key));
			valueXOR[i] = (value[i] ^ S_key);
			if (valueXOR[i] < 0) {
				valueXOR[i] += 256;
			}
			result.append(String.format("%03d", valueXOR[i]));// 左侧补充0
		}
		String finalResult = result.reverse().toString();
		return finalResult;
	}

	/**
	 * 解密算法
	 * 
	 * @param userID 用户ID
	 * @param EncryptInfo 密文
	 * @throws UnsupportedEncodingException
	 * @return原文
	 */
	public static String DecryptCode(String userID, String EncryptInfo) {
		byte[] idByte = MD5(userID).getBytes();// android默认utf-8格式，一个中文字符占用3个字符
		byte S_key = idByte[idByte.length - 1];// 密钥
		String S_source = new StringBuffer("").append(EncryptInfo).reverse()
				.toString();
		byte[] value = new byte[S_source.length() / 3];// 构造value数组

		int temp;
		for (int i = 0, k = 0; i < S_source.length(); i += 3, k++) {
			temp = Integer.parseInt(S_source.substring(i, i + 3));
			value[k] = (byte) (temp ^ S_key);
		}
		String result;
		try {
			result = new String(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			result = new String(value);
		}
		return result;
	}
}
