package net.xuele.member.persist;

import net.xuele.member.domain.MUserIcon;
import org.apache.ibatis.annotations.Param;

public interface MUserIconMapper {

    int insert(MUserIcon record);
    void updateStatusByUserIdAndIcon(@Param("userId") String userId,@Param("icon") String icon,@Param("status") Integer status);
    MUserIcon getByUserIdAndIcon(@Param("userId") String userId,@Param("icon") String icon);

}