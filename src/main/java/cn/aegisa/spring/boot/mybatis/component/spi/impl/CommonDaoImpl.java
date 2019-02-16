package cn.aegisa.spring.boot.mybatis.component.spi.impl;

import org.mybatis.spring.SqlSessionTemplate;

/**
 * @author XianYingda
 */
public class CommonDaoImpl extends DefaultDaoImpl {

    private SqlSessionTemplate sqlSession;

    private SqlSessionTemplate sqlSessionQurey;

    @Override
    public SqlSessionTemplate getSqlSessionQueryTemplate() {
        return sqlSessionQurey;
    }

    @Override
    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSession;
    }

    public  void setSqlSessionQurey(SqlSessionTemplate sqlSessionQurey){
        this.sqlSessionQurey = sqlSessionQurey;
    }

    public  void setSqlSession(SqlSessionTemplate sqlSession){
        this.sqlSession = sqlSession;
    }



}
