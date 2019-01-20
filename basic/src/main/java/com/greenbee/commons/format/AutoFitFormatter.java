package com.greenbee.commons.format;

import com.greenbee.commons.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoFitFormatter {
    protected int size_;
    protected List<Integer> widths_;

    protected List<String> headers_;
    protected List<List<? extends Object>> rows_ = new ArrayList<List<? extends Object>>();

    protected Map<Integer, Boolean> right_ = new HashMap<Integer, Boolean>();

    public AutoFitFormatter(int size) {
        size_ = size;
        widths_ = new ArrayList<Integer>(size);
        for (int i = 0; i < size; i++) {
            widths_.add(i, 0);
        }
    }

    public AutoFitFormatter(List<Integer> minWidth) {
        size_ = minWidth.size();
        widths_ = new ArrayList<Integer>(minWidth);
    }

    public static void main(String[] args) {
        //List<String> format = Arrays.asList("@s", "@s", "-@s", "@d");
        //List<Integer> min = Arrays.asList(5, 5, 2, 1);
        AutoFitFormatter fm = new AutoFitFormatter(4).setAlignRight(2);

        fm.addHeader(Arrays.asList("Feed", "Subscriber", "Durable", "Size"));
        fm.addRow(Arrays.asList((Object) "/home/vtbaadmin/EPN", "process_abc_ID10", "+", 1000));
        System.out.println(fm);
    }

    public AutoFitFormatter setAlignRight(int index) {
        right_.put(index, true);
        return this;
    }

    public void addHeader(List<String> headers) {
        if (size_ != headers.size())
            throw new IllegalArgumentException("Size of headers should be equal with format");
        headers_ = headers;
    }

    public void addRow(List<? extends Object> row) {
        if (size_ != row.size())
            throw new IllegalArgumentException("Size of row should be equal with format");
        rows_.add(row);
    }

    protected void getAutoFitWidth() {
        for (int i = 0; i < size_; i++) {
            String column = headers_.get(i);
            int width = column.length();
            if (widths_.get(i) < width)
                widths_.set(i, width);
        }
        for (List<? extends Object> columns : rows_) {
            for (int i = 0; i < size_; i++) {
                String column = columns.get(i).toString();
                int width = column.length();
                if (widths_.get(i) < width)
                    widths_.set(i, width);
            }
        }
    }

    protected void printColumnSpace(StringBuilder sb) {
        sb.append("  ");
    }

    //%1$60s %2$60s %3$50s %4$40s
    protected String getHeaderFormat() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size_; i++) {
            if (i != 0)
                printColumnSpace(sb);
            String align = Boolean.TRUE.equals(right_.get(i)) ? "" : "-";
            sb.append("%").append(i + 1).append("$").append(align).append(widths_.get(i))
                    .append("s");
        }
        return sb.toString();
    }

    protected String getRowFormat() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size_; i++) {
            if (i != 0)
                printColumnSpace(sb);
            String align = Boolean.TRUE.equals(right_.get(i)) ? "" : "-";
            sb.append("%").append(i + 1).append("$").append(align).append(widths_.get(i))
                    .append("s");
        }
        return sb.toString();
    }

    public void format(StringBuilder sb) {
        getAutoFitWidth();
        String headerForamt = getHeaderFormat();
        String rowFormat = getRowFormat();
        String content = String.format(headerForamt, headers_.toArray());
        sb.append(content);
        for (List<? extends Object> row : rows_) {
            sb.append(StringUtils.getLineSeperator());
            content = String.format(rowFormat, row.toArray());
            sb.append(content);
        }
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        format(sb);
        return sb.toString();
    }
}
