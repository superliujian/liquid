package liquid.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by redbrick9 on 6/11/14.
 */
@Service
public class SimpleExcelService {
    private static final Logger logger = LoggerFactory.getLogger(SimpleExcelService.class);

    public void read(InputStream inputStream) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        int numberOfSheets = workbook.getNumberOfSheets();
        String sheetName = null;
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (null == sheet) {
                logger.warn("Sheet {} is null. The previous sheet is {}.", i, sheetName);
                System.out.println(String.format("Sheet %s is null. The previous sheet is %s.", i, sheetName));
                continue;
            }
            sheetName = sheet.getSheetName();
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();
            System.out.println(String.format("Sheet name %s. The first row is %s, the last num is %s", sheetName, firstRowNum, lastRowNum));
            lastRowNum = lastRowNum <= 0 ? -1 : lastRowNum;
            for (int j = firstRowNum; j <= lastRowNum; j++) {
                Row row = sheet.getRow(j);
                if (null == row) {
                    logger.warn("Row {} is null. The sheet is {}.", j, sheetName);
                    System.out.println(String.format("Row %s is null. The sheet is %s.", j, sheetName));
                    continue;
                }
                int firstCellNum = row.getFirstCellNum();
                int lastCellNum = row.getLastCellNum();
                for (int k = firstCellNum; k < lastCellNum; k++) {
                    Cell cell = row.getCell(k);
                    if (null == cell) {
                        logger.warn("Cell {} is null. The row is {}, the sheet is {}.", k, j, sheetName);
                        System.out.println(String.format("Cell %s is null. The row is %s, the sheet is %s.", k, j, sheetName));
                        continue;
                    }
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_BLANK:
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            cell.getBooleanCellValue();
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            cell.getErrorCellValue();
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            cell.getCellFormula();
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            cell.getNumericCellValue();
                            break;
                        case Cell.CELL_TYPE_STRING:
                            cell.getStringCellValue();
                            break;
                    }
                }
            }
        }
    }
}
