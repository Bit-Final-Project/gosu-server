package com.ncp.moeego.main.controller.service;

import java.util.Map;

public interface SearchService {
    Map<String, Object> getSearchList(String value, int pg);
}
