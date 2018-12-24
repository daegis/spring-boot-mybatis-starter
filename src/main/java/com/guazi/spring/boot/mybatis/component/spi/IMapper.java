package com.guazi.spring.boot.mybatis.component.spi;


import com.guazi.spring.boot.mybatis.component.utils.Id;
import com.guazi.spring.boot.mybatis.component.utils.PageFinder;
import com.guazi.spring.boot.mybatis.component.utils.Query;

import java.util.List;
import java.util.Map;

public interface IMapper {

    /**
     * Mapper 文件命名规则,BeanClasName+Mapper
     */
    String Mapper = "Mapper";
    /**
     * 查询所有数据的 select id
     **/
    String SELECT_GET_ALL_ID = "getAll";
    /**
     * 通过主键查询sql id
     **/
    String SELECT_BY_PRIMARYKEY_ID = "selectByPrimaryKey";
    /**
     * 通过主键删除sql id
     **/
    String DELETE_BY_PRIMARYKEY_ID = "deleteByPrimaryKey";
    /**
     * 通过主键更新 update id
     **/
    String UPDATE_BY_PRIMARYKEY_ID = "updateByPrimaryKeySelective";

    //下面定义的常量为Mapper中常用到 sqlId,必须与*Mapper.xml  sqlId对应才可以使用
    /**
     * 选择性插入 insert id
     **/
    String INSERT_SELECTIVE_ID = "insertSelective";
    /**
     * 批量插入 insert id
     **/
    String INSERT_BATCH_ID = "insertBatch";
    /**
     * 根据Map 参数删除 delete id
     **/
    String DELETE_BY_MAP_ID = "deleteByMap";
    /**
     * 获取总条数
     **/
    String SELECT_PAGECOUNT_ID = "pageCount";

    String SELECT_PAGEDATA_ID = "pageData";

    String SELECT_BY_IDS_ID = "selectByIds";

    String SELECT_BY_FIELD = "selectByField";

    String DELETE_BY_IDS_ID = "deleteByIds";

    String UPDATE_BATCH = "updateBatch";

    String SELECT_MINIDANDMAXID = "getMinIdAndMaxId";

    String SELECT_DATALIST = "getDataList";

    <V> V executeTransactional(TransactionalVistor<V> vistor);

    /**
     * 返回class(T类型)对应的Mapper文件名称
     *
     * @param clzss
     * @return
     */
    default <T> String getMapperName(Class<T> clzss) {
        return clzss.getSimpleName().concat(Mapper).concat(".");
    }


    /*************************************常用方法************************************************/
    /**
     * @param sqlId
     * @param paramBeam
     * @param <T>
     * @return
     */

    <T> Integer updateBySqlId(String sqlId, T paramBeam);

    /**
     * 根据参数执行指定的sql,返回结果,注意参数值的类型,如果跟数据库对不上，查询可能会出错(或者影响效率,SQL自动转型)
     *
     * @param clzss        对应有Model(Bean)
     * @param sqlId        Mybaits 中sqlId
     * @param keyValuePair (bean) fieldName - fieldValue
     * @return
     */
    <T> Integer updateBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair);

    /**
     * 查询符合条件的表的主键的最小值和最大值,默认SELECT_MINIDANDMAXID
     *
     * @param clzss
     * @param keyValuePair 业务参数
     * @param <T>
     * @return
     */
    <T> Id getMinIdAndMaxId(Class<T> clzss, Object... keyValuePair);

    /**
     * 每次取完数据后 startIndex = maxId(dataList)(最大的主键id)
     *
     * @param clzss
     * @param keyValuePair 业务参数,必须包含参数pageIndext和batchSize
     * @param <T>
     * @param <V>
     * @return
     */
    <T, V> List<V> getDataList(Class<T> clzss, Object... keyValuePair);


    /**
     * 根据bean 参数更新bean 对应数据表符合条件记录
     *
     * @param bean
     * @return
     */
    <T> int update(T bean);

    /**
     * 批量更新
     *
     * @param beanList
     * @param <T>
     * @return
     */
    <T> int update(List<T> beanList);

    /**
     * PK 主键
     * 根据主键查询对应的实体Bean
     *
     * @param key
     * @param clzss 实体Bean Class
     * @return
     */
    <PK, T, V> V get(PK key, Class<T> clzss);

    /**
     * 从库操作
     **/
    <PK, T, V> List<V> get(List<PK> key, Class<T> clzss);

    <PK, T> List<T> getListByField(List<PK> key, String field, Class<T> clzss);

    /**
     * 根据Bean 实体参数,返回唯一Bean
     *
     * @param bean 具体Bean 对象
     * @return
     */
    <T, V> V get(T bean);

    /**
     * 通过map参数获取对应的 clzss 实体列表
     *
     * @param map
     * @param clzss
     * @return
     */
    <T, V> List<V> getByMap(Map<?, ?> map, Class<T> clzss);

    /**
     * 根据Bean 实体参数,返回Bean List
     *
     * @param bean 具体Bean 对象
     * @return
     */
    <T, V> List<V> getList(T bean);

    <T, V> List<V> getList(Class<T> clzss, Object paramsObj);

    <T, V> List<V> getList(Class<T> clzss);

    <T, V> List<V> getListBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair);

    <T, V> List<V> getListFromMasterBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair);

    /**
     * 根据 key-value 实体参数,返回唯一Bean
     *
     * @param
     * @return
     */
    <T, V> V get(Class<T> clzss, Object... keyValueParams);

    /**
     * 不暴露Mapper生成规则
     *
     * @param dataSql
     * @param params
     * @param query
     * @param <T>
     * @return
     * @see IMapper#getListBySqlId(Class, String, Object, Query)
     */
    @Deprecated
    <T> List<T> getList(String dataSql, Object params, Query query);

    <T> List<T> getListBySqlId(Class clazz, String sqlId, Object params, Query query);

    /**
     * 根据sqlId和参数的参数查询结果
     *
     * @param clzss
     * @param sqlId        SQL语句标识
     * @param keyValuePair 参数对
     * @return V 结果集
     */
    <T, V> V getBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair);

    /**
     * 根据 key-value 参数对,返回Bean List
     *
     * @return
     */

    <T, V> List<V> getList(Class<T> clzss, Object... keyValuePair);

    /**
     * 不暴露Mapper生成规则
     *
     * @param dataSql
     * @param params
     * @param query
     * @param <T>
     * @return
     * @see IMapper#getMasterListBySqlId(Class, String, Object, Query)
     */
    @Deprecated
    <T> List<T> getMasterList(String dataSql, Object params, Query query);

    <T> List<T> getMasterListBySqlId(Class clazz, String sqlId, Object params, Query query);

    <T, V> V getMasterBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair);

    /**
     * 主库操作
     **/
    <PK, T, V> V getFromMaster(final PK key, Class<T> clzss);

    <PK, T, V> List<V> getFromMaster(final List<PK> key, Class<T> clzss);

    /**
     * 根据Bean 实体参数,返回唯一Bean
     *
     * @param bean 具体Bean 对象
     * @return
     */
    <T, V> V getFromMaster(T bean);

    /**
     * 根据Bean 实体参数,返回Bean List
     *
     * @param bean 具体Bean 对象
     * @return
     */
    <T, V> List<V> getListFromMaster(T bean);

    /**
     * 根据 key-value 实体参数,返回唯一Bean
     *
     * @return
     */
    <T, V> V getFromMaster(Class<T> clzss, Object... keyValuePair);

    /**
     * 根据 key-value 参数对,返回Bean List
     *
     * @return
     */
    <T, V> List<V> getListFromMaster(Class<T> clzss, Object... keyValuePair);

    <T, V> List<V> getListFromMaster(Class<T> clzss, Object paramsObj);

    /**
     * 删除 clzss 对应表中 主键为 key的记录
     *
     * @param key
     * @param clzss
     * @return
     */
    <PK, T> int delete(final PK key, Class<T> clzss);

    <PK, T> int delete(final List<PK> key, Class<T> clzss);

    /**
     * 根据bean 参数删除bean对应表的符合条件的记录
     *
     * @param bean
     * @return
     */
    <T> int delete(T bean);

    /**
     * 根据 key-value 参数 删除Bean对应数据表符合条件的记录
     *
     * @param clzss
     * @param keyValuePair
     * @return
     */
    <T> int delete(Class<T> clzss, Object... keyValuePair);

    <T> int deleteBySqlId(Class<T> clzss, String sqlId, Object... keyValuePair);

    <T> int deleteBySqlId(String sqlId, T bean);

    <T> int save(T bean);

    <T> int save(List<T> beanList);

    /**
     * 通过指定SqlId 插入数据
     *
     * @param clzss
     * @param sqlId
     * @param keyValuePair
     * @return
     */
    <T> int save(Class<T> clzss, String sqlId, Object... keyValuePair);


    <T, V> PageFinder<V> getMasterPageFinder(T bean, Query query);

    /**
     * @param bean  model条件设置
     * @param query
     * @param <T>
     * @return
     */
    <T> PageFinder<T> getPageFinder(T bean, Query query);

    <T> PageFinder<T> getPageFinder(Class<T> clzss, Query query, Map param);

    /**
     * @param bean  bean本身查询条件
     * @param query 分页查询条件
     * @param param 另外的不在 bean中其它查询条件
     * @param <T>
     * @return
     */
    <T> PageFinder<T> getPageFinder(T bean, Query query, Map param);

    /**
     * 查询分页（从库）
     * 不应该暴露Mapper中sqlId具体名称
     *
     * @param bean     参数对象
     * @param query    查询query
     * @param countSql 总记录数的查询sqlId 返回结果是int类型
     * @param dataSql  结果集的查询sqlId
     * @return
     */
    <V, T> PageFinder<V> getPageFinder(T bean, Query query, String countSql, String dataSql);


    /**
     * @param clzss
     * @param query
     * @param countSqlId
     * @param dataSqlId
     * @param param
     * @param <V>
     * @param <T>
     * @return
     * @Since 2017-11-06
     */
    <V, T> PageFinder<V> getPageFinder(Class<T> clzss, Query query, String countSqlId, String dataSqlId, Object... param);

    @Deprecated
    <T> PageFinder<T> getPageFinder(Query query, String countSql, String dataSql, Object... keyValuePair);

    /**
     * 查询分页（从库）
     *
     * @param <V>
     * @param params   参数对象
     * @param query    查询query
     * @param countSql 总记录数的查询sqlid 返回结果是int类型
     * @param dataSql  结果集的查询sqlid
     * @return
     */
    @Deprecated
    <V> PageFinder<V> getPageFinderObjs(Object params, Query query, String countSql, String dataSql);

    /**
     * 查询分页（主库）
     *
     * @param <V>
     * @param params   参数对象
     * @param query    查询query
     * @param countSql 总记录数的查询sqlid 返回结果是int类型
     * @param dataSql  结果集的查询sqlid
     * @return
     */
    @Deprecated
    <V> PageFinder<V> getMasterPageFinderObjs(Object params, Query query, String countSql, String dataSql);

    /**
     * 支持事务接口
     *
     * @author lvxiang
     * @date 2015年6月27日 下午4:30:34
     */
    interface TransactionalVistor<V> {
        V executeTransactional();
    }
}
