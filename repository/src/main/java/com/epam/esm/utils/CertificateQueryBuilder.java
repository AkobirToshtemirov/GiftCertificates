package com.epam.esm.utils;

import java.util.ArrayList;
import java.util.List;

public class CertificateQueryBuilder {

    public static final String BASE_QUERY = "SELECT gc.*, t.* FROM gift_certificates gc " +
            "LEFT JOIN gift_certificate_tags gct ON gc.id = gct.gift_certificate_id " +
            "LEFT JOIN tags t ON gct.tag_id = t.id WHERE 1=1";
    public static final String TAG_NAME_CRITERIA = " AND t.name = ?";
    public static final String SEARCH_CRITERIA = " AND (gc.name LIKE ? OR gc.description LIKE ?)";
    public static final String SORTING = " ORDER BY ";
    public static final String ASCENDING = " ASC";
    public static final String DESCENDING = " DESC";
    public static final String PERCENT_SIGN = "%";

    private final StringBuilder queryBuilder;
    private final List<Object> queryParams;

    public CertificateQueryBuilder() {
        this.queryBuilder = new StringBuilder(BASE_QUERY);
        this.queryParams = new ArrayList<>();
    }

    public String build() {
        return queryBuilder.toString();
    }

    public Object[] getQueryParams() {
        return queryParams.toArray();
    }

    public void addTagCriteria(String tagName) {
        if (tagName != null && !tagName.isEmpty()) {
            queryBuilder.append(TAG_NAME_CRITERIA);
            queryParams.add(tagName);
        }
    }

    public void addSearchCriteria(String search) {
        if (search != null && !search.isEmpty()) {
            queryBuilder.append(SEARCH_CRITERIA);
            queryParams.add(PERCENT_SIGN + search + PERCENT_SIGN);
            queryParams.add(PERCENT_SIGN + search + PERCENT_SIGN);
        }
    }

    public void addSortingCriteria(String sortBy, boolean ascending) {
        if (sortBy != null && !sortBy.isEmpty()) {
            String sortField = sortBy.equalsIgnoreCase("name") ? "gc.name" : "gc.created_date";
            queryBuilder.append(SORTING).append(sortField).append(ascending ? ASCENDING : DESCENDING);
        }
    }
}