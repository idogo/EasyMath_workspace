package com.easymath.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by igoodis on 02/07/2017.
 */

public class PropertiesUtil {

    private static Properties properties;
    private static Properties studProperties;
    private static Properties teacherProperties;

    private static final String EXIT_PROP = "general.exit";
    private static final String OK_PROP = "general.ok";
    private static final String HELP = "help.msg";

    public static void initProperties(Context context) throws IOException {
        properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("messages.properties");
        properties.load(inputStream);

        studProperties = new Properties();
        assetManager = context.getAssets();
        inputStream = assetManager.open("helpstudents.properties");
        studProperties.load(inputStream);

        teacherProperties = new Properties();
        assetManager = context.getAssets();
        inputStream = assetManager.open("helpteachers.properties");
        teacherProperties.load(inputStream);
    }

    public static String getProperty(String key) throws IOException {
        return properties.getProperty(key);
    }

    public static String getExitMessage() throws IOException {
        return getProperty(EXIT_PROP);
    }

    public static String getOkMessage() throws IOException {
        return getProperty(OK_PROP);
    }

    public static String getStudentHelp() throws IOException {
        return studProperties.getProperty(HELP);
    }

    public static String getTeacherHelp() throws IOException {
        return teacherProperties.getProperty(HELP);
    }
}
