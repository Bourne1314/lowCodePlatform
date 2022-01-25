package com.csicit.ace.common.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 */
@Data
public class PageUtils implements Serializable {
	private static final long serialVersionUID = 1L;
	private int total;
	private List<?> rows;


	/**
	 * 按当前记录位置翻页
	 *
	 * @param offset 当前数据位置
	 * @param limit
	 * @param list
	 * @param total
	 */
	public PageUtils(int offset,int limit,List<?> list, int total) {
		int limits = offset+limit>total?total:offset+limit;
		List<Object> pageList = new ArrayList<>();
		for(int i = offset;i<limits;i++){
			pageList.add(list.get(i));
		}
		this.rows = pageList;
		this.total = total;
	}

	/**
	 * 按页面数翻页
	 *
	 * @param page 当前页面数
	 * @param limit
	 * @param list
	 */
	public PageUtils(int page,int limit,List<?> list) {
		int start = (page-1)*limit;
		List<Object> pageList = new ArrayList<>();
		for(int i=start;i<start+limit;i++){
			pageList.add(list.get(i));
		}
		this.rows = pageList;
		this.total = list.size();
	}


}
