package app.allever.android.lib.core.util;

import java.util.ArrayList;

public class ArrayUtils {

    public static <T> ArrayList<T> copyArray(ArrayList<T> data){
        ArrayList<T> result = new ArrayList<T>();
        int l = data.size();
        for(int i = 0 ; i < l ; i++){
            result.add(data.get(i));
        }
        return result;
    }
}
