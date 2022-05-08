package app.allever.android.lib.ad.admob;//package com.allever.lib.ad.admob;
//
//import android.content.Context;
//import android.view.View;
//
//import com.allever.lib.common.util.log.LogUtils;
//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdSize;
//import com.google.android.gms.ads.AdView;
//import com.mob.bean.Model;
//import com.mob.core.MobBannerAd;
//import com.mob.tool.Utils;
//
///**
// * Created by Administrator on 2016/8/26.
// */
//public class MobAdmobBan extends MobBannerAd {
//
//    private  AdView admobbanner = null;
//    public MobAdmobBan(Context context, String pub){
//        super(context,pub);
//    }
//    @Override
//    public void loadAd() {
//        admobbanner  = new AdView(mContext);
//        admobbanner.setAdSize(AdSize.BANNER);
//        admobbanner.setAdUnitId(mPub);
//        admobbanner.setAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(int i) {
//                super.onAdFailedToLoad(i);
//                LogUtils.INSTANCE.d("加载 AdMob Banner 失败 code = " + i + "pub = " + mPub);
//                if(mAdListener != null) {
//                    Utils.printInfo("faild " + mPub);
//                    mAdListener.onAdBanFailedToLoad();
//                    mAdListener = null;  //有些sdk可能一次请求回调多次失败或成功状态，那么我们应该避免这些问题
//                }
//            }
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                LogUtils.INSTANCE.d("加载 AdMob Banner 成功 code = "  +  "pub = " + mPub);
//                if(mAdListener != null) {
//                    Utils.printInfo("suceess " + mPub);
//                    mAdListener.onAdBanLoaded(MobAdmobBan.this);
//                    mAdListener = null;  //有些sdk可能一次请求回调多次失败或成功状态，那么我们应该避免这些问题
//                }
//            }
//        });
//        Utils.printInfo("loadAd "+getTag()+mPub);
//
//        //加载请求
//        AdRequest.Builder reqBuild = new AdRequest.Builder()
//                .addTestDevice("1621DB3C172AE6711BA840F4AEF6EF48")
//                .addTestDevice("811A5A5DA1BF1E2FC9EE39041EC322FF");
//        for (String device: Model.getInstance().getTestDevice()) {
//            reqBuild.addTestDevice(device);
//        }
//        admobbanner.loadAd( reqBuild.build());
//
//    }
//
//    @Override
//    public View getBannerView() {
//        return admobbanner;
//    }
//
//    @Override
//    public void initSdk() {
//
//    }
//
//    @Override
//    public String getTag() {
//        return "Admob ban";
//    }
//}
