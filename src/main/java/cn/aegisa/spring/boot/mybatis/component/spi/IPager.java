package cn.aegisa.spring.boot.mybatis.component.spi;

import java.util.List;

/**
 * @author XianYingda
 */
public interface IPager {

    default <T> Integer getMaxId(List<T> dataList) {
        return dataList.stream().map(o -> getBeanId(o)).reduce(Math::max).get();
    }

    /**
     * 获取Bean的主键
     *
     * @param bean
     * @param <T>
     * @return
     */
    <T> Integer getBeanId(T bean);


    /**
     * 处理业务数据
     *
     * @param dataList 数据列表
     * @param <T>
     */
    <T> void processingData(List<T> dataList);
}
