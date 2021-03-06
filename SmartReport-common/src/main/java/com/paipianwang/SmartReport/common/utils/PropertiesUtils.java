package com.paipianwang.SmartReport.common.utils;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesUtils {
    private static Properties props = new Properties();

    public PropertiesUtils() {
    }

    public static void configure(String configFilename) {
        try {
            FileInputStream fs = new FileInputStream(configFilename);
            props.load(fs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }

    public static void updateProperties(String key, String value) {
        props.setProperty(key, value);
    }
}
