package com.jca.systemset;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.jca.datacommon.tool.StringUtils;
import com.jca.datatool.EmptyUtils;
 
public class LatitudeUtils {
	
	public static final String KEY_1 = "7d9fbeb43e975cd1e9477a7e5d5e192a";
	public static final String KEY_2="A1LU7iHS0avqQwPLAxbhKn0UYSQCuRVH";
	// String key2 = "8ec6cd98bd56554407a207d597c0f3e5";
	// String key3 = "be025dc280e1e3f7ffb95fe42a01fab2";
	// String key4 = "327db7009617d6806b9c38e819ea06ac";
	
	/**
	 * 返回输入地址的经纬度坐标
	 * key lng(经度),lat(纬度)
	 */
	public static Map<String,String> getGeocoderLatitude(String address){
		BufferedReader in = null;
		try {
			//将地址转换成utf-8的16进制
			address = URLEncoder.encode(address, "UTF-8");
//       如果有代理，要设置代理，没代理可注释
//		System.setProperty("http.proxyHost","192.168.1.188");
//		System.setProperty("http.proxyPort","3128");
			URL tirc = new URL("http://api.map.baidu.com/api?v=2.0&address="+ address +"&output=json&key="+ KEY_1);
			
			in = new BufferedReader(new InputStreamReader(tirc.openStream(),"UTF-8"));
			String res;
			StringBuilder sb = new StringBuilder("");
			while((res = in.readLine())!=null){
				sb.append(res.trim());
			}
			String str = sb.toString();
			Map<String,String> map = null;
			if(EmptyUtils.isNotEmpty(str)){
				int lngStart = str.indexOf("lng\":");
				int lngEnd = str.indexOf(",\"lat");
				int latEnd = str.indexOf("},\"precise");
				if(lngStart > 0 && lngEnd > 0 && latEnd > 0){
					String lng = str.substring(lngStart+5, lngEnd);
					String lat = str.substring(lngEnd+7, latEnd);
					map = new HashMap<String,String>();
					map.put("lng", lng);
					map.put("lat", lat);
					return map;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public static void main(String args[]){
		Map<String, String> json = GetLatAndLngByBaidu.getLngAndLat("深圳市捷成安科技");
		System.out.println("lng : "+json.get("lng"));
		System.out.println("lat : "+json.get("lat"));
	}
	
}