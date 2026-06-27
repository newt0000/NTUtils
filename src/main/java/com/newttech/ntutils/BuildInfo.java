package com.newttech.healthbars;

import java.io.InputStream;
import java.util.Properties;

public final class BuildInfo {

    public static final String VERSION;

    static {
        String v = "Unknown";

        try (InputStream in = BuildInfo.class.getClassLoader()
                .getResourceAsStream("version.properties")) {

            if (in != null) {
                Properties p = new Properties();
                p.load(in);

                int major = Integer.parseInt(p.getProperty("major", "1"));
                int minor = Integer.parseInt(p.getProperty("minor", "0"));
                int build = Integer.parseInt(p.getProperty("build", "0"));

                v = major + "." + String.format("%01d", minor)
                        + "." + String.format("%02d", build);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        VERSION = v;
    }

    private BuildInfo() {}
}