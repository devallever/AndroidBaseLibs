package app.allever.android.lib.network.demo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import app.allever.android.lib.core.base.AbstractActivity;
import app.allever.android.lib.core.util.LogUtils;
import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.Dispatchers;

public class NetActivityJava extends AbstractActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NetRepository.INSTANCE.test(new Continuation<String>() {
            @NonNull
            @Override
            public CoroutineContext getContext() {
                return Dispatchers.getDefault();
            }

            @Override
            public void resumeWith(@NonNull Object o) {
                LogUtils.INSTANCE.d(o.toString());
            }
        });
//        NetRepository.INSTANCE.getBanner(new Continuation<Result<? extends BaseResponse<List<? extends BannerData>>>>() {
//            @NonNull
//            @Override
//            public CoroutineContext getContext() {
//                return Dispatchers.getDefault();
//            }
//
//            @Override
//            public void resumeWith(@NonNull Object o) {
//                String result = o.toString();
//            }
//        });
    }
}
