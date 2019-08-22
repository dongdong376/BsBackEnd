package com.jca.datatool;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import org.apache.tools.ant.types.resources.selectors.Date;

public class MD5 {

	public static String getMd5(String plainText, int length) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位
			// return buf.toString();
			// 16位
			// return buf.toString().substring(0, 16);

			return buf.toString().substring(0, length);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static int getRandomCode() {
		int max = 9999;
		int min = 1111;
		Random random = new Random();
		return random.nextInt(max) % (max - min + 1) + min;
	}

	public static void main(String[] args) {
		System.out.println(MD5.getMd5("123456", 20));
		System.out.println(getRandomCode());
		System.out.println(new Date().getDatetime());
//		final Base64.Decoder decoder = Base64.getDecoder();
//		final Base64.Encoder encoder = Base64.getEncoder();
//		final String text = "字串文字";
//		byte[] textByte;
//		try {
//			textByte = text.getBytes("UTF-8");
//			//编码
//			final String encodedText = encoder.encodeToString(textByte);
//			System.out.println(encodedText);
//			//解码
//			System.out.println(new String(decoder.decode(encodedText), "UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
		//final Base64.Decoder decoder = Base64.getDecoder();
		//final Base64.Encoder encoder = Base64.getEncoder();
		//final String text = "字串文字";
		//final byte[] textByte = text.getBytes("UTF-8");
		//编码
		//final String encodedText = encoder.encodeToString(textByte);
		//System.out.println(encodedText);
		//解码
		//System.out.println(new String(decoder.decode(encodedText), "UTF-8"));
	}

}
