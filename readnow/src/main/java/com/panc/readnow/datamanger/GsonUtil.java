package com.panc.readnow.datamanger;

import com.google.gson.Gson;

/**
 * 封装的是使用Gson解析json的方法
 * @author Administrator
 *
 */
public class GsonUtil {
	/**
	 * 把一个json字符串变成对象
	 * @param json
	 * @param cls
	 * @return
	 */
	public static <T> T parseJsonToBean(String json, Class<T> cls) {
		Gson gson = new Gson();
		T t = null;
		try {
			t = gson.fromJson(json, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

}
