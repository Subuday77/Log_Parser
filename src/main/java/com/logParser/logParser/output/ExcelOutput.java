package com.logParser.logParser.output;

import com.logParser.logParser.beans.AnalyseBeans.ResultST;
import com.logParser.logParser.beans.AnalyseBeans.TransactionST;
import com.logParser.logParser.beans.AnalyseBeans.UserST;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.text.SimpleDateFormat;


import static com.logParser.logParser.beans.Constants.COLUMNS;

@Component
public class ExcelOutput {

    String pattern = "dd-MM-yyyy(HH-mm-ss)";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    public ResponseEntity<?> createReport(long operatorId, ResultST resultST) {
        String date = simpleDateFormat.format(new Date());
        String path = "files/" + "ST_" + operatorId + "_" + resultST.getRounds().get(0).getUsersST().size()+"PL" + "_" + date + ".xlsx";
        XSSFWorkbook workbook = null;
        Sheet sheet = null;
        File file = new File(path);
        int index = 1;
        for (UserST user : resultST.getRounds().get(0).getUsersST()) {
            if (file.exists()) {
                FileInputStream fileinp = null;
                try {
                    fileinp = new FileInputStream(path);
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                try {
                    workbook = new XSSFWorkbook(fileinp);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    sheet = workbook.createSheet(user.getUid());
                } catch (Exception e) {
                    sheet = workbook.createSheet("User_" + index);
                }
            } else {
                workbook = new XSSFWorkbook();
                try {
                    sheet = workbook.createSheet(user.getUid());
                } catch (Exception e) {
                    sheet = workbook.createSheet("User_" + index);
                }
            }
//            sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, 15));
            sheet.createFreezePane(0, 1);
            CellStyle style = workbook.createCellStyle();

            style.setWrapText(true);

            Font headerFont = workbook.createFont();
            Font whiteFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            whiteFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setBorderBottom(BorderStyle.DOUBLE);
            headerCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            headerCellStyle.setBorderLeft(BorderStyle.DOUBLE);
            headerCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            headerCellStyle.setBorderRight(BorderStyle.DOUBLE);
            headerCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            headerCellStyle.setBorderTop(BorderStyle.DOUBLE);
            headerCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            headerCellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle.setFont(headerFont);
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < COLUMNS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(COLUMNS[i]);
                cell.setCellStyle(headerCellStyle);

            }
            CellStyle bodyCellStyle = workbook.createCellStyle();
            CellStyle bodyCellStyleWithColor = workbook.createCellStyle();
            CellStyle bodyCellStyleWithError = workbook.createCellStyle();
            CellStyle bodyCellStyleTopRow = workbook.createCellStyle();
            CellStyle green = workbook.createCellStyle();
            CellStyle red = workbook.createCellStyle();


            bodyCellStyle.setBorderBottom(BorderStyle.THIN);
            bodyCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            bodyCellStyle.setBorderLeft(BorderStyle.THIN);
            bodyCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            bodyCellStyle.setBorderRight(BorderStyle.THIN);
            bodyCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            bodyCellStyle.setBorderTop(BorderStyle.THIN);
            bodyCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

            bodyCellStyleWithColor.setBorderBottom(BorderStyle.THIN);
            bodyCellStyleWithColor.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            bodyCellStyleWithColor.setBorderLeft(BorderStyle.THIN);
            bodyCellStyleWithColor.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            bodyCellStyleWithColor.setBorderRight(BorderStyle.THIN);
            bodyCellStyleWithColor.setRightBorderColor(IndexedColors.BLACK.getIndex());
            bodyCellStyleWithColor.setBorderTop(BorderStyle.THIN);
            bodyCellStyleWithColor.setTopBorderColor(IndexedColors.BLACK.getIndex());
            bodyCellStyleWithColor.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            bodyCellStyleWithColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            bodyCellStyleWithError.setBorderBottom(BorderStyle.THIN);
            bodyCellStyleWithError.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            bodyCellStyleWithError.setBorderLeft(BorderStyle.THIN);
            bodyCellStyleWithError.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            bodyCellStyleWithError.setBorderRight(BorderStyle.THIN);
            bodyCellStyleWithError.setRightBorderColor(IndexedColors.BLACK.getIndex());
            bodyCellStyleWithError.setBorderTop(BorderStyle.THIN);
            bodyCellStyleWithError.setTopBorderColor(IndexedColors.BLACK.getIndex());
            bodyCellStyleWithError.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            bodyCellStyleWithError.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            bodyCellStyleWithError.setFillForegroundColor(IndexedColors.CORAL.getIndex());
            bodyCellStyleWithError.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            bodyCellStyleTopRow.setBorderBottom(BorderStyle.THIN);
            bodyCellStyleTopRow.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            bodyCellStyleTopRow.setBorderLeft(BorderStyle.THIN);
            bodyCellStyleTopRow.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            bodyCellStyleTopRow.setBorderRight(BorderStyle.THIN);
            bodyCellStyleTopRow.setRightBorderColor(IndexedColors.BLACK.getIndex());

            green.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            green.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            green.setFont(whiteFont);
            red.setFillForegroundColor(IndexedColors.RED.getIndex());
            red.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            red.setFont(whiteFont);

            int rowNum = 1;
            boolean errorsFound = false;
            Row row = sheet.createRow(rowNum);
            List<TransactionST> txns = user.getTransactions();
            txns.sort(Comparator.comparingLong(TransactionST::getTimestamp).reversed());
            for (TransactionST transaction : txns) {
                HashMap<Integer, Object> values = new HashMap<>();
                values.put(0, transaction.getRoundId());
                values.put(1, transaction.getOperatorId());
                values.put(2, transaction.getUid());
                values.put(3, transaction.getGameType());
                values.put(4, transaction.getTableId());
                values.put(5, transaction.getSeatId());
                values.put(6, transaction.getDate());
                values.put(7, transaction.getBetType());
                values.put(8, transaction.getBet());
                values.put(9, transaction.getWin());
                values.put(10, transaction.getCurrency());
                values.put(11, transaction.getBalance());
                values.put(12, transaction.getErrorCode());
                values.put(13, transaction.getTransactionId());
                values.put(14, transaction.getTimestamp());
                values.put(15, transaction.isCorrectPlace());

                for (int i = 0; i < COLUMNS.length; i++) {
                    Cell cell = row.createCell(i);
                    if (values.get(i) instanceof String || values.get(i) instanceof Boolean) {
                        String value = "";
                        if (values.get(i) instanceof Boolean) {
                            if (values.get(i).equals(true)) {
                                value = "V";
                            } else {
                                value = "X";
                                errorsFound = true;
                            }
                        } else {
                            value = String.valueOf(values.get(i));
                        }
                        cell.setCellValue(value);
                    }
                    if (values.get(i) instanceof Double) {
                        double value = (double) values.get(i);
                        cell.setCellValue(value);
                    }
                    if (values.get(i) instanceof Long) {
                        long value = (long) values.get(i);
                        cell.setCellValue(value);
                    }
                    if (values.get(i) instanceof Integer) {
                        int value = (int) values.get(i);
                        cell.setCellValue(value);
                    }
//                  Set  Styles  for cells

                    if (rowNum == 1) {
                        cell.setCellStyle(bodyCellStyleTopRow);
                        if (i == COLUMNS.length - 1) {
                            CellStyle center = workbook.getCellStyleAt(bodyCellStyleTopRow.getIndex()).copy();
                            center.setAlignment(HorizontalAlignment.CENTER);
                            cell.setCellStyle(center);
                        }
                        if (values.get(i) instanceof Long) {
                            DataFormat df = workbook.createDataFormat();
                            CellStyle format = workbook.getCellStyleAt(bodyCellStyleTopRow.getIndex()).copy();
                            format.setDataFormat(df.getFormat("0"));
                            cell.setCellStyle(format);
                        }
                        if (values.get(i) instanceof Double) {
                            DataFormat df = workbook.createDataFormat();
                            CellStyle format = workbook.getCellStyleAt(bodyCellStyleTopRow.getIndex()).copy();
                            format.setDataFormat(df.getFormat("0.00"));
                            cell.setCellStyle(format);
                        }
                    } else {
                        cell.setCellStyle(bodyCellStyle);
                        if (i == COLUMNS.length - 1) {
                            CellStyle center = workbook.getCellStyleAt(bodyCellStyle.getIndex()).copy();
                            center.setAlignment(HorizontalAlignment.CENTER);
                            cell.setCellStyle(center);
                        }
                        if (values.get(i) instanceof Long) {
                            DataFormat df = workbook.createDataFormat();
                            CellStyle format = workbook.getCellStyleAt(bodyCellStyle.getIndex()).copy();
                            format.setDataFormat(df.getFormat("0"));
                            cell.setCellStyle(format);
                        }
                        if (values.get(i) instanceof Double) {
                            DataFormat df = workbook.createDataFormat();
                            CellStyle format = workbook.getCellStyleAt(bodyCellStyle.getIndex()).copy();
                            format.setDataFormat(df.getFormat("0.00"));
                            cell.setCellStyle(format);
                        }
                    }
                    if (rowNum % 2 == 0) {
                        cell.setCellStyle(bodyCellStyleWithColor);
                        if (i == COLUMNS.length - 1) {
                            CellStyle center = workbook.getCellStyleAt(bodyCellStyleWithColor.getIndex()).copy();
                            center.setAlignment(HorizontalAlignment.CENTER);
                            cell.setCellStyle(center);
                        }
                        if (values.get(i) instanceof Long) {
                            DataFormat df = workbook.createDataFormat();
                            CellStyle format = workbook.getCellStyleAt(bodyCellStyleWithColor.getIndex()).copy();
                            format.setDataFormat(df.getFormat("0"));
                            cell.setCellStyle(format);
                        }
                        if (values.get(i) instanceof Double) {
                            DataFormat df = workbook.createDataFormat();
                            CellStyle format = workbook.getCellStyleAt(bodyCellStyleWithColor.getIndex()).copy();
                            format.setDataFormat(df.getFormat("0.00"));
                            cell.setCellStyle(format);
                        }
                    }
                    if (!txns.get(rowNum - 1).isCorrectPlace()) {
                        cell.setCellStyle(bodyCellStyleWithError);
                        if (i == COLUMNS.length - 1) {
                            CellStyle center = workbook.getCellStyleAt(bodyCellStyleWithError.getIndex()).copy();
                            center.setAlignment(HorizontalAlignment.CENTER);
                            cell.setCellStyle(center);
                        }
                        if (values.get(i) instanceof Long) {
                            DataFormat df = workbook.createDataFormat();
                            CellStyle format = workbook.getCellStyleAt(bodyCellStyleWithError.getIndex()).copy();
                            format.setDataFormat(df.getFormat("0"));
                            cell.setCellStyle(format);
                        }
                        if (values.get(i) instanceof Double) {
                            DataFormat df = workbook.createDataFormat();
                            CellStyle format = workbook.getCellStyleAt(bodyCellStyleWithError.getIndex()).copy();
                            format.setDataFormat(df.getFormat("0.00"));
                            cell.setCellStyle(format);
                        }
                    }
                }
                rowNum++;
                row = sheet.createRow(rowNum);
            }
            DataFormat df = workbook.createDataFormat();
            CellStyle format = workbook.createCellStyle();
            format.setDataFormat(df.getFormat("0.00"));
            row.createCell(8).setCellFormula("SUM(I2:I" + rowNum + ")");
            row.getCell(8).setCellStyle(format);
            row.createCell(9).setCellFormula("SUM(J2:J" + rowNum + ")");
            row.getCell(9).setCellStyle(format);
            row.createCell(10).setCellValue("Expected:");
            rowNum++;
            row.createCell(11).setCellFormula("J" + rowNum + "-" + "I" + rowNum);
            row.getCell(11).setCellStyle(format);
            row = sheet.createRow(rowNum);
            row.createCell(10).setCellValue("By timestamp:");
            row.createCell(11).setCellValue(user.getLastShownBalance());
            row.getCell(11).setCellStyle(format);
            if (user.getLastShownBalance() != user.getExpectedBalance()) {
                CellStyle format1 = workbook.getCellStyleAt(red.getIndex()).copy();
                format1.setDataFormat(df.getFormat("0.00"));
                row.getCell(11).setCellStyle(format1);
            }
            rowNum++;
            row = sheet.createRow(rowNum);
            row.createCell(10).setCellValue("By balance:");
            row.createCell(11).setCellValue(user.getLastReturnedBalance());
            row.getCell(11).setCellStyle(format);
            if (user.getLastReturnedBalance() != user.getExpectedBalance()) {
                CellStyle format1 = workbook.getCellStyleAt(red.getIndex()).copy();
                format1.setDataFormat(df.getFormat("0.00"));
                row.getCell(11).setCellStyle(format1);
            }
            rowNum++;
            row = sheet.createRow(rowNum);
            row.createCell(10).setCellValue("Result:");
            if (user.getLastShownBalance() != user.getExpectedBalance() || user.getLastReturnedBalance() != user.getExpectedBalance() || errorsFound) {
                row.createCell(11).setCellValue("Failed");
                row.getCell(11).setCellStyle(red);
            } else {
                row.createCell(11).setCellValue("Passed");
                row.getCell(11).setCellStyle(green);
            }
// Get rid of formulas
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            CellReference cellReference = new CellReference("I" + (rowNum - 2));
            row = sheet.getRow(cellReference.getRow());
            Cell cell = row.getCell(cellReference.getCol());
            evaluator.evaluateFormulaCell(cell);
            Double tempValue = cell.getNumericCellValue();
            cell.removeFormula();
            cell.setCellValue(tempValue);

            cellReference = new CellReference("J" + (rowNum - 2));
            row = sheet.getRow(cellReference.getRow());
            cell = row.getCell(cellReference.getCol());
            evaluator.evaluateFormulaCell(cell);
            tempValue = cell.getNumericCellValue();
            cell.removeFormula();
            cell.setCellValue(tempValue);

            cellReference = new CellReference("L" + (rowNum - 2));
            row = sheet.getRow(cellReference.getRow());
            cell = row.getCell(cellReference.getCol());
            evaluator.evaluateFormulaCell(cell);
            tempValue = cell.getNumericCellValue();
            cell.removeFormula();
            cell.setCellValue(tempValue);
// End of get rid of formulas
            for (int i = 0; i < COLUMNS.length; i++) {
                sheet.autoSizeColumn(i);
            }
            FileOutputStream fileOut = null;
            try {
                fileOut = new FileOutputStream(path);
                workbook.write(fileOut);
                fileOut.close();
                workbook.close();
            } catch (Exception e) {
                return new ResponseEntity<String>("Error creating file", HttpStatus.EXPECTATION_FAILED);
            }
            index++;
        }
        return new ResponseEntity<String>(path, HttpStatus.OK);
    }

    public ResponseEntity<?> sendFile(String fileName) throws IOException {
        File file = new File("files/" + fileName);
        if (file.exists()) {
            Path path = Paths.get(file.getAbsolutePath());

            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            return ResponseEntity.ok().headers(headers).contentLength(file.length())
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(resource);
        }
        return new ResponseEntity<String>("File not found", HttpStatus.NOT_FOUND);
    }
}
