package com.comunired.quejas.application.dto;

import java.util.List;

public class QuejasPageDTO {
    private List<QuejasDTO> content;
    private long totalElements;
    private int totalPages;
    private int number;
    private int size;
    private boolean last;    

    public List<QuejasDTO> getContent() { return content; }
    public void setContent(List<QuejasDTO> content) { this.content = content; }

    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public boolean isLast() { return last; }
    public void setLast(boolean last) { this.last = last; }
}