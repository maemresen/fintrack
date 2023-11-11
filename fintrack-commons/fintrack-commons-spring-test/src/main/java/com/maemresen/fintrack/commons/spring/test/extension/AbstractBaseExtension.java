package com.maemresen.fintrack.commons.spring.test.extension;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.extension.ExtensionContext;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseExtension {

    private static String getKey(ExtensionContext extensionContext, String key) {
        return String.format("%s_%s", extensionContext.getRequiredTestClass().getName(), key);
    }

    protected static <V> V get(ExtensionContext extensionContext, String key, Class<V> valueClass) {
        var actualKey = getKey(extensionContext, key);
        return extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).get(actualKey, valueClass);
    }

    protected static void store(ExtensionContext extensionContext, String key, Object value) {
        var actualKey = getKey(extensionContext, key);
        extensionContext.getStore(ExtensionContext.Namespace.GLOBAL).put(actualKey, value);
    }

}
