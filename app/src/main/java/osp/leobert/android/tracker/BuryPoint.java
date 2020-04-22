package osp.leobert.android.tracker;

import android.util.Pair;

import androidx.annotation.NonNull;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * <p><b>Package:</b> com.jdd.motorfans.burylog </p>
 * <p><b>Project:</b> MotorFans </p>
 * <p><b>Classname:</b> BuryPoint </p>
 * <p><b>Description:</b> 埋点 </p>
 * Created by leobert on 2018/12/19.
 */
public abstract class BuryPoint {

    public static BuryPoint normal(@NonNull String key) {
        return new NormalBuryPoint(key);
    }

    public static BuryPoint transfer(@NonNull String key) {
        return new TransferBuryPoint(key);
    }

    @NonNull
    private String key;

    @NonNull
    public String getKey() {
        return key;
    }

    BuryPoint(@NonNull String key) {
        this.key = key;
    }

    void handle(@NonNull BuryPointContext context, Pair<String, String>[] appendData) {

        List<Pair<String, String>> contextData = context.createContextData(key);

        List<Pair<String, String>> tmp = new ArrayList<>();
        if (appendData != null)
            tmp.addAll(Arrays.asList(appendData));
        if (contextData != null)
            tmp.addAll(contextData);

        //去重
        Set<Pair<String, String>> tmp2 = new LinkedHashSet<>(tmp);
        tmp.clear();
        tmp.addAll(tmp2);

        if (tmp.isEmpty())
            context.uploadPoint(key, null);
        else
            context.uploadPoint(key, tmp);
    }

    void handle(@NonNull BuryPointContext context, List<Pair<String, String>> appendData) {

        List<Pair<String, String>> contextData = context.createContextData(key);

        List<Pair<String, String>> tmp = new ArrayList<>();

        if (appendData != null)
            tmp.addAll(appendData);

        if (contextData != null)
            tmp.addAll(contextData);
        //去重
        Set<Pair<String, String>> tmp2 = new LinkedHashSet<>(tmp);
        tmp.clear();
        tmp.addAll(tmp2);

        if (tmp.isEmpty())
            context.uploadPoint(key, null);
        else
            context.uploadPoint(key, tmp);
    }

    void allocate(@NonNull BuryPointContext context, @NonNull List<Pair<String, String>> appendData) {

        List<Pair<String, String>> contextData = context.allocateContextData(key);

        List<Pair<String, String>> tmp = new ArrayList<>();
        if (contextData != null)
            tmp.addAll(contextData);

        appendData.addAll(tmp);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuryPoint buryPoint = (BuryPoint) o;

        return key.equals(buryPoint.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    /**
     * 普通点，不会派生其他点
     */
    public static class NormalBuryPoint extends BuryPoint {
        NormalBuryPoint(@NonNull String key) {
            super(key);
        }
    }

    /**
     * 本身点名无法确定，key只是一个事件唯一标识，根据上下文处理成真正的点
     */
    public static class TransferBuryPoint extends BuryPoint {

        TransferBuryPoint(@NonNull String key) {
            super(key);
        }

        @Override
        void handle(@NonNull BuryPointContext context, Pair<String, String>[] appendData) {
            Set<BuryPoint> tmp = context.transferByKey(getKey());
            if (tmp == null)
                return;
            tmp.remove(this);
            for (BuryPoint point : tmp) {
                point.handle(context, appendData);
            }
        }

        void handle(@NonNull BuryPointContext context, List<Pair<String, String>> appendData) {
            Set<BuryPoint> tmp = context.transferByKey(getKey());
            if (tmp == null)
                return;
            tmp.remove(this);

            for (BuryPoint point : tmp) {
                point.handle(context, appendData);
            }
        }

        @Override
        void allocate(@NonNull BuryPointContext context, @NonNull List<Pair<String, String>> appendData) {
            super.allocate(context, appendData);
        }
    }

    /**
     * 可派生的点，慎用，目前我能想到的场景都可以使用TransferBuryPoint 构建出多个NormalBuryPoint处理
     * //     * @hide
     */
    private static class FissileBuryPoint extends BuryPoint {

        public FissileBuryPoint(@NonNull String key) {
            super(key);
        }
    }

}
