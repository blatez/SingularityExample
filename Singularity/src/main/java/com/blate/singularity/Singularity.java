package com.blate.singularity;

import com.blate.singularity.log.Logg;

public class Singularity {

    private static final String TAG = "Singularity";

    /**
     * 强制设置是否是debug模式
     * 这个标志存在的意义是如果直接使用{@link BuildConfig#DEBUG},主module依赖的每个子module都是release模式,
     * 因为默认会依赖release版本,除非在gradle配置依赖模式,侵入性太大了,可以在主module中设置该值为主module
     * 的BuildConfig#DEBUG.统一模式.
     */
    private static Boolean sDebugForcibly;

    /**
     * 强制调制模式锁存标志
     */
    private static boolean sLatchDebugForcibly = false;


    public static boolean debug() {
        if (sDebugForcibly != null) {
            return sDebugForcibly;
        } else {
            //这个值不是很可靠,因为默认依赖的都是release,除非在每个依赖这个module的地方配置:
            //dependencies {
            //	releaseImplementation project(path: ':library', configuration: 'release')
            //	// debug 模式下，使用 debug 版本生成 BuildConfig
            //	debugImplementation project(path: ':library', configuration: 'debug')
            //}
            return BuildConfig.DEBUG;
        }
    }

    public static void setDebugForcibly(boolean debugForcibly) {
        if (sLatchDebugForcibly) {
            Logg.w(TAG, String.format("[DebugForcibly] set multiple times; set value[%s]fail; the current using value [%s]",
                    debugForcibly, sDebugForcibly));
            return;
        }
        sDebugForcibly = debugForcibly;
        sLatchDebugForcibly = true;
    }


}
