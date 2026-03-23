package com.aladdin.core_service.util.trie;

import com.aladdin.core_service.dto.HouseSearchResult;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Primary
public class TrieAutocompleteService implements AutocompleteService<HouseSearchResult, HouseSearchResult> {

    private final TrieNode root = new TrieNode();

    private final Map<String, HouseSearchResult> houseMap = new HashMap<>();

    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        Set<String> aptSeqs = new HashSet<>();
    }

    @Override
    public void initializeData(String keyword, HouseSearchResult data) {
        String seq = String.valueOf(data.getAptSeq());
        houseMap.putIfAbsent(seq, data);

        int n = keyword.length();
        for (int i = 0; i < n; i++) {
            TrieNode current = root;
            for (int j = i; j < n; j++) {
                char ch = keyword.charAt(j);
                current = current.children.computeIfAbsent(ch, c -> new TrieNode());
                current.aptSeqs.add(seq);
            }
        }
    }

    @Override
    public List<HouseSearchResult> search(String keyword) {
        TrieNode current = root;
        for (char ch : keyword.toCharArray()) {
            current = current.children.get(ch);
            if (current == null) return Collections.emptyList();
        }

        return current.aptSeqs.stream()
                .map(houseMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public void initializeDataBulk(List<HouseSearchResult> dataList) {
        for (HouseSearchResult house : dataList) {
            initializeData(house.getAptName(), house);
        }
    }
}