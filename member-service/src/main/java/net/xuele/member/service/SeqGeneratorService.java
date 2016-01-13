package net.xuele.member.service;

/**
 *
 * zhihuan.cai 新建于 2015/7/30 0030.
 */
public interface SeqGeneratorService {

    /**
     * 生成序列
     * @return
     */
    Long generate(String tableName);

}
