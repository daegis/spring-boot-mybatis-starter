package com.guazi.spring.boot.mybatis.component.spi.impl;

import com.guazi.spring.boot.mybatis.component.jdbc.handler.RowBeanMapper;
import com.guazi.spring.boot.mybatis.component.spi.ICommonDao;
import com.guazi.spring.boot.mybatis.component.utils.Id;
import com.guazi.spring.boot.mybatis.component.utils.PageFinder;
import com.guazi.spring.boot.mybatis.component.utils.ParameterUtils;
import com.guazi.spring.boot.mybatis.component.utils.Query;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author XianYingda
 */
public abstract class DefaultDaoImpl implements ICommonDao {
    abstract SqlSessionTemplate getSqlSessionQueryTemplate();

    abstract SqlSessionTemplate getSqlSessionTemplate();

    @Override
    public <T, V> List<V> getListBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair) {
        return selectList(getMapperName(clzss) + sqlId, ParameterUtils.map(keyValuePair), false);
    }

    @Override
    public <T> Id getMinIdAndMaxId(Class<T> clzss, Object... keyValuePair) {
        Map<String, Integer> resultMap = getSqlSessionTemplate().selectOne(getMapperName(clzss) + SELECT_MINIDANDMAXID,
                ParameterUtils.map(keyValuePair));
        if (null == resultMap || null == resultMap.get("minId")) {
            return Id.empty();
        }
        Id id = new Id();
        id.setMinId(Integer.valueOf(String.valueOf(resultMap.get("minId"))));
        id.setMaxId(Integer.valueOf(String.valueOf(resultMap.get("maxId"))));
        return id;
    }

    @Override
    public <T, V> List<V> getDataList(Class<T> clzss, Object... keyValuePair) {
        Map map = ParameterUtils.map(keyValuePair);
        if (null == map || !map.containsKey("startIndex") || !map.containsKey("batchSize")) {
            throw new IllegalArgumentException("缺少必须参数startIndex和batchSize");
        }
        return selectList(getMapperName(clzss) + SELECT_DATALIST, ParameterUtils.map(keyValuePair), false);
    }

    @Override
    public <T, V> List<V> getListFromMasterBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair) {
        return selectList(getMapperName(clzss) + sqlId, ParameterUtils.map(keyValuePair), true);
    }

    @Override
    public <V> V executeTransactional(TransactionalVistor<V> vistor) {
        return vistor.executeTransactional();
    }

    @Override
    public <T> Integer updateBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair) {
        Map paramMap = keyValuePair.length == 1 ? ParameterUtils.convertBeanToMap(keyValuePair) : ParameterUtils.map(keyValuePair);
        if (paramMap == null || paramMap.size() == 0) {
            throw new IllegalArgumentException(getMapperName(clzss) + sqlId + "update 参数不能为空,没有参数可加上1=1");
        }
        return update(getMapperName(clzss) + sqlId, paramMap);
    }


    @Override
    public <T> Integer updateBySqlId(String sqlId, T paramBeam) {
        Map<String, Object> paramMap = ParameterUtils.convertBeanToMap(paramBeam);
        if (paramMap == null || paramMap.size() == 0) {
            throw new IllegalArgumentException(getMapperName(paramBeam.getClass()) + sqlId + "update 参数不能为空,没有参数可加上1=1");
        }
        return update(getMapperName(paramBeam.getClass()) + sqlId, paramMap);
    }

    @Override
    public <T> List<T> getListBySqlId(Class clazz, String sqlId, Object params, Query query) {
        Map<String, Object> paramMap = ParameterUtils.convertBeanToMap(params);
        return getSqlSessionQueryTemplate().selectList(getMapperName(clazz) + sqlId, paramMap, new RowBounds(query.getOffset(), query.getPageSize()));
    }

    @Override
    public <T> List<T> getMasterListBySqlId(Class clazz, String sqlId, Object params, Query query) {
        Map<String, Object> paramMap = ParameterUtils.convertBeanToMap(params);
        return getSqlSessionTemplate().selectList(getMapperName(clazz) + sqlId, paramMap, new RowBounds(query.getOffset(), query.getPageSize()));

    }

    /**
     * 从库查询
     *
     * @param statement
     * @return
     */

    public <T> T selectOne(String statement) {
        return this.getSqlSessionQueryTemplate().selectOne(statement);
    }

    /**
     * 从库查询
     *
     * @param <T> public <T> T selectOne (String statement, Object parameter) {
     *            return selectOne(statement, parameter,false);
     *            }
     */
    public <T> T selectOne(String statement, Object parameter, boolean master) {
        return this.getSqlSessionTemplate(master).selectOne(statement, parameter);
    }

    /**
     * 主库查询(查询及时信息)
     *
     * @param <T> public <T> T selectMasterOne (String statement, Object parameter) {
     *            return this.getSqlSessionTemplate().selectOne(statement, parameter);
     *            }
     */
    @Override
    public <T, V> V getMasterBySqlId(Class<T> clzss, String sqlId, Object... keyValueParams) {
        return selectOne(getMapperName(clzss) + sqlId, ParameterUtils.map(keyValueParams), true);
    }

    /**
     * 从库查询
     */
    public Map<?, ?> selectMap(String statement, String mapKey) {
        return this.getSqlSessionQueryTemplate().selectMap(statement, mapKey);
    }

    /**
     * 从库查询
     */
    public Map<?, ?> selectMap(String statement, Object parameter, String mapKey) {
        return this.getSqlSessionQueryTemplate().selectMap(statement, parameter, mapKey);
    }

    /**
     * 从库查询
     */
    public Map<?, ?> selectMap(String statement, Object parameter,
                               String mapKey, RowBounds rowBounds) {
        return this.getSqlSessionQueryTemplate().selectMap(statement, parameter, mapKey,
                rowBounds);
    }

    /**
     * 从库查询
     *
     * @param <T>
     */
    public <T> List<T> selectList(String statement) {
        return this.getSqlSessionQueryTemplate().selectList(statement);
    }

    private SqlSessionTemplate getSqlSessionTemplate(boolean master) {
        return (master ? this.getSqlSessionTemplate() : this.getSqlSessionQueryTemplate());
    }

    /**
     * 不返回NULL,返回空List
     *
     * @param statement
     * @param parameter
     * @param master    是否走主库
     * @param <T>
     * @return
     */
    public <T> List<T> selectList(String statement, Object parameter, boolean master) {
        List<T> resultList = getSqlSessionTemplate(master).selectList(statement, parameter);
        return null == resultList ? Collections.emptyList() : resultList;
    }

    /**
     * 从库查询
     *
     * @param <T>
     */

    public <T> List<T> selectList(String statement, Object parameter, RowBounds rowBounds) {
        List<T> resultList = this.getSqlSessionQueryTemplate().selectList(statement, parameter, rowBounds);
        return null == resultList ? Collections.emptyList() : resultList;
    }

    /**
     * 从库查询
     */
    public void select(String statement, ResultHandler handler) {
        this.getSqlSessionQueryTemplate().select(statement, handler);
    }

    /**
     * 从库查询
     */
    public void select(String statement, Object parameter, ResultHandler handler) {
        this.getSqlSessionQueryTemplate().select(statement, parameter, handler);
    }

    /**
     * 从库查询
     */
    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
        this.getSqlSessionQueryTemplate().select(statement, parameter, rowBounds, handler);
    }

    /**
     * 主库插入
     */

    public int insert(String statement) {
        return this.getSqlSessionTemplate().insert(statement);
    }

    /**
     * 主库插入
     */
    public int insert(String statement, Object parameter) {
        return this.getSqlSessionTemplate().insert(statement, parameter);
    }

    public <T> int insert(T target) {
        return insert(getMapperName(target.getClass()) + INSERT_SELECTIVE_ID, target);
    }

    public <T> int insert(List<T> target) {
        return insert(getMapperName(target.get(0).getClass()) + INSERT_BATCH_ID, target);
    }

    @Override
    public <T> int deleteBySqlId(String sqlId, T bean) {
        Map map = ParameterUtils.convertBeanToMap(bean);
        return delete(getMapperName(bean.getClass()) + sqlId, map);
    }


    /**
     * 主库更新
     */
    public int update(String statement) {
        return this.getSqlSessionTemplate().update(statement);
    }

    /**
     * 主库更新
     */
    public int update(String statement, Object parameter) {
        if (parameter != null) {
            return this.getSqlSessionTemplate().update(statement, parameter);
        }
        return 0;
    }

    /**
     * 主库删除
     */
    public int delete(String statement) {
        return this.getSqlSessionTemplate().delete(statement);
    }

    /**
     * all delete medthod with parameter deal here
     * 主库删除
     */
    public int delete(String statement, Object parameter) {
        if (parameter != null) {
            if (parameter instanceof Map) {
                if (((Map) parameter).size() == 0) {
                    throw new IllegalArgumentException(statement + " delete 参数不能为空,没有参数可以加上1=1");
                }
            }
            return this.getSqlSessionTemplate().delete(statement, parameter);
        }
        return 0;
    }

    /**
     * @param params
     * @return
     */
    public int deleteByParamsObjs(Object params) {
        return delete(getMapperName(params.getClass()) + DELETE_BY_MAP_ID, ParameterUtils.convertBeanToMap(params));
    }


    /**
     * 主库查询(查询及时信息)
     *
     * @param <T>
     */
    public <T> List<T> selectMasterList(String statement) {
        return this.getSqlSessionTemplate().selectList(statement);
    }


    /**
     * 查询分页（从库）
     *
     * @param <T>
     * @param params   参数对象
     * @param query    查询query
     * @param countSql 总记录数的查询sqlid 返回结果是int类型
     * @param dataSql  结果集的查询sqlid
     * @return
     */
    @Deprecated
    public <T> PageFinder<T> getPageFinderObjs(Object params, Query query, String countSql, String dataSql) {
        int count = getSqlSessionQueryTemplate().selectOne(countSql, params);
        List<T> datas = getSqlSessionQueryTemplate().selectList(dataSql, params, new RowBounds(query.getOffset(), query.getPageSize()));
        PageFinder<T> pageFinder = new PageFinder<T>(query.getPage(), query.getPageSize(), count, datas);
        return pageFinder;
    }


    /**
     * 查询分页（主库）
     *
     * @param <T>
     * @param params   参数对象
     * @param query    查询query
     * @param countSql 总记录数的查询sqlid 返回结果是int类型
     * @param dataSql  结果集的查询sqlid
     * @return
     */
    @Deprecated
    public <T> PageFinder<T> getMasterPageFinderObjs(Object params, Query query, String countSql, String dataSql) {
        int count = getSqlSessionTemplate().selectOne(countSql, params);
        List<T> datas = getSqlSessionTemplate().selectList(dataSql, params, new RowBounds(query.getOffset(), query.getPageSize()));
        PageFinder<T> pageFinder = null;
        if (count > 0) {
            pageFinder = new PageFinder<T>(query.getPage(), query.getPageSize(), count, datas);
        } else {
            pageFinder = new PageFinder<T>(1, query.getPageSize(), 0);
        }
        return pageFinder;
    }


    @Override
    public <PK, T, V> V get(PK key, Class<T> clzss) {
        return this.selectOne(getMapperName(clzss) + SELECT_BY_PRIMARYKEY_ID, key, false);
    }

    @Override
    public <T, V> V get(T bean) {
        return selectOne(getMapperName(bean.getClass()) + SELECT_GET_ALL_ID, ParameterUtils.convertBeanToMap(bean), false);
    }

    @Override
    public <T, V> List<V> getList(T bean) {
        return selectList(getMapperName(bean.getClass()) + SELECT_GET_ALL_ID, ParameterUtils.convertBeanToMap(bean), false);
    }

    @Override
    public <T, V> List<V> getList(Class<T> clzss, Object paramsObj) {
        return selectList(getMapperName(clzss) + SELECT_GET_ALL_ID, ParameterUtils.convertBeanToMap(paramsObj), false);
    }

    @Override
    public <T, V> List<V> getList(Class<T> clzss) {
        return selectList(getMapperName(clzss) + SELECT_GET_ALL_ID);
    }

    /**
     * @param dataSql
     * @param params
     * @param query
     * @param <T>
     * @return
     * @See getListBySqlId (Class clazz, String sqlId, Object params, Query query)
     */
    @Override
    @Deprecated
    public <T> List<T> getList(String dataSql, Object params, Query query) {
        Map<String, Object> paramMap = ParameterUtils.convertBeanToMap(params);
        return getSqlSessionQueryTemplate().selectList(dataSql, paramMap, new RowBounds(query.getOffset(), query.getPageSize()));
    }

    @Override
    @Deprecated
    public <T> List<T> getMasterList(String dataSql, Object params, Query query) {
        return getSqlSessionTemplate().selectList(dataSql, params, new RowBounds(query.getOffset(), query.getPageSize()));
    }


    @Override
    public <T, V> V get(Class<T> clzss, Object... keyValueParams) {
        return selectOne(getMapperName(clzss) + SELECT_GET_ALL_ID, ParameterUtils.map(keyValueParams), false);
    }

    @Override
    public <T, V> V getBySqlId(Class<T> clzss, String sqlId, Object... keyValueParams) {
        return selectOne(getMapperName(clzss) + sqlId, ParameterUtils.map(keyValueParams), false);
    }


    @Override
    public <PK, T> int delete(PK key, Class<T> clzss) {
        return delete(getMapperName(clzss) + DELETE_BY_PRIMARYKEY_ID, key);
    }


    @Override
    public <T> int delete(T bean) {
        Map<String, Object> paramMap = ParameterUtils.convertBeanToMap(bean);
        if (paramMap == null || paramMap.size() == 0) {
            throw new IllegalArgumentException("delete 参数不能为空");
        }
        return delete(getMapperName(bean.getClass()) + DELETE_BY_MAP_ID, paramMap);
    }


    @Override
    public <T> int delete(Class<T> clzss, Object... params) {
        Map paramMap = ParameterUtils.map(params);
        if (paramMap == null || paramMap.size() == 0) {
            throw new IllegalArgumentException("delete 参数不能为空");
        }
        return delete(getMapperName(clzss) + DELETE_BY_MAP_ID, paramMap);
    }


    @Override
    public <T> int deleteBySqlId(Class<T> clzss, final String sqlId, Object... params) {
        return delete(getMapperName(clzss) + sqlId, ParameterUtils.map(params));
    }


    @Override
    public <T> int save(T bean) {
        return insert(getMapperName(bean.getClass()) + INSERT_SELECTIVE_ID, bean);
    }


    @Override
    public <T> int save(List<T> beanList) {
        return insert(getMapperName(beanList.get(0).getClass()) + INSERT_BATCH_ID, beanList);
    }

    @Override
    public <T> int save(Class<T> clzss, String sqlId, Object... keyValueParams) {
        Map paramMap = ParameterUtils.map(keyValueParams);
        if (paramMap == null || paramMap.size() == 0) {
            throw new IllegalArgumentException(getMapperName(clzss) + sqlId + " save 参数不能为空");
        }
        return insert(getMapperName(clzss) + sqlId, paramMap);
    }


    @Override
    public <T> int update(T target) {
        return update(getMapperName(target.getClass()) + UPDATE_BY_PRIMARYKEY_ID, target);
    }

    @Override
    public <T> int update(List<T> beanList) {
        return update(getMapperName(beanList.get(0).getClass()) + UPDATE_BATCH, beanList);
    }

    @Override
    public <PK, T, V> V getFromMaster(PK key, Class<T> clzss) {
        return selectOne(getMapperName(clzss) + SELECT_BY_PRIMARYKEY_ID, key, true);
    }


    @Override
    public <T, V> V getFromMaster(T bean) {
        return selectOne(getMapperName(bean.getClass()) + SELECT_GET_ALL_ID, ParameterUtils.convertBeanToMap(bean), true);
    }


    @Override
    public <T, V> List<V> getListFromMaster(T bean) {
        return selectList(getMapperName(bean.getClass()) + SELECT_GET_ALL_ID, ParameterUtils.convertBeanToMap(bean), true);
    }


    @Override
    public <T, V> V getFromMaster(Class<T> clzss, Object... keyValueParams) {
        return selectOne(getMapperName(clzss) + SELECT_GET_ALL_ID, ParameterUtils.map(keyValueParams), true);
    }


    @Override
    public <T, V> List<V> getListFromMaster(Class<T> clzss, Object... keyValueParams) {
        return selectList(getMapperName(clzss) + SELECT_GET_ALL_ID, ParameterUtils.map(keyValueParams), true);
    }


    @Override
    public <T, V> List<V> getListFromMaster(Class<T> clzss, Object paramsObj) {
        return selectList(getMapperName(clzss) + SELECT_GET_ALL_ID, ParameterUtils.convertBeanToMap(paramsObj), true);
    }

    @Override
    public <PK, T, V> List<V> get(List<PK> key, Class<T> clzss) {
        return selectList(getMapperName(clzss) + SELECT_BY_IDS_ID, key, false);
    }


    @Override
    public <PK, T> List<T> getListByField(List<PK> key, String field, Class<T> clzss) {
        String fieldColumnName = RowBeanMapper.underscoreName(field);
        if (StringUtils.isEmpty(fieldColumnName)) {
            throw new IllegalArgumentException(" field not mapping to column ");
        }
        Map<String, Object> paramsObj = new HashMap();
        paramsObj.put("_fieldName", fieldColumnName);
        paramsObj.put("list", key);
        return selectList(getMapperName(clzss) + SELECT_BY_FIELD, paramsObj, false);

    }

    public <PK, T> List<T> getListByField(List<PK> key, Class<T> clzss) {
        return selectList(getMapperName(clzss) + SELECT_BY_FIELD, key, false);
    }

    @Override
    public <PK, T> int delete(List<PK> key, Class<T> clzss) {
        return delete(getMapperName(clzss) + DELETE_BY_IDS_ID, key);
    }


    @Override
    public <PK, T, V> List<V> getFromMaster(List<PK> key, Class<T> clzss) {
        return selectList(getMapperName(clzss) + SELECT_BY_IDS_ID, key, true);
    }


    @Override
    public <T, V> List<V> getByMap(Map<?, ?> map, Class<T> clzss) {
        return selectList(getMapperName(clzss) + SELECT_GET_ALL_ID, map, false);
    }


    @Override
    public <T, V> List<V> getList(Class<T> clzss, Object... params) {
        return selectList(getMapperName(clzss) + SELECT_GET_ALL_ID, ParameterUtils.map(params), false);
    }


    @Override
    public <T, V> PageFinder<V> getMasterPageFinder(T bean, Query query) {
        return getMasterPageFinderObjs(ParameterUtils.convertBeanToMap(bean), query, getMapperName(bean.getClass()) + SELECT_PAGECOUNT_ID, getMapperName(bean.getClass()) + SELECT_PAGEDATA_ID);

    }

    @Override
    public <T> PageFinder<T> getPageFinder(T bean, Query query) {
        Map params = ParameterUtils.convertBeanToMap(bean);
        return getPageFinderObjs(params, query, getMapperName(bean.getClass()) + SELECT_PAGECOUNT_ID, getMapperName(bean.getClass()) + SELECT_PAGEDATA_ID);
    }

    @Override
    public <T> PageFinder<T> getPageFinder(Class<T> clzss, Query query, Map param) {
        return getPageFinderObjs(param, query, getMapperName(clzss) + SELECT_PAGECOUNT_ID, getMapperName(clzss) + SELECT_PAGEDATA_ID);
    }

    @Override
    public <T> PageFinder<T> getPageFinder(T bean, Query query, Map param) {
        Map params = ParameterUtils.convertBeanToMap(bean);
        //添加另外的参数
        params.putAll(param);
        return getPageFinderObjs(params, query, getMapperName(bean.getClass()) + SELECT_PAGECOUNT_ID, getMapperName(bean.getClass()) + SELECT_PAGEDATA_ID);
    }

    @Override
    public <V, T> PageFinder<V> getPageFinder(T bean, Query query, String countSql, String dataSql) {
        Map params = ParameterUtils.convertBeanToMap(bean);
        if (countSql.indexOf(".") > 0) {
            //兼容老版本不规范的写法
            return getPageFinderObjs(params, query, countSql, dataSql);
        } else {
            return getPageFinderObjs(params, query, getMapperName(bean.getClass()) + countSql, getMapperName(bean.getClass()) + dataSql);

        }
    }

    @Override
    public <V, T> PageFinder<V> getPageFinder(Class<T> clzss, Query query, String countSqlId, String dataSqlId, Object... param) {
        Map params = ParameterUtils.map(param);
        return getPageFinderObjs(params, query, getMapperName(clzss) + countSqlId, getMapperName(clzss) + dataSqlId);
    }

    /**
     * 当 param为多种参数组合时,比较容易出错
     */
    @Override
    @Deprecated
    public <T> PageFinder<T> getPageFinder(Query query, String countSql, String dataSql, Object... param) {
        Map params = ParameterUtils.convertBeanToMap(param);
        return getPageFinderObjs(params, query, countSql, dataSql);
    }

}
