package cn.aegisa.spring.boot.mybatis.component.service;


import cn.aegisa.spring.boot.mybatis.component.spi.ICommonDao;
import cn.aegisa.spring.boot.mybatis.component.spi.IMapper;

import java.util.List;

/**
 * @author XianYingda
 */
public interface ICommonService extends IMapper {
    ICommonDao getCommonDao();

    void setCommonDao(ICommonDao commonDao);

    /**
     * 批量处理数据,非事务
     *
     * @param batchSize
     * @param beanList
     * @return
     */
    <T> Integer batchInsert(final Integer batchSize, List<T> beanList);
}
