package com.ncp.moeego.main.controller.service;

import java.util.Map;

public interface SearchService {
    Map<String, Object> getSearchProList(String value, int pg);
    Map<String, Object> getSearchArticleList(String value, int pg);
}
