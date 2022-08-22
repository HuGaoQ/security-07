package com.ncamc.entity;

import lombok.Data;

import java.util.List;

@Data
public class Pages<T> {

    /**
     * 总页数
     */
    private Long pageCount;

    /**
     * 总条数
     */
    private Long count;

    /**
     * 分页数据
     */
    private List<T> data;

    public Pages(Long pageCount, Long count, List<T> data) {
        this.pageCount = pageCount;
        this.count = count;
        this.data = data;
    }
}