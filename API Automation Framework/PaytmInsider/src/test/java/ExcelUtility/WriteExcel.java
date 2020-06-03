package ExcelUtility;


	 
	import java.io.FileInputStream;
	import java.io.FileNotFoundException;
	import java.io.FileOutputStream;
	import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
	import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
	import org.apache.poi.xssf.usermodel.XSSFWorkbook;
	 
	public class WriteExcel {
		
		static XSSFWorkbook workbook ;
		XSSFSheet sheet;
		XSSFRow row;
		public WriteExcel(String exceplpath,String sheetname)
		{
			try{
			FileInputStream fis = new FileInputStream(exceplpath);
			
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheet(sheetname);
			}catch (Exception exp) {
				// TODO Auto-generated catch block
				
				System.out.println(exp.getCause());
				System.out.println(exp.getMessage());
				exp.printStackTrace();
	            }
		}
	 
		
		public void setCellData(List<String> listname) throws IOException
		{
			
			List<String> list = listname;
			int rownum=0;
		    for (String date : list )
				
			{
				row = sheet.createRow(rownum++);

			        Cell cell = row.createCell(0);
			        cell.setCellValue(date);
			}
			FileOutputStream fos = new FileOutputStream("./Excelfiles/Test.xlsx");
			workbook.write(fos);
			fos.close();
			System.out.println("END OF WRITING DATA IN EXCEL");
		}
	}