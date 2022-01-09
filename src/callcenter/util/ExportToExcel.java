/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package callcenter.util;

import call.center.controller.utils.MessageDialog;
import call.center.model.QuestionInformation;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Developer
 */
public class ExportToExcel implements Runnable{
    private final ObservableList<? extends IExcel> allData;
    private final XSSFWorkbook workBook=new XSSFWorkbook();
    private final XSSFSheet sheet;
    private final String fileName;
        public ExportToExcel(ObservableList<? extends IExcel> allData, String dateTime) {
            this.allData = allData;
            sheet=workBook.createSheet();
            fileName=FileUtils.ALL_EXCEL_FILES_FOLDER+dateTime+".xlsx";
        }
        
        private void startExport() throws IOException{
            writeRow(sheet.createRow(0), allData.get(0).getHeaders());
            int rowIndex=1;
            for(IExcel item:allData){
                writeRow(sheet.createRow(rowIndex++), item.getFullData());
            }
            File file=new File(fileName);
            if(file.exists()){
               file.delete();
            }
            workBook.write(new FileOutputStream(file));
            workBook.close();
        }
        private void writeRow(XSSFRow row,String[]allItems){
            for(int i=0;i<allItems.length;i++){
                XSSFCell cell=row.createCell(i);
                cell.setCellValue(allItems[i]);
            }
        }
        
        @Override
        public void run() {
            try {
                startExport();
                Desktop.getDesktop().open(new File(fileName));
            } catch (IOException ex) {
                Logger.getLogger(ExportToExcel.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
}
