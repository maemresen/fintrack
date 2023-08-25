package com.maemresen.fintrack.api.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class ApiUriUtil {
    public static String mergeUri(String baseUri, String uri) {
        return "/" + Stream.of(baseUri, uri)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.joining("/"));
    }
}
