package net.xuele.member.persist;

import net.xuele.common.page.Page;
import net.xuele.member.domain.MRole;
import net.xuele.member.domain.MRoleResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MRoleMapper {



    int deleteByPrimaryKey(String roleId);

    int insert(MRole record);

    MRole selectByPrimaryKey(String roleId);

    List<MRole> selectAll();

    int updateByPrimaryKey(MRole record);

    int deleteByPrimaryKey(@Param("roleId") String roleId, @Param("resourceId") String resourceId);

    int insert(MRoleResource record);

    List<MRoleResource> selectRoleResAll();

    List<MRoleResource> selectRoleResByRoleId(String roleId);


    long selelctCountPage(MRole roleRequest);


    List<MRole> selectRoleByPage(@Param("page") Page page,@Param("mRole")MRole mRole);
}