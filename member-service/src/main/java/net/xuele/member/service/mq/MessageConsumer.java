package net.xuele.member.service.mq;

import com.alibaba.fastjson.JSON;
import net.xuele.member.constant.MemberMessageTypeEnum;
import net.xuele.member.dto.ModifyTableFieldDTO;
import net.xuele.member.dto.SchoolDTO;
import net.xuele.member.persist.MClassMapper;
import net.xuele.member.persist.MSchoolManagerMapper;
import net.xuele.member.persist.MStudentMapper;
import net.xuele.member.persist.MTeacherMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wuxh on 15/9/7.
 */
@Component
public class MessageConsumer implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    @Autowired
    private MClassMapper mClassMapper;
    @Autowired
    private MStudentMapper mStudentMapper;
    @Autowired
    private MTeacherMapper mTeacherMapper;
    @Autowired
    private MSchoolManagerMapper mSchoolManagerMapper;
    @Override
    public void onMessage(Message message) {

        ModifyTableFieldDTO modifyTableFieldDTO = JSON.parseObject(message.getBody(), ModifyTableFieldDTO.class);

        if (modifyTableFieldDTO.getType() == MemberMessageTypeEnum.MODIFY_SCHOOL_NAME){
            SchoolDTO schoolDTO = JSON.parseObject(JSON.toJSONString(modifyTableFieldDTO.getTableDTO()), SchoolDTO.class);
            //根据学校id修改学校名 涉及的表有m_class、m_student、m_teacher、m_school_manager
            String schoolId = schoolDTO.getId();
            String schoolName = schoolDTO.getName();
            if (StringUtils.isNotEmpty(schoolId) && StringUtils.isNotEmpty(schoolName)) {
                mClassMapper.updateSchoolNameBySchoolId(schoolId,schoolName);
                mStudentMapper.updateSchoolNameBySchoolId(schoolId,schoolName);
                mTeacherMapper.updateSchoolNameBySchoolId(schoolId,schoolName);
                mSchoolManagerMapper.updateSchoolNameBySchoolId(schoolId,schoolName);
                logger.info("更新表冗余字段school_name：将school_id为{}的school_name更新为{}成功", schoolId, schoolName);
            }
        }
    }
}
