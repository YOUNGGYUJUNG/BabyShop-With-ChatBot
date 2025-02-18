package com.project.apple.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@Slf4j
public class CustomPageRequest extends PageRequest {

    private final int PAGE_INIT_SIZE = -1;
    private final int PAGENUMBER_INIT_SIZE = 1;
    private final int PAGESIZE_INIT_SIZE = 10;
    private final List<String> SORT_VALUES = List.of("id", "createdAt");

    @Getter
    private String sortName;
    @Getter
    private Integer page;
    @Getter
    private Integer size;

    protected CustomPageRequest(Integer page, Integer size, String sortName) {
        super(0, 10, Sort.unsorted());

        this.page = (page==null || page < PAGENUMBER_INIT_SIZE) ? PAGENUMBER_INIT_SIZE : page;
        this.size = (size==null || size < PAGESIZE_INIT_SIZE) ? PAGESIZE_INIT_SIZE : size;
        this.sortName = (sortName == null) ? "id" : sortName;
    }

    @JsonIgnore
    public PageRequest getPageRequest() {
        if (!SORT_VALUES.contains(sortName)) this.sortName = "id";

        return withSort(Sort.by(this.sortName).descending());
    }

    @JsonIgnore
    @Override
    public PageRequest withSort(Sort sort) {
        return PageRequest.of(page + PAGE_INIT_SIZE, size, sort);
    }


    @JsonIgnore
    @Override
    public Sort getSort() {
        return super.getSort();
    }

    @JsonIgnore
    @Override
    public long getOffset() {
        return super.getOffset();
    }

    @JsonIgnore
    @Override
    public boolean isPaged() {
        return super.isPaged();
    }

    @JsonIgnore
    @Override
    public boolean isUnpaged() {
        return super.isUnpaged();
    }

    @JsonIgnore
    @Override
    public int getPageNumber() {
        return super.getPageNumber();
    }

    @JsonIgnore
    @Override
    public int getPageSize() {
        return super.getPageSize();
    }
}
