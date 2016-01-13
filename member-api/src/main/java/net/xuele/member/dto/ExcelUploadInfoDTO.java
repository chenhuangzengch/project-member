package net.xuele.member.dto;

import java.util.List;

/**
 * Created by Administrator on 2015/9/18 0018.
 */
public class ExcelUploadInfoDTO implements java.io.Serializable {
    private String errorInfo;
    private List<ExcelStateDTO> excelStateDTOs;

    public String getErrorInfo() {
        return errorInfo;
    }

    public List<ExcelStateDTO> getExcelStateDTOs() {
        return excelStateDTOs;
    }

    public void setExcelStateDTOs(List<ExcelStateDTO> excelStateDTOs) {
        this.excelStateDTOs = excelStateDTOs;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
}
