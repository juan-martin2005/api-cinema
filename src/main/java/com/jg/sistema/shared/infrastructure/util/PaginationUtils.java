package com.jg.sistema.shared.infrastructure.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Utility class to perform in-memory pagination on Java collections (e.g. List).
 * Useful when working with cached data or pre-filtered lists that need to be
 * converted into Spring Data's Page<T>.
 */
public final class PaginationUtils {

    private PaginationUtils() {
        // Prevent instantiation
    }

    /**
     * Paginates a given list based on the provided Pageable object.
     *
     * @param list     the list to paginate
     * @param pageable the pagination information (page number, page size, etc.)
     * @param <T>      the type of elements in the list
     * @return a Page containing the sublist for the requested page
     */
    public static <T> Page<T> paginate(List<T> list, Pageable pageable) {
        if (list == null || list.isEmpty()) {
            return Page.empty(pageable != null ? pageable : Pageable.unpaged());
        }

        if (pageable == null || pageable.isUnpaged()) {
            return new PageImpl<>(list, Pageable.unpaged(), list.size());
        }

        int start = (int) pageable.getOffset();
        if (start >= list.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, list.size());
        }

        int end = Math.min(start + pageable.getPageSize(), list.size());
        List<T> subList = list.subList(start, end);

        return new PageImpl<>(subList, pageable, list.size());
    }

    /**
     * Paginates a given list based on page number and page size.
     *
     * @param list     the list to paginate
     * @param page     zero-based page index
     * @param size     the size of the page to be returned
     * @param <T>      the type of elements in the list
     * @return a Page containing the sublist for the requested page
     */
    public static <T> Page<T> paginate(List<T> list, int page, int size) {
        return paginate(list, PageRequest.of(page, size));
    }

    /**
     * Paginates a given list and maps its elements using the provided mapper function.
     *
     * @param list     the list to paginate
     * @param pageable the pagination information
     * @param mapper   the mapping function to apply to each element
     * @param <T>      the source element type
     * @param <R>      the target element type
     * @return a Page containing the mapped elements
     */
    public static <T, R> Page<R> paginate(List<T> list, Pageable pageable, Function<T, R> mapper) {
        return paginate(list, pageable).map(mapper);
    }
}
