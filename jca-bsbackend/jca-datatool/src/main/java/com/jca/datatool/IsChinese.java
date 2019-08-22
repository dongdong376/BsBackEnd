package com.jca.datatool;

public class IsChinese {
	public static boolean isChinese(char c){
		return c >= 0x4E00 &&  c <= 0x9FA5;// 根据字节码判断
	}
	
	public static boolean isChinese(String str){
		if(str == null){
			return false;
		}
		for(char c:str.toCharArray()){
			if(isChinese(c)){
				return true;
			}
		}
		return false;
	}
}
