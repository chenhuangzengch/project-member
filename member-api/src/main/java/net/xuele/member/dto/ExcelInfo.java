package net.xuele.member.dto;

import java.io.Serializable;
import java.util.List;

public class ExcelInfo implements Serializable{
    private static final long serialVersionUID = -1816470434751351688L;
    /**
     * sheet名字
     */
    private String sheetName="Sheet1";
    /**
     * 第一行
     */
    private String[] headers;
    /**
     * 数据,<code>Object.toString()</code>
     */
    private List<List<Object>> data;
    /**
     * 一个单元格的宽度
     */
    private int columnWidth = 10;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<List<Object>> getData() {
        return data;
    }

    public void setData(List<List<Object>> data) {
        this.data = data;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public int getColumnWidth() {
        return columnWidth;
    }

}
