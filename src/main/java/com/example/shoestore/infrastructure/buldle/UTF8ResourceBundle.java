package com.example.shoestore.infrastructure.buldle;

import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class UTF8ResourceBundle extends ResourceBundle {
    private static final String BASE_NAME = "i18n/messages";

    public UTF8ResourceBundle() {
        // Lấy Locale từ LocaleContextHolder
        Locale locale = LocaleContextHolder.getLocale();
        setParent(ResourceBundle.getBundle(BASE_NAME, locale, new UTF8Control()));
    }

    @Override
    protected Object handleGetObject(String key) {
        return parent.getObject(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        return parent.getKeys();
    }

    private class UTF8Control extends ResourceBundle.Control {
        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {

            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            ResourceBundle bundle = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        try (InputStream stream = connection.getInputStream();
                             Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                            bundle = new PropertyResourceBundle(reader);
                        }
                    }
                }
            } else {
                try (InputStream stream = loader.getResourceAsStream(resourceName)) {
                    if (stream != null) {
                        try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                            bundle = new PropertyResourceBundle(reader);
                        }
                    }
                }
            }
            return bundle;
        }
    }
}
