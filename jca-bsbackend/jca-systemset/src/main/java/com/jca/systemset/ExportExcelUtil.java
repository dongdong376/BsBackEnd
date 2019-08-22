//package com.jca.systemset;
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.lang.reflect.Method;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.jca.databeans.pojo.TFProperty;
//
//import jxl.Workbook;
//import jxl.format.Alignment;
//import jxl.format.Border;
//import jxl.format.BorderLineStyle;
//import jxl.format.VerticalAlignment;
//import jxl.write.Label;
//import jxl.write.WritableCellFormat;
//import jxl.write.WritableFont;
//import jxl.write.WritableSheet;
//import jxl.write.WritableWorkbook;
//import jxl.write.WriteException;
//import jxl.write.biff.JxlWriteException;
//
//public class ExportExcelUtil {
//
//	private static final Logger logger = LoggerFactory.getLogger(ExportExcelUtil.class);
//	private final static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
//	
//	public static final String[] TestToXls = { "物业编号","物业名称", "物业状态","创建时间", "地理位置"};
//	
//	private static String fileName;
//	
//	/**
//	 * 导出excel
//	 * 
//	 * @param fileName
//	 * @param exportMap
//	 * @param listContent
//	 * @param response
//	 * @return
//	 */
//	@SuppressWarnings("rawtypes")
//	public final static String exportExcel(String fileName, Map<String, String> exportMap,
//			List<? extends Object> listContent, HttpServletResponse response) {
//		String result = "success";
//		// 以下开始输出到EXCEL
//		try {
//			// 定义输出流，以便打开保存对话框______________________begin
//			// HttpServletResponse response=ServletActionContext.getResponse();
//			OutputStream os = response.getOutputStream();// 取得输出流
//			response.reset();// 清空输出流
//			response.setHeader("Content-disposition",
//					"attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
//			// 设定输出文件头
//			response.setContentType("application/msexcel");// 定义输出类型
//			// 定义输出流，以便打开保存对话框_______________________end
//
//			/** **********创建工作簿************ */
//			WritableWorkbook workbook = Workbook.createWorkbook(os);
//
//			/** **********创建工作表************ */
//
//			WritableSheet sheet = workbook.createSheet("Sheet1", 0);
//
//			/** **********设置纵横打印（默认为纵打）、打印纸***************** */
//			jxl.SheetSettings sheetset = sheet.getSettings();
//			sheetset.setProtected(false);
//
//			/** ************设置单元格字体************** */
//			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
//			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
//
//			/** ************以下设置三种单元格样式，灵活备用************ */
//			// 用于标题居中
//			WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
//			wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
//			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
//			wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
//			wcf_center.setWrap(false); // 文字是否换行
//
//			// 用于正文居左
//			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
//			wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
//			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
//			wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
//			wcf_left.setWrap(false); // 文字是否换行
//
//			/** ***************以下是EXCEL开头大标题，暂时省略********************* */
//			// sheet.mergeCells(0, 0, colWidth, 0);
//			// sheet.addCell(new Label(0, 0, "XX报表", wcf_center));
//			/** ***************以下是EXCEL第一行列标题********************* */
//			Object[] columArr = exportMap.keySet().toArray();
//			Object[] filedNameArr = exportMap.values().toArray();
//			sheet.addCell(new Label(0, 0, "序号", wcf_center));
//			for (int i = 0; i < columArr.length; i++) {
//				sheet.addCell(new Label(i + 1, 0, columArr[i].toString(), wcf_center));
//			}
//			/** ***************以下是EXCEL正文数据********************* */
//			int i = 1;
//			for (Object obj : listContent) {
//
//				int j = 0;
//				String fieldNameTemp = null;
//				Object value = null;
//				Method method=null;
//				Class methodTypeClass=null;
//				sheet.addCell(new Label(j, i, String.valueOf(i), wcf_left));
//				j++;
//				for (Object fieldName : filedNameArr) {
//					fieldNameTemp = "get" + fieldName.toString().substring(0, 1).toUpperCase()
//							+ fieldName.toString().substring(1);
//					method = obj.getClass().getMethod(fieldNameTemp, null);
//					methodTypeClass = method.getReturnType();
//					//时间格式转换
//					if (Date.class.getSimpleName().equals(methodTypeClass.getSimpleName())) {
//						value = dateFormat.format((Date)method.invoke(obj, null));
//					}else{
//						value = method.invoke(obj, null);
//					}
//					if (value == null) {
//						value = "";
//					}
//					sheet.addCell(new Label(j, i, value.toString(), wcf_left));
//					j++;
//				}
//				i++;
//			}
//			/** **********将以上缓存中的内容写到EXCEL文件中******** */
//			workbook.write();
//			/** *********关闭文件************* */
//			workbook.close();
//			logger.info("导出excel成功");
//
//		} catch (Exception e) {
//			result = "error";
//			logger.error("导出excel失败", e);
//		}
//		return result;
//	}
//
//	
//	public static void toExcel(List<TFProperty> list,HttpServletResponse response) {	
//		//这里为导出文件存放的路径	
//		String filePath ="C:\\Users\\Administrator\\Desktop\\Working\\jcaWork"
//				+ "\\jca-bsbackend\\jca-systemset\\target\\jca-systemset\\" + UUID.randomUUID() + "\\";	
//		//加入一个uuid随机数是因为	
//		//每次导出的时候，如果文件存在了，会将其覆盖掉，这里是保存所有的文件	
//		File file = new File(filePath);	
//		if (!file.exists()) {		
//			file.mkdirs();	
//		}
//		
//		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");	
//		// 给要导出的文件起名为 "测试导出数据表_时间.xls"	
//		String filePath2 = filePath + "物业表" + "_" + fmt.format(new Date()) + ".xls";	
//		WritableWorkbook wb = null;	
//		try {		
//			File file2 = new File(filePath2);		
//			if (!file2.exists()) {//不存在，创建			
//				file2.createNewFile();		
//			}		
//			wb = Workbook.createWorkbook(file2);//创建xls表格文件
//		
//			// 表头显示
//			WritableCellFormat wcf = new WritableCellFormat();		
//			wcf.setAlignment(Alignment.CENTRE);// 水平居中		
//			wcf.setWrap(true);		
//			wcf.setVerticalAlignment(VerticalAlignment.CENTRE);// 垂直居中		
//			wcf.setFont(new WritableFont(WritableFont.TIMES,13, WritableFont.BOLD));// 表头字体 加粗 13号		
//			wcf.setBackground(jxl.format.Colour.PERIWINKLE);
//			// 内容显示		
//			WritableCellFormat wcf2 = new WritableCellFormat();		
//			wcf2.setWrap(true);//设置单元格可以换行		
//			wcf2.setAlignment(Alignment.CENTRE);//水平居中		
//			wcf2.setVerticalAlignment(VerticalAlignment.CENTRE);// 垂直居中		
//			wcf2.setFont( new WritableFont(WritableFont.TIMES,11));// 内容字体 11号
//	 
//			//导出的xls的第一页，第二页就是0换成1，“sheet1”，也可以修改为自己想要的显示的内容
//			WritableSheet ws = wb.createSheet("sheet1", 0);		
//			//WritableSheet ws2 = wb.createSheet("sheet2", 1);//第2个sheet页		
//			ws.addCell(new Label(0,0, "导出结果"));//代表着表格中第一列的第一行显示查询结果几个字
//	 
//			// 导出时生成表头		
//			for (int i = 0; i < TestToXls.length; i++) {
//				//i,代表的第几列，1，代表第2行，第三个参数为要显示的内容，第四个参数，为内容格式设置（按照wcf的格式显示）			
//				ws.addCell(new Label(i, 1, TestToXls[i],wcf));//在sheet1中循环加入表头
//			}
//					
//			//查询出来的数据,这个方法是演示所用		
//			String sql="com.Test.Service.findAllUser";//sql为mybatis框架下的路径		
//			//Map<String, Object> map = new HashMap<String, Object>();//map里为存放前台的条件
//			//map.put("prnte",  this.getParameter("prnteTest"));
//			int k =2 ;//从第三行开始写入数据
//			
//			for (int i = 0; i < list.size(); i++) {			
//				ws.addCell(new Label(0, k, list.get(i).getPropertyNo(), wcf2));
//				ws.addCell(new Label(1, k, list.get(i).getPropertyName(),wcf2));
//				ws.addCell(new Label(2, k, list.get(i).getCreateTime(),wcf2));
//				ws.addCell(new Label(3, k, list.get(i).getIsDown().toString(),wcf2));
//				ws.addCell(new Label(4, k, list.get(i).getAddress(),wcf2));
//				//ws.mergeCells(4, 5, 5, 5);//合并两列，按参数顺序，意思是第4列的第五行，跟第五列的第五行合并为一个单元格			
//				k++;		
//			}		
//			wb.write();//写入，到这里已经生成完成，可以在相应目录下找到刚才生成的文件	
//		} catch (IOException e) {
//			e.printStackTrace();	
//		} catch (JxlWriteException e) {
//			e.printStackTrace();	
//		} catch (WriteException e) {
//			e.printStackTrace();	
//		} finally {		
//			try {			
//				if (wb != null) {
//					wb.close();
//				}		
//			} catch (WriteException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}	
//		}	
//		//这个是我们项目中，是把刚才生成的文件，响应到前台，进行下载、保存，可省略。	
//		downLoadFile(filePath2,response);
//	}
//
//	public static void downLoadFile(String filePath,HttpServletResponse response) {
//		fileName=filePath.substring(filePath.lastIndexOf("\\")+1);
//		FileInputStream in = null;		
//		ServletOutputStream out = null;		
//		BufferedOutputStream toOut = null;		
//		try {			
//			in = new FileInputStream(new File(filePath));			
//			byte[] buffer = new byte[in.available()];			
//			while (in.read(buffer) != -1) {				
//				//HttpServletResponse response = this.getContext().getResponse();//从application中得到response				
//				response.reset();// 清空				
//				// 设置响应的文件的头文件格式
//				response.setContentType("application/octet-stream");
//				response.setHeader("Content-Disposition",
//						"attachment;filename="+ new 
//						String(fileName.getBytes("GBK"),"ISO8859-1"));
//				response.addHeader("Content-type", "application-download");
//				// 获取响应的对象流
//				out = response.getOutputStream();
//				toOut = new BufferedOutputStream(out);
//				toOut.write(buffer);
//				toOut.flush();
//			}		
//		} catch (Exception e) {	
//			e.printStackTrace();		} 
//		finally {
//			try {
//				if(in!=null) {	
//					in.close();	
//				}				
//				if(out != null) {
//					out.close();
//				}
//				if(toOut != null) {
//					toOut.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}		
//		}
//	}
//}