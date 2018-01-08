package com.example.demo.utils;

public class OSInfoUtils {
    private static final String OS = System.getProperty("os.name").toLowerCase();

    private OSInfoUtils() {
    }

    public static boolean isLinux() {
        return OS.contains("linux");
    }

    public static boolean isMacOS() {
        return OS.contains("mac") && OS.contains("os");
    }

    public static boolean isWindows() {
        return OS.contains("windows");
    }

    public static boolean isOS2() {
        return OS.contains("os/2");
    }

    public static boolean isSolaris() {
        return OS.contains("solaris");
    }

    public static boolean isSunOS() {
        return OS.contains("sunos");
    }

    public static boolean isMPEiX() {
        return OS.contains("mpe/ix");
    }

    public static boolean isHPUX() {
        return OS.contains("hp-ux");
    }

    public static boolean isAix() {
        return OS.contains("aix");
    }

    public static boolean isOS390() {
        return OS.contains("os/390");
    }

    public static boolean isFreeBSD() {
        return OS.contains("freebsd");
    }

    public static boolean isIrix() {
        return OS.contains("irix");
    }

    public static boolean isDigitalUnix() {
        return OS.contains("digital") && OS.contains("unix");
    }

    public static boolean isNetWare() {
        return OS.contains("netware");
    }

    public static boolean isOSF1() {
        return OS.contains("osf1");
    }

    public static boolean isOpenVMS() {
        return OS.contains("openvms");
    }

}
