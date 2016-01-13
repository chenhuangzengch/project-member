package net.xuele.member.web.wrapper;

/**
 * Created by zhongjian.xu on 2015/6/27 0027.
 */
public class SummaryWrapper {
    /**
     * 科目ID
     */
    private String summaryCode;

    /**
     * 科目名称
     */
    private String summaryName;

    /**
     * 英文标识
     */
    private String summaryMark;

    /**
     * 获取 [D_SUMMARY] 的属性 科目ID
     */
    public String getSummaryCode() {
        return summaryCode;
    }

    /**
     * 设置[D_SUMMARY]的属性科目ID
     */
    public void setSummaryCode(String summaryCode) {
        this.summaryCode = summaryCode == null ? null : summaryCode.trim();
    }

    /**
     * 获取 [D_SUMMARY] 的属性 科目名称
     */
    public String getSummaryName() {
        return summaryName;
    }

    /**
     * 设置[D_SUMMARY]的属性科目名称
     */
    public void setSummaryName(String summaryName) {
        this.summaryName = summaryName == null ? null : summaryName.trim();
    }

    /**
     * 获取 [D_SUMMARY] 的属性 英文标识
     */
    public String getSummaryMark() {
        return summaryMark;
    }

    /**
     * 设置[D_SUMMARY]的属性英文标识
     */
    public void setSummaryMark(String summaryMark) {
        this.summaryMark = summaryMark == null ? null : summaryMark.trim();
    }
}
