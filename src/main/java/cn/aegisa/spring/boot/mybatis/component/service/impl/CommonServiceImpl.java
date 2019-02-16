package cn.aegisa.spring.boot.mybatis.component.service.impl;


import cn.aegisa.spring.boot.mybatis.component.spi.ICommonDao;

/**
 * @author XianYingda
 */
public class CommonServiceImpl extends DefaultServiceImpl {

    private ICommonDao commonDao;

    @Override
    public ICommonDao getCommonDao() {
        return commonDao;
    }

    @Override
    public void setCommonDao(ICommonDao commonDao) {
        this.commonDao = commonDao;
    }


}
