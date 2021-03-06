package com.balloon.springboot.core.rules;

/**
 *
 * 分页实体类
 *
 * 每一个需要接收前端页面分页数据的Dto和需要返回分页给前端的VO都应该继承这个类
 *
 * @author liaofuxing
 * @date 2020/03/13 22:03
 */
public class PageInfo {

    /**
     * 当前页
     */
    private Integer page;

    /**
     * 一页多少条
     */
    private Integer pageSize;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
