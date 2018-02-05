package com.leaf.xadmin.utils.encrypt;

import com.leaf.xadmin.config.ShiroConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.Key;

/**
 * 自定义密码加密工具
 * 		主要实现过程 : 首先将原用户密码进行MD5不可逆加密，然后使用可逆加密算法并保存至数据库中
 * 		密码比较过程 : 采用比较大众的方式--中间值比较(比较的是MD5加密后的字符串)
 *
 * @author leaf
 *
 */
@Component
public class PassEncryptUtil {

	@Autowired
	private DesUtil desUtil;

	/**
	 * 解密密码
	 *
	 * @param password
	 * @return
	 */
	public String decryptPass(String password) {
		String result = null;
		try {
			result = desUtil.decrypt(password);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 加密密码
	 *
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public String encryptPass(String password) {
		String encrypt = null;
		try {
			encrypt = desUtil.encrypt(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encrypt;
	}

	@Component
	public class DesUtil {

		private ShiroConfig shiroConfig;

		/**
		 * 加密工具
		 */
		private Cipher encryptCipher;

		/**
		 * 解密工具
		 */
		private Cipher decryptCipher;

		/**
		 * 默认构造方法，使用默认密钥
		 *
		 */
		@Autowired
		public DesUtil(ShiroConfig shiroConfig) throws Exception {
			this.shiroConfig = shiroConfig;
			Key key = getKey(shiroConfig.getSecretKey().getBytes());
			encryptCipher = Cipher.getInstance("DES");
			encryptCipher.init(Cipher.ENCRYPT_MODE, key);

			decryptCipher = Cipher.getInstance("DES");
			decryptCipher.init(Cipher.DECRYPT_MODE, key);
		}

		/**
		 * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813 ,
		 * 和public static byte[] hexStr2ByteArr(String str) 互为可逆的转换过程
		 *
		 * @param arrB 需要转换的byte数组
		 * @return 转换后的字符串
		 * @throws Exception 本方法不处理任何异常，所有异常全部抛出
		 */
		public String byteArr2HexStr(byte[] arrB) throws Exception {
			int iLen = arrB.length;
			// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
			StringBuffer sb = new StringBuffer(iLen * 2);
			for (int i = 0; i < iLen; i++) {
				int intTmp = arrB[i];
				// 把负数转换为正数
				while (intTmp < 0) {
					intTmp = intTmp + 256;
				}
				// 小于0F的数需要在前面补0
				if (intTmp < 16) {
					sb.append("0");
				}
				sb.append(Integer.toString(intTmp, 16));
			}
			return sb.toString();
		}

		/**
		 * 将表示16进制值的字符串转换为byte数组 ,
		 * 和public static String byteArr2HexStr(byte[] arrB)互为可逆的转换过程
		 *
		 * @param str 需要转换的字符串
		 * @return 转换后的byte数组
		 * @throws Exception 本方法不处理任何异常，所有异常全部抛出
		 */
		public byte[] hexStr2ByteArr(String str) throws Exception {
			int step = 2;
			byte[] arrB = str.getBytes();
			int iLen = arrB.length;

			// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
			byte[] arrOut = new byte[iLen / 2];
			for (int i = 0; i < iLen; i = i + step) {
				String strTmp = new String(arrB, i, 2);
				arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
			}
			return arrOut;
		}

		/**
		 * 加密字节数组
		 *
		 * @param arrB 需加密的字节数组
		 * @return 加密后的字节数组
		 * @throws Exception
		 */
		public byte[] encrypt(byte[] arrB) throws Exception {
			return encryptCipher.doFinal(arrB);
		}

		/**
		 * 加密字符串
		 *
		 * @param str 需加密的字符串
		 * @return 加密后的字符串
		 * @throws Exception
		 */
		public String encrypt(String str) throws Exception {
			return byteArr2HexStr(encrypt(str.getBytes()));
		}

		/**
		 * 解密字节数组
		 *
		 * @param arrB 需解密的字节数组
		 * @return 解密后的字节数组
		 * @throws Exception
		 */
		public byte[] decrypt(byte[] arrB) throws Exception {
			return decryptCipher.doFinal(arrB);
		}

		/**
		 * 解密字符串
		 *
		 * @param str 需解密的字符串
		 * @return 解密后的字符串
		 * @throws Exception
		 */
		public String decrypt(String str) throws Exception {
			return new String(decrypt(hexStr2ByteArr(str)));
		}

		/**
		 * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
		 *
		 * @param arrBTmp 构成该字符串的字节数组
		 * @return 生成的密钥
		 * @throws Exception
		 */
		private Key getKey(byte[] arrBTmp) throws Exception {
			// 创建一个空的8位字节数组（默认值为0）
			byte[] arrB = new byte[8];

			// 将原始字节数组转换为8位
			for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
				arrB[i] = arrBTmp[i];
			}

			// 生成密钥
			Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

			return key;
		}
	}
}
