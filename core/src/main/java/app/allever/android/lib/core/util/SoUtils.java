package app.allever.android.lib.core.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SoUtils {
    public static final String X86_64 = "x86_64";
    public static final String X86 = "x86";
    public static final String ARM64_V8A = "arm64-v8a";
    public static final String ARMEABI_V7A = "armeabi-v7a";
    public static final String ARMEABI = "armeabi";

    public static String getCpuEabi() {
        String eabi = "";
        try {
            String os_cpuabi = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop ro.product.cpu.abi").getInputStream())).readLine();
            if (os_cpuabi.contains(SoUtils.X86_64)) {
                eabi = SoUtils.X86_64;
            } else if (os_cpuabi.contains(SoUtils.X86)) {
                eabi = SoUtils.X86;
            } else if (os_cpuabi.contains(SoUtils.ARM64_V8A)) {
                eabi = SoUtils.ARM64_V8A;
            } else if (os_cpuabi.contains(SoUtils.ARMEABI_V7A)) {
                eabi = SoUtils.ARMEABI_V7A;
            } else if (os_cpuabi.contains(SoUtils.ARMEABI)) {
                eabi = SoUtils.ARMEABI;
            }
        } catch (Exception e) {
            eabi = "";
            e.printStackTrace();
        }
        return eabi;
    }
}
