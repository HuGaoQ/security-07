package com.ncamc.entity;

import lombok.Data;

import java.util.List;

@Data
public class Pages<T> {

    private Long pageCount;//总页数
    private Long count;//总条数
    private List<T> data;//分页数据

    public Pages(Long pageCount, Long count, List<T> data) {
        this.pageCount = pageCount;
        this.count = count;
        this.data = data;
    }
}