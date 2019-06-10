package com.example.user.music.lib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MyList
 */
public class MyList<T> {

    private List<T> list;

    private int total;

    private int pageNum;

    private int pageSize;

    private Boolean hasNextPage;

    public MyList(List<T> list, int total, int pageNum, int pageSize) {
        this.list = list;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.hasNextPage = total > pageNum * pageSize;
    }
    /**
     * @return the list
     */
    public List<T> getList() {
        return list;
    }

    /**
     * @return the pageNum
     */
    public int getPageNum() {
        return pageNum;
    }

    /**
     * @param pageNum the pageNum to set
     */
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the hasNextPage
     */
    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    /**
     * @param hasNextPage the hasNextPage to set
     */
    public void setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    /**
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<T> list) {
        this.list = list;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("list", this.list);
        map.put("total", this.total);
        map.put("hasNextPage", this.hasNextPage);
        map.put("pageNum", this.pageNum);
        map.put("pageSize", this.pageSize);
        return map;
    }
}
