package com.util.tools;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DateUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelUtil {
    private static final SimpleDateFormat formatDate1 = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat formatDate2 = new SimpleDateFormat("HH:mm:ss");

    public ExcelUtil() {
    }

    public void templateCreateExcel(String templateFilePath, String targetFilePath, Map<String, String> fieldMap, List<Map<String, String>>... listMapArgs) throws Exception {
        File file = new File(templateFilePath);
        if (!file.exists()) {
            return;
        }
        Map<String, String> fieldKeyMap = new HashMap<String, String>();
        Map<Integer, Map<String, String>> fieldListKeyMap = new HashMap<Integer, Map<String, String>>();
        InputStream in = new FileInputStream(file);
        POIFSFileSystem fileSystem = new POIFSFileSystem(in);
        HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
        HSSFSheet sheet = workbook.getSheetAt(0);
        int rs = sheet.getFirstRowNum();
        int re = sheet.getLastRowNum();
        for (int i = rs; i <= re; i++) {// 注意此处有=
            HSSFRow row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            int cs = row.getFirstCellNum();
            int ce = row.getLastCellNum();
            for (int j = cs; j < ce; j++) {// 注意此处无=
                HSSFCell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                } else {
                    String cellValue = getCellValue(cell);
                    if (cellValue == null || "".equals(cellValue) || "".equals(cellValue.trim())) {
                        continue;
                    }
                    String addFieldString = null;
                    String fieldString = getField(cellValue);
                    if (fieldString != null) {
                        addFieldString = i + "," + j + "#";
                        String fieldMapString = fieldKeyMap.get(fieldString);
                        if (fieldMapString != null) {
                            addFieldString = fieldMapString + addFieldString;
                        }
                        fieldKeyMap.put(fieldString, addFieldString);// 必须重新put
                        continue;
                    }
                    String fieldIndex = getListFieldIndex(cellValue);
                    fieldString = getListField(cellValue);
                    if (fieldIndex != null && fieldString != null) {
                        Map<String, String> fieldListIndexMap = fieldListKeyMap.get(Integer.valueOf(fieldIndex));// Integer.valueOf(fieldIndex)不能直接写成fieldIndex
                        if (fieldListIndexMap == null) {
                            fieldListIndexMap = new HashMap<String, String>();
                            fieldListIndexMap.put("$_row", String.valueOf(i));
                            fieldListKeyMap.put(Integer.valueOf(fieldIndex), fieldListIndexMap);// 注意添加此行
                        }
                        addFieldString = j + ",";
                        String indexkey = fieldListIndexMap.get(fieldString);
                        if (indexkey != null) {
                            addFieldString = indexkey + addFieldString;
                        }
                        fieldListIndexMap.put(fieldString, addFieldString);// 可以自动更新fieldListKeyMap
                    }
                }
            }
        }
        // 写固定字段
        Iterator<Entry<String, String>> iterator1 = fieldKeyMap.entrySet().iterator();
        while (iterator1.hasNext()) {
            Entry<String, String> entry1 = iterator1.next();
            String fieldNameString = entry1.getKey();
            String fieldCoordString = entry1.getValue();
            String fieldValueString = fieldMap.get(fieldNameString);
            String[] rowCellStrings = fieldCoordString.split("#");
            for (String rowCellString : rowCellStrings) {
                if ("".equals(rowCellString)) {
                    continue;
                }
                String[] rowCells = rowCellString.split(",");
                int rowIndex = Integer.parseInt(rowCells[0]);
                int colIndex = Integer.parseInt(rowCells[1]);
                HSSFRow row = sheet.getRow(rowIndex);
                HSSFCell cell = row.getCell(colIndex);
                if (fieldValueString != null) {
                    if (isNumber(fieldValueString)) {
                        // 是数字当作double处理
                        cell.setCellValue(Double.parseDouble(fieldValueString));
                    } else {
                        HSSFRichTextString richString = new HSSFRichTextString(fieldValueString);
                        cell.setCellValue(richString);
                    }
                } else {
                    cell.setCellValue("");
                }
            }
            iterator1.remove();
        }

        int insertedRows = 0;
        for (int k = 0; listMapArgs != null && k < listMapArgs.length; k++) {
            List<Map<String, String>> list = listMapArgs[k];
            int size = list.size();
            Map<String, String> keyColumnMap = fieldListKeyMap.get(k);
            if (keyColumnMap == null) {
                continue;
            }
            int oldRowIndex = Integer.parseInt(keyColumnMap.get("$_row"));
            int newRowIndex = oldRowIndex + insertedRows;
            insertRow(sheet, newRowIndex, size - 1);// 少插入一行，因为有一行为模板行
            insertedRows = insertedRows + size - 1;
            for (int t = 0; t < size; t++) {
                Map<String, String> rowValueMap = list.get(t);// 行值
                Iterator<Entry<String, String>> iterator2 = keyColumnMap.entrySet().iterator();
                while (iterator2.hasNext()) {
                    Entry<String, String> entry1 = iterator2.next();
                    String fieldNameString = entry1.getKey();
                    if ("$_row".equals(fieldNameString)) {
                        continue;
                    }
                    String fieldColumnString = entry1.getValue();
                    String fieldValueString = rowValueMap.get(fieldNameString);
                    String[] colCells = fieldColumnString.split(",");
                    for (String colCell : colCells) {
                        if ("".equals(colCell)) {
                            continue;
                        }
                        int colIndex = Integer.parseInt(colCell);
                        HSSFRow row = sheet.getRow(newRowIndex);
                        if (row == null) {
                            continue;
                        }
                        HSSFCell cell = row.getCell(colIndex);
                        if (cell == null) {
                            continue;
                        }
                        // System.out.println("newRowIndex:"+newRowIndex+"======colIndex:"+colIndex);
                        if (fieldValueString != null) {
                            if (isNumber(fieldValueString)) {
                                // 是数字当作double处理
                                cell.setCellValue(Double.parseDouble(fieldValueString));
                            } else {
                                HSSFRichTextString richString = new HSSFRichTextString(fieldValueString);
                                cell.setCellValue(richString);
                            }
                        } else {
                            cell.setCellValue("");
                        }
                    }
                }
                newRowIndex++;
            }
        }
        OutputStream os = new FileOutputStream(targetFilePath);
        workbook.write(os);
        os.flush();
        os.close();
        fieldListKeyMap.clear();
    }

    /**
     * 从startRow处插入rows行，并与startRow行样式相同
     *
     * @param sheet
     * @param startRow
     * @param rows
     */
    private void insertRow(HSSFSheet sheet, int startRow, int rows) {
        int endRow = startRow + rows;
        sheet.shiftRows(startRow, sheet.getLastRowNum(), rows, true, false);
        for (int i = 0; i < rows; i++) {
            HSSFRow targetRow = sheet.createRow(startRow++);
            HSSFRow sourceRow = sheet.getRow(endRow);
            for (int j = sourceRow.getFirstCellNum(); j < sourceRow.getLastCellNum(); j++) {
                HSSFCell sourceCell = sourceRow.getCell(j);
                if (sourceCell != null) {
                    HSSFCellStyle cellStyle = sourceCell.getCellStyle();
                    targetRow.createCell(j).setCellStyle(cellStyle);
                }
            }
        }
    }

    private String getField(String cellValue) {
        Pattern pattern = Pattern.compile("f\\{(\\w+)\\}");
        Matcher matcher = pattern.matcher(cellValue);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String getListFieldIndex(String cellValue) {
        Pattern pattern = Pattern.compile("fs(\\d+)\\{(\\w+)\\}");
        Matcher matcher = pattern.matcher(cellValue);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String getListField(String cellValue) {
        Pattern pattern = Pattern.compile("fs(\\d+)\\{(\\w+)\\}");
        Matcher matcher = pattern.matcher(cellValue);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }

    protected boolean isBlankRow(HSSFRow row) {
        if (row == null) {
            return true;
        }
        int cs = row.getFirstCellNum();
        int ce = row.getLastCellNum();
        int blank = 0;
        for (int j = cs; j < ce; j++) {// 注意此处无=
            HSSFCell cell = row.getCell(j);
            if (cell == null) {
                blank = blank + 1;
            } else {
                String cellValue = getCellValue(cell);
                if (cellValue == null || "".equals(cellValue) || "".equals(cellValue.trim())) {
                    blank = blank + 1;
                }
            }
        }
        if (blank == ce - cs) {
            return true;
        }
        return false;
    }

    private boolean isNumber(String text) {
        Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
        Matcher matcher = p.matcher(text);
        return matcher.matches();
    }

    private String getCellValue(HSSFCell cell) {
        if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            short dataFormat = cell.getCellStyle().getDataFormat();
            if (dataFormat == 14 || dataFormat == 31 || dataFormat == 57 || dataFormat == 58) {
                double dateValue1 = cell.getNumericCellValue();
                Date date1 = DateUtil.getJavaDate(dateValue1);
                return formatDate1.format(date1);
            } else if (dataFormat == 20 || dataFormat == 32) {
                double dateValue2 = cell.getNumericCellValue();
                Date date2 = DateUtil.getJavaDate(dateValue2);
                return formatDate2.format(date2);
            }
        }
        String value = "";
        int cellType = cell.getCellType();
        switch (cellType) {
            case HSSFCell.CELL_TYPE_BLANK:
                value = "";
                break;
            case HSSFCell.CELL_TYPE_ERROR:
                value = "";
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                value = String.valueOf(new BigDecimal(cell.getNumericCellValue()));
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                value = String.valueOf(new BigDecimal(cell.getNumericCellValue()));
                break;
            case HSSFCell.CELL_TYPE_STRING:
                value = String.valueOf(cell.getRichStringCellValue());
                break;
            default:
                value = "";
                break;
        }
        return value;
    }

    public void arrayExportExcel(String title, String[] headers, Collection<String[]> dataset, OutputStream out) {
        try {
            // 声明一个工作薄
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 生成一个表格
            HSSFSheet sheet = workbook.createSheet(title);
            // 设置表格默认列宽度为15个字节
            sheet.setDefaultColumnWidth(15);
            // 生成标题样式
            HSSFCellStyle style1 = workbook.createCellStyle();
            // 设置标题样式
            style1.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
            style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            // 生成标题字体
            HSSFFont font1 = workbook.createFont();
            font1.setColor(HSSFColor.VIOLET.index);
            font1.setFontHeightInPoints((short) 14);
            font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            // 把字体应用到标题样式
            style1.setFont(font1);
            // 生成并设置数据单元格样式
            HSSFCellStyle style2 = workbook.createCellStyle();
            style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
            style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            // 生成另一个字体
            HSSFFont font2 = workbook.createFont();
            font2.setColor(HSSFColor.BLUE.index);
            font2.setFontHeightInPoints((short) 10);
            font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
            // 把字体应用到当前的样式
            style2.setFont(font2);

            int index = 0;
            HSSFRow row = null;
            HSSFCell cell = null;
            // 产生表格标题行
            if (headers != null) {
                row = sheet.createRow(index);
                for (int i = 0; i < headers.length; i++) {
                    cell = row.createCell(i);
                    cell.setCellStyle(style1);
                    HSSFRichTextString text = new HSSFRichTextString(headers[i]);
                    cell.setCellValue(text);
                }
                index++;
            }

            // 遍历集合数据，产生数据行
            Iterator<String[]> it = dataset.iterator();
            while (it.hasNext()) {
                row = sheet.createRow(index);
                String[] objects = it.next();
                for (int i = 0; i < objects.length; i++) {
                    cell = row.createCell(i);
                    cell.setCellStyle(style2);
                    // 判断值的类型后进行强制类型转换
                    String textValue = objects[i];
                    // 利用正则表达式判断textValue是否全部由数字组成
                    if (textValue != null) {
                        if (isNumber(textValue)) {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            HSSFRichTextString richString = new HSSFRichTextString(textValue);
                            cell.setCellValue(richString);
                        }
                    } else {
                        cell.setCellValue("");
                    }
                }
                index++;
            }
            // OutputStream os = new FileOutputStream("E:/a.xls");
            // workbook.write(os);
            // os.flush();
            // os.close();
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
