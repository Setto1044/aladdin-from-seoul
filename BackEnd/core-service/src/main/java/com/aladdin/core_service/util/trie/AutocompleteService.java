package com.aladdin.core_service.util.trie;

import com.aladdin.core_service.dto.HouseSearchResultDto;

import java.util.List;

public interface AutocompleteService<D, R> {
    void initializeData(String keyword, D data);
    List<R> search(String keyword);
    void initializeDataBulk(List<HouseSearchResultDto> dataList);
}
