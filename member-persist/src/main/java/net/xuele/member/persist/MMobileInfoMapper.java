package net.xuele.member.persist;

import net.xuele.member.domain.MMobileInfo;

public interface MMobileInfoMapper {
    //int deleteByPrimaryKey(String id);

    //MMobileInfo selectByPrimaryKey(String id);
    //
    //List<MMobileInfo> selectAll();
    //
    //int updateByPrimaryKey(MMobileInfo record);

    /**
     * 插入用户设备信息表
     *
     * @param record 用户设备信息表数据
     */
    int insert(MMobileInfo record);
}