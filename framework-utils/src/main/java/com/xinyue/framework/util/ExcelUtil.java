package com.xinyue.framework.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

	/**
	 * @Description: 创建生成exel表格
	 * @param @param title
	 * @param @param dataset
	 * @param @param out   
	 * @return void  
	 * @throws
	 * @author pengzhihao
	 * @date 2016-1-22下午4:37:48
	 */
	public static void exportExcel(String title, Collection<Object[]> dataset, OutputStream out) {
		exportExcel(title, null, dataset, out);
	}

	/**
	 * @Description: 创建生成exel表格
	 * @param @param title 文件名
	 * @param @param headers 头部
	 * @param @param dataset 数据
	 * @param @param out   输出流
	 * @return void  
	 * @throws
	 * @author pengzhihao
	 * @date 2016-1-22下午4:35:45
	 */
	@SuppressWarnings("deprecation")
	public static void exportExcel(String title, String[] headers,
			Collection<Object[]> dataset, OutputStream out) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		//sheet.setDefaultColumnWidth((short) 15);
		
		// 生成一个样式（标题样式）
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		// 把字体应用到当前的样式
		style.setFont(font);
		
		// 生成并设置另一个样式（表格内容）
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		
		// 把字体应用到当前的样式
		style2.setFont(font2);

		/*// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
				0, 0, 0, (short) 4, 2, (short) 6, 5));
		
		// 设置注释内容
		comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
		
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("pengzhihao");*/
		HSSFFont font3 = workbook.createFont();
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		Iterator<Object[]> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			Object[] obj = it.next();
			for (int i = 0; i < obj.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style2);
				String textValue = obj[i] + "";
				// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
				if (textValue != null) {
					HSSFRichTextString richString = new HSSFRichTextString(
							textValue);
					//不用多次创建.
					//HSSFFont font3 = workbook.createFont();
					font3.setColor(HSSFColor.BLUE.index);
					richString.applyFont(font3);
					cell.setCellValue(richString);
				}
			}

		}
		
		try {
			workbook.write(out);
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}
	/**
	 * @Description: 创建生成exel多个工作sheet
	 * @param @param title 文件名
	 * @param @param headers 头部
	 * @param @param dataset 数据
	 * @param @param out   输出流
	 * @param @param rowCount   每个sheet多少行，不大于65535
	 * @return void  
	 * @throws
	 * @author chenzhichao
	 * @date 2017-5-17上午10:35:45
	 */
	@SuppressWarnings("deprecation")
	public static void exportExcelForManySheet(String title, String[] headers, List<Object[]> dataset, OutputStream out, Integer rowCount) {
		Integer rows = dataset.size() / rowCount;
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFCellStyle style = createCellStyleByWorkbookForHeader(workbook);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		// 把字体应用到当前的样式
		style.setFont(font);
		HSSFCellStyle style2 = createCellStyleByWorkbookForRow(workbook);

		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

		// 把字体应用到当前的样式
		style2.setFont(font2);

		for (int m = 0; m < rows; m++) {
			List<Object[]> datasetSingleSheet = new ArrayList<Object[]>();
			for (int n = 0; n < rowCount; n++) {
				datasetSingleSheet.add(dataset.get(n + rowCount * m));
			}
			HSSFSheet sheet = workbook.createSheet(title + (m + 2));
			// 产生表格标题行
			HSSFRow row = sheet.createRow(0);
			for (short i = 0; i < headers.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style);
				HSSFRichTextString text = new HSSFRichTextString(headers[i]);
				cell.setCellValue(text);
			}
			// 遍历集合数据，产生数据行
			Iterator<Object[]> it = datasetSingleSheet.iterator();
			int index = 0;
			while (it.hasNext()) {
				index++;
				row = sheet.createRow(index);
				Object[] obj = it.next();
				for (int i = 0; i < obj.length; i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(style2);
					String textValue = obj[i] + "";
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						HSSFRichTextString richString = new HSSFRichTextString(textValue);
						cell.setCellValue(richString);
					}
				}
			}
		}
		List<Object[]> datasetLast = new ArrayList<Object[]>();
		for (int m = rowCount * rows; m < dataset.size(); m++) {
			datasetLast.add(dataset.get(m));
		}

		HSSFSheet sheet = workbook.createSheet(title);

		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		Iterator<Object[]> it = datasetLast.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			Object[] obj = it.next();
			for (int i = 0; i < obj.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style2);
				String textValue = obj[i] + "";
				// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
				if (textValue != null) {
					HSSFRichTextString richString = new HSSFRichTextString(textValue);
					cell.setCellValue(richString);
				}
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static HSSFCellStyle createCellStyleByWorkbookForHeader(HSSFWorkbook workbook) {
		// 设置这些样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		return style;
	}

	public static HSSFCellStyle createCellStyleByWorkbookForRow(HSSFWorkbook workbook) {
		// 生成并设置另一个样式（表格内容）
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return style;
	}

	public static void main(String[] args) {
		
		String[] headers = { "设备id", "包名", "SessionId", "级别", "日志消息","客户端ip", "创建时间" };
		
		List<Object[]> dataset = new ArrayList<Object[]>();
		for (int i = 0; i < 8713; i++) {
			Object[] objs = new Object[7];
			objs[0] = "860312026375103";
			objs[1] = "com.example.debugdemo";
			objs[2] = "373922512";
			objs[3] = "1";
			objs[4] = "I am a test log msg 2 ";
			objs[5] = "172.10.1.17";
			objs[6] = "2015-07-09 12:01:26";
			dataset.add(objs);
		}
		
		OutputStream out = null;
		try {
			out = new FileOutputStream("E://a1.xls");
			
			ExcelUtil.exportExcel("log日志", headers, dataset, out);
			//ExcelUtil.exportExcelForManySheet("log日志", headers, dataset, out,1);
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	 /**
     * 
    * @Title: readXls 
    * @Description: 处理xls文件
    * @param @param path
    * @param @return
    * @param @throws Exception    设定文件 
    * @return List<List<String>>    返回类型 
    * @throws
    * 
    * 从代码不难发现其处理逻辑：
    * 1.先用InputStream获取excel文件的io流
    * 2.然后穿件一个内存中的excel文件HSSFWorkbook类型对象，这个对象表示了整个excel文件。
    * 3.对这个excel文件的每页做循环处理
    * 4.对每页中每行做循环处理
    * 5.对每行中的每个单元格做处理，获取这个单元格的值
    * 6.把这行的结果添加到一个List数组中
    * 7.把每行的结果添加到最后的总结果中
    * 8.解析完以后就获取了一个List<List<String>>类型的对象了
    * 
     */
    public static List<List<String>> readXls(String path) throws Exception {
        InputStream is = new FileInputStream(path);
        // HSSFWorkbook 标识整个excel
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        List<List<String>> result = new ArrayList<List<String>>();
        int size = hssfWorkbook.getNumberOfSheets();
        // 循环每一页，并处理当前循环页
        for (int numSheet = 0; numSheet < size; numSheet++) {
            // HSSFSheet 标识某一页
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 处理当前页，循环读取每一行
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                // HSSFRow表示行
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                int minColIx = hssfRow.getFirstCellNum();
                int maxColIx = hssfRow.getLastCellNum();
                List<String> rowList = new ArrayList<String>();
                // 遍历改行，获取处理每个cell元素
                for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                    // HSSFCell 表示单元格
                    HSSFCell cell = hssfRow.getCell(colIx);
                    if (cell == null) {
                        continue;
                    }
                    rowList.add(getStringVal(cell));
                }
                result.add(rowList);
            }
        }
        return result;
    }

    /**
     * 
    * @Title: readXlsx 
    * @Description: 处理Xlsx文件
    * @param @param path
    * @param @return
    * @param @throws Exception    设定文件 
    * @return List<List<String>>    返回类型 
    * @throws
     */
    public static List<List<String>> readXlsx(String path) throws Exception {
        InputStream is = new FileInputStream(path);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        List<List<String>> result = new ArrayList<List<String>>();
        // 循环每一页，并处理当前循环页
        for (XSSFSheet xssfSheet : xssfWorkbook) {
            if (xssfSheet == null) {
                continue;
            }
            // 处理当前页，循环读取每一行
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                int minColIx = xssfRow.getFirstCellNum();
                int maxColIx = xssfRow.getLastCellNum();
                List<String> rowList = new ArrayList<String>();
                for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                    XSSFCell cell = xssfRow.getCell(colIx);
                    if (cell == null) {
                        continue;
                    }
                    rowList.add(cell.toString());
                }
                result.add(rowList);
            }
        }
        return result;
    }
    public static List<List<String>> readXlsxRec(String path) throws Exception {
    	InputStream is = new FileInputStream(path);
    	XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
    	List<List<String>> result = new ArrayList<List<String>>();
    	// 循环每一页，并处理当前循环页
    	for (XSSFSheet xssfSheet : xssfWorkbook) {
    		if (xssfSheet == null) {
    			continue;
    		}
    		// 处理当前页，循环读取每一行
    		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
    			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
    			int minColIx = xssfRow.getFirstCellNum();
    			int maxColIx = xssfRow.getLastCellNum();
    			List<String> rowList = new ArrayList<String>();
    			for (int colIx = minColIx; colIx < maxColIx; colIx++) {
    				XSSFCell cell = xssfRow.getCell(colIx);
    				if (cell == null) {
    					continue;
    				}
    				rowList.add(getStringVal(cell));
    			}
    			result.add(rowList);
    		}
    	}
    	return result;
    }

    // 存在的问题
    /*
     * 其实有时候我们希望得到的数据就是excel中的数据，可是最后发现结果不理想
     * 如果你的excel中的数据是数字，你会发现Java中对应的变成了科学计数法。
     * 所以在获取值的时候就要做一些特殊处理来保证得到自己想要的结果
     * 网上的做法是对于数值型的数据格式化，获取自己想要的结果。
     * 下面提供另外一种方法，在此之前，我们先看一下poi中对于toString()方法:
     * 
     * 该方法是poi的方法，从源码中我们可以发现，该处理流程是：
     * 1.获取单元格的类型
     * 2.根据类型格式化数据并输出。这样就产生了很多不是我们想要的
     * 故对这个方法做一个改造。
     */
    /*public String toString(){
        switch(getCellType()){
            case CELL_TYPE_BLANK:
                return "";
            case CELL_TYPE_BOOLEAN:
                return getBooleanCellValue() ? "TRUE" : "FALSE";
            case CELL_TYPE_ERROR:
                return ErrorEval.getText(getErrorCellValue());
            case CELL_TYPE_FORMULA: 
                return getCellFormula();
            case CELL_TYPE_NUMERIC:
                if(DateUtil.isCellDateFormatted(this)){
                    DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy")
                    return sdf.format(getDateCellValue());
                }
                return getNumericCellValue() + "";
            case CELL_TYPE_STRING:  
                return getRichStringCellValue().toString();
            default :
                return "Unknown Cell Type:" + getCellType();
        }
    }*/

    /**
     * 改造poi默认的toString（）方法如下
    * @Title: getStringVal 
    * @Description: 1.对于不熟悉的类型，或者为空则返回""控制串
    *               2.如果是数字，则修改单元格类型为String，然后返回String，这样就保证数字不被格式化了
    * @param @param cell
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public static String getStringVal(HSSFCell cell) {
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_BOOLEAN:
            return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
        case Cell.CELL_TYPE_FORMULA:
            return cell.getCellFormula();
        case Cell.CELL_TYPE_NUMERIC:
            cell.setCellType(Cell.CELL_TYPE_STRING);
            return cell.getStringCellValue();
        case Cell.CELL_TYPE_STRING:
            return cell.getStringCellValue();
        default:
            return "";
        }
    }
    public static String getStringVal(XSSFCell cell) {
    	switch (cell.getCellType()) {
    	case Cell.CELL_TYPE_BOOLEAN:
    		return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
    	case Cell.CELL_TYPE_FORMULA:
    		return cell.getCellFormula();
    	case Cell.CELL_TYPE_NUMERIC:
    		cell.setCellType(Cell.CELL_TYPE_STRING);
    		return cell.getStringCellValue();
    	case Cell.CELL_TYPE_STRING:
    		return cell.getStringCellValue();
    	default:
    		return "";
    	}
    }
}
