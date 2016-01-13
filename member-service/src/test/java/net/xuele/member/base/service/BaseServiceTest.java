package net.xuele.member.base.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ZhengTao
 *         单元测试基类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:service-test.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public abstract class BaseServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
    /*@Before
    public void before() {
        String fileName = getSqlFileName();
        if (StringUtils.isNotEmpty(fileName)) {
            executeSqlScript("classpath:h2/" + fileName, false);
        }
    }

    protected String getSqlFileName() {
        return null;
    }*/
}
