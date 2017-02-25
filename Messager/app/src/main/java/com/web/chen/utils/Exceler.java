package com.web.chen.utils;

import com.web.chen.entities.ExcelContent;
import com.web.chen.exception.ExcelException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CHEN on 2017/1/11.
 *
 * 处理excel表格
 *
 */
public class Exceler {

    public static List<ExcelContent> returnContent(String fileName) {
        List<ExcelContent> excelContents = new ArrayList<>();
        FileInputStream input = null;
        Workbook wb = null;
        try {
            input = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fileName.endsWith(".xls")) {
            try {
                wb = new HSSFWorkbook(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (fileName.endsWith(".xlsx")) {
            try {
                wb = new XSSFWorkbook(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new ExcelException("该文件不是excel");
        }

        Sheet sheet = wb.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        int lastCellNum = sheet.getRow(0).getLastCellNum();
        for (int i = 1; i <=lastRowNum; i++) {
            Row row = sheet.getRow(i);//第i行
            ExcelContent excelContent = new ExcelContent();
            List<String> contents = new ArrayList<>();
            for (int j = 0; j < lastCellNum; j++) {
                contents.add(returnString(row.getCell(j)));
            }
            excelContent.setContent(contents);
            excelContents.add(excelContent);
        }
        try {
            input.close();
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return excelContents;
    }

    public static String returnString(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                return cell.toString();
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            default:
                return "";
        }
    }

}
