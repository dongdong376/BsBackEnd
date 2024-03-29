package com.jca.databeans.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 生成器
 * 
 * @author Administrator
 *
 */
@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
public class Generator {

	public static void main(String[] args) throws Exception {
		// 创建Configuration对象
		Configuration configuration = new Configuration();
		// 设置模板所在目录
		String path = Generator.class.getClassLoader().getResource("template").getPath();
		System.out.println(path);
		configuration.setDirectoryForTemplateLoading(new File(path));
		// 获取模板
		Template template = configuration.getTemplate("pojo.ftl");
		// 设置数据并执行
		Map map = new HashMap();
		String mapperPath="C:\\Users\\Administrator\\Desktop\\Working\\jcaWork\\jca-bsbackend"
				+ "\\jca-datadao\\src\\main\\java\\com\\jca\\datadao\\mapperxml\\";
		String beanPath="E:\\team\\git\\data\\jca-bsbackend\\jca-databeans\\src\\main\\java\\com\\jca\\databeans\\pojo\\";
		//mapper:C:\Users\Administrator\Desktop\Working\jcaWork\jca-bsbackend\jca-datadao\src\main\java\com\jca\datadao\mapperxml
		//C:\Users\Administrator\Desktop\Working\jcaWork\jca-bsbackend\jca-datadao\src\main\java\com\jca\datadao\mapperxml
		ClassGenerator classGenerator = new ClassGenerator();
		MyClass myClass = classGenerator.generateClass("t_f_owner_apply");
			map.put("myClass", myClass);
			Writer writer = new OutputStreamWriter(new FileOutputStream(beanPath+ myClass.getClassName() + ".java"));
			template.process(map, writer);
	}
}