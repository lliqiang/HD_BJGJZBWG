package com.example.lenovo.hd_beijing_meseum.autono;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/4/20 09:38
 * 邮箱：tailyou@163.com
 * 描述：AutoNum工具-单例模式
 */
public class AutoNoUtil {

    private static volatile AutoNoUtil instance = null;

    private LinkedList<Integer> linkedAutoNoList = new LinkedList<>();
    private int listSize;
    private int listThreshold;


    public static AutoNoUtil getInstance(int listSize, int listThreshold) {
        if (instance == null) {
            synchronized (AutoNoUtil.class) {
                if (instance == null) {
                    instance = new AutoNoUtil(listSize, listThreshold);
                }
            }
        }
        return instance;
    }

    private AutoNoUtil(int listSize, int listThreshold) {
        this.listSize = listSize;
        this.listThreshold = listThreshold;
    }


    /**
     * 添加AutoNo
     *
     * @author 祝文飞（Tailyou）
     * @time 2016/11/3 11:36
     */
    public void addAutoNo(int autoNo) {
        if (linkedAutoNoList.size() == listSize)
            linkedAutoNoList.removeFirst();
        linkedAutoNoList.addLast(autoNo);
    }


    /**
     * 取质量最好的一个AutoNum
     *
     * @author 祝文飞（Tailyou）
     * @time 2016/11/3 11:39
     */
    public int getBestAutoNo() {
        int bestAutoNo = 0;
        if (linkedAutoNoList.size() > listThreshold) {
            HashMap<Integer, Integer> countMap = new HashMap<>();
            for (Integer autoNo : linkedAutoNoList) {
                int count = countMap.containsKey(autoNo) ? countMap.get(autoNo) : 0;
                count++;
                if (count == listThreshold) {
                    bestAutoNo = autoNo;
                    //linkedAutoNoList.clear();
                    break;
                } else {
                    countMap.put(autoNo, count);
                }
            }
        }
        return bestAutoNo;
    }

}
