package com.rbittencourt.aws.cost.miner.application.sheet;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class SheetWriter {

    public void updateSheet() throws IOException {
        FileInputStream inputStream = new FileInputStream("aws-cost-miner-ec2-report.xlsx");
        Workbook workbook = WorkbookFactory.create(inputStream);

        Sheet sheet = workbook.getSheetAt(1);

        Row row = sheet.getRow(5);
        row.getCell(2).setCellValue(180.50);

        Row row1 = sheet.getRow(6);
        row1.getCell(2).setCellValue(200.50);

        Row row2 = sheet.getRow(7);
        row2.getCell(2).setCellValue(500.50);

        workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

        FileOutputStream fileOut = new FileOutputStream("aws-cost-miner-ec2-report.xlsx");
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

}
