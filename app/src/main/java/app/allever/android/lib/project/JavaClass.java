package app.allever.android.lib.project;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import app.allever.android.lib.core.app.App;
import app.allever.android.lib.core.function.imagecompress.CompressResult;
import app.allever.android.lib.core.function.imagecompress.ImageCompress;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.Dispatchers;

public class JavaClass {
    public static void test() {
        List<String > list = new ArrayList<>();
        ImageCompress.INSTANCE.compress(App.context, list, new Continuation<CompressResult>() {
            @NonNull
            @Override
            public CoroutineContext getContext() {
                return (CoroutineContext) Dispatchers.getIO();
            }

            @Override
            public void resumeWith(@NonNull Object o) {

            }
        });
    }
}
