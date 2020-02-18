package com.xinyue.framework.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 该类对list操作 附加类
 * 
 * @author xiangtao
 *
 */
public class SplitList {
	/**
	 * 分割数组
	 * 
	 * @param list
	 * @param pz
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<List<T>> splitList(List<T> list, int pz) {
		int lsz = list.size();
		int pg = lsz / pz;
		List<List<T>> lst = new ArrayList<List<T>>();
		for (int i = 0; i <= pg; i++) {
			List tempList = null;
			if (i == pg && lsz != i * pz) {
				tempList = list.subList(i * pz, lsz);
			} else {
				if ((i + 1) * pz <= lsz)
					tempList = list.subList(i * pz, (i + 1) * pz);
			}
			if (tempList != null) {
				lst.add(tempList);
			}
		}
		return lst;
	}

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 100; i++) {
			list.add(i + 1);
		}
		List<List<Integer>> lists = splitList(list, 9);
		System.out.println(lists);

	}

}
