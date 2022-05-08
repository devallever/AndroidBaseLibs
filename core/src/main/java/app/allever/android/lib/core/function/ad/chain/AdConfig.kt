package app.allever.android.lib.core.function.ad.chain

class AdConfig {
    var business: MutableList<BusinessBean>? = null
    var adConfig: MutableList<AdConfigBean>? = null

    class BusinessBean {
        /**
         * name : 厂家-定值：A, AMZ, F, MI, IFly
         * appId :
         * appKey :
         * token :
         */

        var name: String? = null
        var appId: String? = null
        var appKey: String? = null
        var token: String? = null
    }

    class AdConfigBean {
        /**
         * name : 广告位名称-变量：例如主页退出插屏
         * type : 广告类型-定值：Banner，Insert，Video
         * chain : [{"business":"厂家-定值：A, AMZ, F, MI, IFly","adPosition":"广告位"},{"business":"A: AdMob","adPosition":"广告位"},{"business":"AMZ: 亚马逊","adPosition":"广告位"},{"business":"F: Facebook","adPosition":"广告位"},{"business":"MI: 小米","adPosition":"广告位"},{"business":"IFly: 科大讯飞","adPosition":"广告位"}]
         */

        var name: String? = null
        var type: String? = null
        var chain: MutableList<ChainBean>? = null

        class ChainBean {
            /**
             * business : 厂家-定值：A, AMZ, F, MI, IFly
             * adPosition : 广告位
             */

            var business: String? = null
            var adPosition: String? = null
        }
    }
}
