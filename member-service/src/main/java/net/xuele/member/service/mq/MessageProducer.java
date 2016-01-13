package net.xuele.member.service.mq;

import net.xuele.member.constant.MemberMessageKeyEnum;
import net.xuele.member.dto.ModifyTableFieldDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wuxh on 15/9/7.
 */
@Component
public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public boolean sendData(MemberMessageKeyEnum key,ModifyTableFieldDTO tableDTO){

        rabbitTemplate.convertAndSend(key.getName(),tableDTO);

        return true;
    }

}
