package net.xuele.member.service;

import net.xuele.member.dto.PeriodDTO;
import net.xuele.member.dto.SchoolPeriodDTO;

import java.util.List;


/**
 * Created by kaike.du on 2015/6/23 0023.
 */
public interface SchoolPeriodService {

    /**
     * 保存某学校的学段学制信息
     *
     * @param forcible 是否强制修改，强制修改会造成隐患
     * @param periodDTO 学制信息
     * @param schoolId  学校id
     * @param forcible
     */
    void savePeriod(PeriodDTO periodDTO, String schoolId, Boolean forcible);

    /**
     * 根据学校ID获取学制学段信息
     *
     * @param schoolId 学校ID
     * @return 学制学段信息
     */
    List<SchoolPeriodDTO> queryBySchoolId(String schoolId);

    /**
     * 根据用户id返回该用户所对应的学段
     * 老师：学校的学段
     * 学生：所在班级对应的学段
     *
     * @param userId   用户id
     * @param schoolId 学校id
     * @return 0:小学 1:初中 2:高中
     */
    List<Integer> queryPeriodByUserId(String userId, String schoolId);

    /**
     * 同步课堂年级获取(小学和初中)：通过学校id获取该学校需要设置同步课堂的年级列表
     * @param schoolId 学校id
     * @return 年级列表
     */
    List<Integer> getSysGradeNum(String schoolId);
}
