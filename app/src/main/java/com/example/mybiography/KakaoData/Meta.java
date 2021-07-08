package com.example.mybiography.KakaoData;

public class Meta {
    private RegionInfo same_name;
    private int pageable_count;
    private int total_count;
    private boolean is_end;

    public RegionInfo getSame_name() {
        return same_name;
    }

    public void setSame_name(RegionInfo same_name) {
        this.same_name = same_name;
    }

    public int getPageable_count() {
        return pageable_count;
    }

    public void setPageable_count(int pageable_count) {
        this.pageable_count = pageable_count;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public boolean isIs_end() {
        return is_end;
    }

    public void setIs_end(boolean is_end) {
        this.is_end = is_end;
    }

//    @Override
//    public String toString() {
//        return "Meta{" +
//                "same_name=" + same_name +
//                ", pageable_count=" + pageable_count +
//                ", total_count=" + total_count +
//                ", is_end=" + is_end +
//                '}';
//    }
}
