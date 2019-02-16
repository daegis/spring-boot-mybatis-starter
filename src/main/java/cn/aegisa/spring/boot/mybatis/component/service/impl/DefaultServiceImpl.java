package cn.aegisa.spring.boot.mybatis.component.service.impl;

import cn.aegisa.spring.boot.mybatis.component.service.ICommonService;
import cn.aegisa.spring.boot.mybatis.component.spi.IMapper;
import cn.aegisa.spring.boot.mybatis.component.utils.Id;
import cn.aegisa.spring.boot.mybatis.component.utils.PageFinder;
import cn.aegisa.spring.boot.mybatis.component.utils.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author XianYingda
 */
public abstract class DefaultServiceImpl implements ICommonService {
    /**
     * 添加默认dao配置,便于dao的重载
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <V> V executeTransactional(IMapper.TransactionalVistor<V> vistor) {
        return getCommonDao().executeTransactional(vistor);
    }

    @Override
    public <T, V> List<V> getListBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair) {
        return getCommonDao().getListBySqlId(clzss, sqlId, keyValuePair);
    }

    @Override
    public <T, V> List<V> getListFromMasterBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair) {
        return getCommonDao().getListFromMasterBySqlId(clzss, sqlId, keyValuePair);
    }

    @Override
    public <T> Integer updateBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair) {
        return getCommonDao().updateBySqlId(clzss, sqlId, keyValuePair);
    }

    @Override
    public <T> List<T> getListBySqlId(Class clazz, String sqlId, Object params, Query query) {
        return getCommonDao().getListBySqlId(clazz, sqlId, params, query);
    }

    @Override
    public <T> List<T> getMasterListBySqlId(Class clazz, String sqlId, Object params, Query query) {
        return getCommonDao().getMasterListBySqlId(clazz, sqlId, params, query);
    }

    @Override
    public <T> Integer updateBySqlId(String sqlId, T paramBeam) {
        return getCommonDao().updateBySqlId(sqlId, paramBeam);
    }

    @Override
    public <PK, T, V> V get(final PK id, Class<T> clzss) {
        return getCommonDao().get(id, clzss);
    }

    @Override
    public <PK, T> int delete(PK id, Class<T> clzss) {
        return getCommonDao().delete(id, clzss);
    }

    @Override
    public <T> int save(T target) {
        return getCommonDao().save(target);
    }

    @Override
    public <T> int save(List<T> target) {
        return getCommonDao().save(target);
    }

    @Override
    public <T> int save(Class<T> clzss, String sqlId, Object... keyValuePair) {
        return getCommonDao().save(clzss, sqlId, keyValuePair);
    }

    @Override
    public <T> int update(T target) {
        return getCommonDao().update(target);
    }

    @Override
    public <T> int update(List<T> beanList) {
        return getCommonDao().update(beanList);
    }

    @Override
    public <PK, T, V> List<V> get(List<PK> ids, Class<T> clzss) {
        return getCommonDao().get(ids, clzss);
    }

    @Override
    public <PK, T> List<T> getListByField(List<PK> key, String fieldName, Class<T> clzss) {
        return getCommonDao().getListByField(key, fieldName, clzss);
    }

    public <PK, T, V> List<V> findMasterByIds(List<PK> ids, Class<T> clzss) {
        return getCommonDao().getFromMaster(ids, clzss);
    }

    @Override
    public <T, V> V get(T bean) {
        return getCommonDao().get(bean);
    }

    @Override
    public <T, V> List<V> getList(Class<T> clzss, Object paramsObj) {
        return getCommonDao().getList(clzss, paramsObj);
    }

    @Override
    public <T> Id getMinIdAndMaxId(Class<T> clzss, Object... keyValuePair) {
        return getCommonDao().getMinIdAndMaxId(clzss, keyValuePair);
    }

    @Override
    public <T, V> List<V> getDataList(Class<T> clzss, Object... keyValuePair) {
        return getCommonDao().getDataList(clzss, keyValuePair);
    }

    @Override
    public <T, V> List<V> getList(Class<T> clzss, Object... keyValuePair) {
        return getCommonDao().getList(clzss, keyValuePair);
    }


    @Override
    public <T, V> List<V> getByMap(Map<?, ?> map, Class<T> clzss) {
        return getCommonDao().getByMap(map, clzss);
    }


    @Override
    public <T, V> List<V> getList(T bean) {
        return getCommonDao().getList(bean);
    }


    @Override
    public <PK, T, V> V getFromMaster(PK key, Class<T> clzss) {
        return getCommonDao().getFromMaster(key, clzss);
    }


    @Override
    public <PK, T, V> List<V> getFromMaster(List<PK> key, Class<T> clzss) {
        return getCommonDao().getFromMaster(key, clzss);
    }


    @Override
    public <T, V> V getFromMaster(T bean) {
        return getCommonDao().getFromMaster(bean);
    }


    @Override
    public <T, V> List<V> getListFromMaster(T bean) {
        return getCommonDao().getListFromMaster(bean);
    }

    @Override
    public <T, V> V getFromMaster(Class<T> clzss, Object... keyValuePair) {
        return getCommonDao().getFromMaster(clzss, keyValuePair);
    }

    @Override
    public <T, V> List<V> getListFromMaster(Class<T> clzss, Object paramsObj) {
        return getCommonDao().getListFromMaster(clzss, paramsObj);
    }

    @Override
    public <T, V> List<V> getListFromMaster(Class<T> clzss, Object... keyValuePair) {
        return getCommonDao().getListFromMaster(clzss, keyValuePair);
    }

    @Override
    public <T, V> V get(Class<T> clzss, Object... keyValuePair) {
        return getCommonDao().get(clzss, keyValuePair);
    }

    @Override
    public <T, V> List<V> getList(Class<T> clzss) {
        return getCommonDao().getList(clzss);
    }

    @Override
    public <T, V> V getBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair) {
        return getCommonDao().getBySqlId(clzss, sqlId, keyValuePair);
    }

    @Override
    public <T, V> V getMasterBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair) {
        return getCommonDao().getMasterBySqlId(clzss, sqlId, keyValuePair);
    }

    @Override
    public <PK, T> int delete(List<PK> ids, Class<T> clzss) {
        return getCommonDao().delete(ids, clzss);
    }

    @Override
    public <T> int delete(T bean) {
        return getCommonDao().delete(bean);
    }

    @Override
    public <T> int deleteBySqlId(String sqlId, T bean) {
        return getCommonDao().deleteBySqlId(sqlId, bean);
    }

    @Override
    public <T> int delete(Class<T> clzss, Object... params) {
        return getCommonDao().delete(clzss, params);
    }

    @Override
    public <T> int deleteBySqlId(Class<T> clzss, final String sqlId, Object... keyValuePair) {
        return getCommonDao().deleteBySqlId(clzss, sqlId, keyValuePair);
    }


    /**
     * 使用默认的查询id查询分页
     *
     * @param bean
     * @param query
     * @return
     */
    @Override
    public <T, V> PageFinder<V> getMasterPageFinder(T bean, Query query) {
        return getCommonDao().getMasterPageFinder(bean, query);
    }

    @Override
    public <V, T> PageFinder<V> getPageFinder(Class<T> clzss, Query query, String countSqlId, String dataSqlId, Object... param) {
        return getCommonDao().getPageFinder(clzss, query, countSqlId, dataSqlId, param);
    }

    @Override
    public <T> PageFinder<T> getPageFinder(T bean, Query query) {
        return getCommonDao().getPageFinder(bean, query);
    }

    @Override
    public <T> PageFinder<T> getPageFinder(T bean, Query query, Map param) {
        return getCommonDao().getPageFinder(bean, query, param);
    }

    @Override
    public <V, T> PageFinder<V> getPageFinder(T params, Query query,
                                              String countSql, String dataSql) {
        return getCommonDao().getPageFinder(params, query, countSql, dataSql);
    }

    @Override
    public <T> PageFinder<T> getPageFinder(Class<T> clzss, Query query, Map param) {
        return getCommonDao().getPageFinder(clzss, query, param);
    }

    @Override
    @Deprecated
    public <T> PageFinder<T> getPageFinder(Query query,
                                           String countSql, String dataSql, Object... params) {
        return (PageFinder<T>) getCommonDao().getPageFinder(query, countSql, dataSql, params);
    }


    @Override
    @Deprecated
    public <V> PageFinder<V> getPageFinderObjs(Object params, Query query, String countSql, String dataSql) {
        return getCommonDao().getPageFinderObjs(params, query, countSql, dataSql);
    }

    @Override
    @Deprecated
    public <V> PageFinder<V> getMasterPageFinderObjs(Object params, Query query, String countSql, String dataSql) {
        return getCommonDao().getMasterPageFinderObjs(params, query, countSql, dataSql);
    }

    @Override
    @Deprecated
    public <T> List<T> getList(String dataSql, Object params, Query query) {
        return getCommonDao().getList(dataSql, params, query);
    }

    @Override
    @Deprecated
    public <T> List<T> getMasterList(String dataSql, Object params, Query query) {
        return getCommonDao().getMasterList(dataSql, params, query);
    }

    @Override
    public <T> Integer batchInsert(final Integer batchSize, List<T> beanList) {
        if (null == beanList && 0 == beanList.size()) {
            return 0;
        }

        int count = 0;
        for (int startIndex = 0, endIndex = 0, size = beanList.size(); endIndex < size; startIndex = endIndex) {
            endIndex += batchSize;
            endIndex = endIndex > beanList.size() ? beanList.size() : endIndex;
            count += getCommonDao().save(beanList.subList(startIndex, endIndex));
        }
        return count;
    }

}
