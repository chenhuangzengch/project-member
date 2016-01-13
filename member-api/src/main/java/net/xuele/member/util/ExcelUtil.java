package net.xuele.member.util;

import net.xuele.member.constant.ExcelConstants;
import net.xuele.member.dto.ExcelInfo;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author ZhengTao 2015-6-22
 */
public class ExcelUtil {

    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
    /**
     * 读取一个excel 文件
     *
     * @param file 文件
     * @return {@link Map} key是0,1,...代表第几张sheet,value是行列集合
     * @throws IOException 文件不存在.读取excel异常
     */
    public static Map<Integer, List<List<Object>>> read(File file) throws IOException, InvalidFormatException {
        return read(new FileInputStream(file));
    }

    /**
     * 读取一个excel 文件
     *
     * @param is 数据流
     * @return {@link Map} key是0,1,...代表第几张sheet,value是行列集合
     * @throws IOException 文件不存在, 读取excel异常
     */
    public static Map<Integer, List<List<Object>>> read(InputStream is)
            throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(is);
        Map<Integer, List<List<Object>>> sheetDataMap = new HashMap<Integer, List<List<Object>>>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet currentSheet = workbook.getSheetAt(i);// 获取当前工作薄,从第一个开始
            List<List<Object>> data = getDataFromSheet(currentSheet);
            sheetDataMap.put(i, data);
        }
        return sheetDataMap;
    }

    private static List<List<Object>> getDataFromSheet(Sheet sheet) {
        NumberFormat format = new DecimalFormat("#.#");
        List<List<Object>> data = new ArrayList<List<Object>>();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            List<Object> rows = new ArrayList<Object>();
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            int minColIx = row.getFirstCellNum();
            int maxColIx = row.getLastCellNum();
            for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                Cell cell = row.getCell(colIx);
                if (cell == null) {
                    rows.add("");
                    continue;
                }
                switch (cell.getCellType()) {
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        rows.add(format.format(cell.getNumericCellValue()));
                        break;
                    default:
                        rows.add(cell.toString().trim());
                }
            }
            data.add(rows);
        }
        return data;
    }

    /**
     * 基本类型数据生成一个sheet(数据都调用<code>toString()</code>方法)
     *
     * @param infos excel 信息
     * @return {@link HSSFWorkbook}
     */
    public static HSSFWorkbook getWorkBook(ExcelInfo... infos) {
        HSSFWorkbook wbk = new HSSFWorkbook();// 新建一个工作薄
        for (ExcelInfo info : infos) {
            String[] headers = info.getHeaders();

            HSSFSheet sheet = wbk.createSheet(info.getSheetName());
            sheet.setDefaultColumnWidth(info.getColumnWidth());
            HSSFRow rowCell;
            HSSFCell cell;
            //HSSFRichTextString text = null;
            int rowNum = 0;
            if (headers != null && headers.length != 0) {
                rowCell = sheet.createRow(rowNum++);
                for (int i = 0; i < headers.length; i++) {
                    cell = rowCell.createCell(i);
                    cell.setCellValue(headers[i]);
                }
            }
            List<List<Object>> data = info.getData();
            if (data == null || data.isEmpty()) {
                return wbk;
            }
            for (List<Object> rowData : data) {
                rowCell = sheet.createRow(rowNum++);
                if (rowData == null || rowData.isEmpty()) {
                    continue;
                }
                int size = rowData.size();
                Object c;
                for (int i = 0; i < size; i++) {
                    cell = rowCell.createCell(i);
                    c = rowData.get(i);
                    if (c == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(c.toString());
                    }
                }
            }
        }
        return wbk;
    }

    /**
     * 导出excel文件
     *
     * @param request  {@link HttpServletRequest} 用于判断请求端是什么浏览器
     * @param response {@link HttpServletResponse} 返回数据
     * @param workBook {@link HSSFWorkbook}工作簿
     * @param fileName 返回给用户的文件名
     * @throws IOException 读写异常
     */
    public static void export(HttpServletRequest request, HttpServletResponse response,
                              HSSFWorkbook workBook, String fileName) throws IOException {
        response.setContentType("application/force-download");
        response.setContentType("application/vnd.ms-excel");
        saveAgent(request, response, fileName);
        OutputStream out = response.getOutputStream();
        workBook.write(out);
        out.flush();    //close()系统会调用
    }

    /**
     * 下载指定路径的Excel模板
     */
    public static void exportExcelDemo(HttpServletRequest request, HttpServletResponse response, File file) throws IOException {
        String fileName = file.getName();
        //模板路径
        response.setContentType("application/force-download");
        response.setContentType("application/vnd.ms-excel");
        saveAgent(request, response, fileName);
        String path = file.getPath();
        // 读到流中
        try (InputStream inStream = new FileInputStream(path)) {
            IOUtils.copy(inStream, response.getOutputStream());
        }
    }

    /**
     * 设置浏览器
     */
    private static void saveAgent(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        String agent = request.getHeader("User-Agent");
        if (agent != null) {
            agent = agent.toLowerCase();
            if (agent.indexOf("msie") != -1) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
                fileName = fileName.replaceAll("\\+", "%20"); // 解决编码后空格变+号的情况
            } else {
                fileName = "=?UTF-8?B?"
                        + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")), "UTF-8")) + "?=";
            }
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
    }

    /**
     * 判断文件是否是excel格式
     *
     * @param fileName 文件名
     * @return true:是 false:不是
     */
    public static boolean judgeExcelFormat(String fileName) {
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if ("xls".equals(suffix) || "xlsx".equals(suffix)) {
            return true;
        }
        return false;
    }

    /**
     * 判断excel是否是模板
     *
     * @param lists excel中的数据
     * @return true:是 false:不是
     */
    public static boolean judgeExcelContent(ArrayList<ArrayList<Object>> lists) {
        if (lists.size() > 0
                && lists.get(0).size() >= 6
                && (((String) lists.get(0).get(0)).trim()).contains(ExcelConstants.FIRST_CELL)
                && (((String) lists.get(0).get(1)).trim()).contains(ExcelConstants.SECOND_CELL)
                && (((String) lists.get(0).get(2)).trim()).contains(ExcelConstants.THIRD_CELL)
                && (((String) lists.get(0).get(3)).trim()).contains(ExcelConstants.FOURTH_CELL)
                && (((String) lists.get(0).get(4)).trim()).contains(ExcelConstants.FIFTH_CELL)
                && (((String) lists.get(0).get(5)).trim()).contains(ExcelConstants.SIXTH_CELL)) {
            return true;
        } else {
            return false;
        }
    }


    public static void main(String[] args) throws IOException, InvalidFormatException {
//      String fileName = "office2007not.xlsx";//office 2007 格式
        String fileName = "canread2007office.xlsx";//wps 2007格式
        String path = ExcelUtil.class.getClassLoader().getResource(fileName).getPath();

        InputStream in = new FileInputStream(new File(path));
        if (in != null) {
            read(in);
        }

        if (in != null){
            in.close();
        }
    }
}
