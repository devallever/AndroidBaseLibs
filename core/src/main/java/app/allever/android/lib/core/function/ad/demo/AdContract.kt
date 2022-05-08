package app.allever.android.lib.core.function.ad.demo

import app.allever.android.lib.core.BuildConfig
import app.allever.android.lib.core.function.ad.ADType
import app.allever.android.lib.core.function.ad.AdBusiness

object AdContract {
    private val XIAO_MI_APP_ID = if (BuildConfig.DEBUG) {
        "2882303761517411490"
    } else {
        "2882303761518207094"
    }

    // 以下两个没有的话就按照以下传入
    const val XIAO_MI_APP_KEY = "fake_app_key"
    const val XIAO_MI_APP_TOKEN = "fake_app_token"

    val INSERT = if (BuildConfig.DEBUG) {
        "1d576761b7701d436f5a9253e7cf9572"
    } else {
        "eb075d1ea0806afa7bdbf1179315fb37"
    }

    val MAIN_BACK_INSERT = if (BuildConfig.DEBUG) {
        "1d576761b7701d436f5a9253e7cf9572"
    } else {
        "4dbe4097dc3b6ca1c2ca8d61d111c15c"
    }
    val ADVANCED_SETTING_ENTER_INSERT = if (BuildConfig.DEBUG) {
        "1d576761b7701d436f5a9253e7cf9572"
    } else {
        "229775d13b912d0273f885272b25c45a"
    }
    val SELECTED_RINGTONE_INSERT = if (BuildConfig.DEBUG) {
        "1d576761b7701d436f5a9253e7cf9572"
    } else {
        "16fcbe7ee15cf0eb90d532ac57999302"
    }
    val SETTING_INSERT = if (BuildConfig.DEBUG) {
        "1d576761b7701d436f5a9253e7cf9572"
    } else {
        "cb1b9b797149f3aea9a1ff5f422ce04e"
    }
    val SELECTED_WALL_PAGER_INSERT = if (BuildConfig.DEBUG) {
        "1d576761b7701d436f5a9253e7cf9572"
    } else {
        "076c4500a1c03bd47f0994c92e52517e"
    }

    val MAIN_BANNER = if (BuildConfig.DEBUG) {
        "802e356f1726f9ff39c69308bfd6f06a"
    } else {
        "4cea409bf739019cfac2cde8822bb1f9"
    }

    val GUIDE_BANNER = if (BuildConfig.DEBUG) {
        "802e356f1726f9ff39c69308bfd6f06a"
    } else {
        "fdbeb0ec7989f21ab477e9a914d99280"
    }

    val SETTING_BANNER = if (BuildConfig.DEBUG) {
        "802e356f1726f9ff39c69308bfd6f06a"
    } else {
        "da0a4151248552a7684079f90eba203a"
    }

    val ADVANCED_SETTING_BANNER = if (BuildConfig.DEBUG) {
        "802e356f1726f9ff39c69308bfd6f06a"
    } else {
        "eaf3136d01cc3a1e2ae0ae48db6f4d4a"
    }

    val SUPPORT_ENCOURAGE = if (BuildConfig.DEBUG) {
        "92d90db71791e6b9f7caaf46e4a997ec"
    } else {
        "65d6c82cad78449debfdbb7b7595087e"
    }


    //462550602
//    val ADMOB_APP_ID = "462550602\nca-app-pub-5576030072710638~6453713586"
//    private const val ADMOB_MAIN_BANNER = "ca-app-pub-5576030072710638/2549772372"
//    private const val ADMOB_GUIDE_BANNER = ADMOB_MAIN_BANNER
//    private const val ADMOB_SETTING_BANNER = ADMOB_MAIN_BANNER
//    private const val ADMOB_ADVANCED_SETTING_BANNER = "ca-app-pub-5576030072710638/1829487574"
//    private const val ADMOB_EXIT_INSERT = "ca-app-pub-5576030072710638/7202004264"
//    private const val ADMOB_BACK_MAIN_INSERT = "ca-app-pub-5576030072710638/2015505621"
//    private const val ADMOB_SETTING_INSERT = "ca-app-pub-5576030072710638/5571607253"
//    private const val ADMOB_SETTING_VIDEO = "ca-app-pub-5576030072710638/2358200685"
//    private const val ADMOB_COMMON_NATIVE = ""


    //adeverdeng
    val ADMOB_APP_ID = "adeverdeng\nca-app-pub-9972782174497381~7872748217"
    private const val ADMOB_MAIN_BANNER = "ca-app-pub-9972782174497381/1718403402"
    private const val ADMOB_GUIDE_BANNER = "ca-app-pub-9972782174497381/8994258190"
    private const val ADMOB_SETTING_BANNER = "ca-app-pub-9972782174497381/3741931514"
    private const val ADMOB_ADVANCED_SETTING_BANNER = "ca-app-pub-9972782174497381/6176523164"
    private const val ADMOB_EXIT_INSERT = "ca-app-pub-9972782174497381/8611114817"
    private const val ADMOB_BACK_MAIN_INSERT = "ca-app-pub-9972782174497381/9213750049"
    private const val ADMOB_SETTING_INSERT = "ca-app-pub-9972782174497381/2045706461"
    private const val ADMOB_SETTING_VIDEO = "ca-app-pub-9972782174497381/9022178359"
    private const val ADMOB_COMMON_NATIVE = "ca-app-pub-9972782174497381/7709096689"

    //adcocoallever@gmail.com
//    val ADMOB_APP_ID = "adcocoallever\nca-app-pub-2202120086964772~1224499251"
//    private const val ADMOB_MAIN_BANNER = "ca-app-pub-2202120086964772/2223855777"
//    private const val ADMOB_GUIDE_BANNER = "ca-app-pub-2202120086964772/6630553382"
//    private const val ADMOB_SETTING_BANNER = "ca-app-pub-2202120086964772/6438981693"
//    private const val ADMOB_ADVANCED_SETTING_BANNER = "ca-app-pub-2202120086964772/3812818351"
//    private const val ADMOB_EXIT_INSERT = "ca-app-pub-2202120086964772/8873573341"
//    private const val ADMOB_BACK_MAIN_INSERT = "ca-app-pub-2202120086964772/3345365758"
//    private const val ADMOB_SETTING_INSERT = "ca-app-pub-2202120086964772/8406120744"
//    private const val ADMOB_SETTING_VIDEO = "ca-app-pub-2202120086964772/1840712394"
//    private const val ADMOB_COMMON_NATIVE = "ca-app-pub-2202120086964772/9527630727";

    val AD_NAME_EXIT_INSERT = "AD_NAME_EXIT_INSERT"

    val AD_NAME_MAIN_BACK_INSERT = "AD_NAME_MAIN_BACK_INSERT"

    val AD_NAME_SETTING_INSERT = "AD_NAME_SETTING_INSERT"

    val AD_NAME_ADVANCED_SETTING_INSERT = "AD_NAME_ADVANCED_SETTING_INSERT"

    val AD_NAME_SELECTED_RINGTONE_INSERT = "AD_NAME_SELECTED_RINGTONE_INSERT"

    val AD_NAME_SELECTED_WALL_PAGER_INSERT = "AD_NAME_SELECTED_WALL_PAGER_INSERT"

    val AD_NAME_MAIN_BANNER = "AD_NAME_MAIN_BANNER"

    val AD_NAME_SETTING_BANNER = "AD_NAME_SETTING_BANNER"

    val AD_NAME_GUIDE_BANNER = "AD_NAME_GUIDE_BANNER"

    val AD_NAME_ADVANCED_SETTING_BANNER = "AD_NAME_ADVANCED_SETTING_BANNER"

    val AD_NAME_SETTING_ENCOURAGE_VIDEO = "AD_NAME_SETTING_ENCOURAGE_VIDEO"

    val AD_NAME_COMMON_NATIVE = "AD_NAME_COMMON_NATIVE"

    val AD_NAME_COMMON_NATIVE_SMALL = "AD_NAME_COMMON_NATIVE_SMALL"


    val adData = "{\n" +
            "  \"business\": [\n" +
            "    {\n" +
            "      \"name\": \"${AdBusiness.A}\",\n" +
            "      \"appId\": \"\",\n" +
            "      \"appKey\": \"\",\n" +
            "      \"token\": \"\"\n" +
            "    },\n" +
            "   {\n" +
            "      \"name\": \"${AdBusiness.MI}\",\n" +
            "      \"appId\": \"$XIAO_MI_APP_ID\",\n" +
            "      \"appKey\": \"$XIAO_MI_APP_KEY\",\n" +
            "      \"token\": \"$XIAO_MI_APP_TOKEN\"\n" +
            "    }" +
            "  ],\n" +
            "  \"adConfig\": [\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_EXIT_INSERT\",\n" +
            "      \"type\": \"${ADType.INSERT}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_EXIT_INSERT\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$INSERT\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_MAIN_BACK_INSERT\",\n" +
            "      \"type\": \"${ADType.INSERT}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_BACK_MAIN_INSERT\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$MAIN_BACK_INSERT\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_SETTING_INSERT\",\n" +
            "      \"type\": \"${ADType.INSERT}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_SETTING_INSERT\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$SETTING_INSERT\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_ADVANCED_SETTING_INSERT\",\n" +
            "      \"type\": \"${ADType.INSERT}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_SETTING_INSERT\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$ADVANCED_SETTING_ENTER_INSERT\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_SELECTED_RINGTONE_INSERT\",\n" +
            "      \"type\": \"${ADType.INSERT}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$SELECTED_RINGTONE_INSERT\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_SELECTED_WALL_PAGER_INSERT\",\n" +
            "      \"type\": \"${ADType.INSERT}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$SELECTED_WALL_PAGER_INSERT\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_MAIN_BANNER\",\n" +
            "      \"type\": \"${ADType.BANNER}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_MAIN_BANNER\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$MAIN_BANNER\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_SETTING_BANNER\",\n" +
            "      \"type\": \"${ADType.BANNER}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_SETTING_BANNER\"\n" +
            "        }," +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$SETTING_BANNER\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_GUIDE_BANNER\",\n" +
            "      \"type\": \"${ADType.BANNER}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_GUIDE_BANNER\"\n" +
            "        }," +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$GUIDE_BANNER\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_ADVANCED_SETTING_BANNER\",\n" +
            "      \"type\": \"${ADType.BANNER}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_ADVANCED_SETTING_BANNER\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$ADVANCED_SETTING_BANNER\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_COMMON_NATIVE\",\n" +
            "      \"type\": \"${ADType.NATIVE}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_COMMON_NATIVE\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_COMMON_NATIVE_SMALL\",\n" +
            "      \"type\": \"${ADType.NATIVE_SMALL}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_COMMON_NATIVE\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_SETTING_ENCOURAGE_VIDEO\",\n" +
            "      \"type\": \"${ADType.VIDEO}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_SETTING_VIDEO\"\n" +
            "        }," +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$SUPPORT_ENCOURAGE\"\n" +
            "        }" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}\n"
}
