package com.yimilan.kedaibiao.dao.api;

import com.yimilan.kedaibiao.domain.entity.api.Banner;

public interface BannerDao {
    int deleteByPrimaryKey(Long id);

    int insert(Banner record);

    int insertSelective(Banner record);

    Banner selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Banner record);

    int updateByPrimaryKey(Banner record);
}