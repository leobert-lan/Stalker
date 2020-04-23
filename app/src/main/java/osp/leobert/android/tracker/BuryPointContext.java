package osp.leobert.android.tracker;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p><b>Package:</b> com.jdd.motorfans.burylog </p>
 * <p><b>Project:</b> MotorFans </p>
 * <p><b>Classname:</b> BuryPointContext </p>
 * <p><b>Description:</b> 埋点上下文环境,核心发起者 </p>
 * Created by leobert on 2018/12/18.
 */
/*public*/
abstract class BuryPointContext {
    public static final String LOG_TAG = "track_msg";
//    public static final BuryPointContext NO_OP = new BuryPointContext() {
//        @Override
//        public boolean allowWrapper(@NonNull BuryPointContextWrapper wrapper) {
//            return false;
//        }
//
//        @Nullable
//        @Override
//        public List<Pair<String, String>> createContextData(String pointKey) {
//            return null;
//        }
//    };

    /**
     * 按照key收集上下文数据，<em>仅处理当前层</em>
     */
    @Nullable
    public List<Pair<String, String>> allocateContextData(String key) {
        return createContextData(key);
    }

    @Nullable
    BuryPointContextWrapper parentWrapper;

    protected Set<BuryPoint> transferByKey(String original) {
        return Collections.emptySet();
    }

    public boolean allowWrapper(@NonNull BuryPointContextWrapper wrapper) {
        return !this.equals(wrapper);
    }

    /**
     * @param wrapper 外层包裹器,将本身包裹进去
     */
    public boolean wrapBy(@NonNull BuryPointContextWrapper wrapper) {
        if (allowWrapper(wrapper)) {
            wrapper.addChild(this);
            parentWrapper = wrapper;
            return true;
        }
        return false;
    }

    /**
     * 按照key获取相关的上下文数据
     *
     * @param pointKey key，可能是真的上报key，也可能是虚拟key
     * @return 上下文数据
     */
    @Nullable
    @Deprecated
    public abstract List<Pair<String, String>> createContextData(String pointKey);

    public void track(String pointKey) {
        try {
            track(pointKey, (Pair<String, String>[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SafeVarargs
    public final void track(String pointKey, Pair<String, String>... appendData) {
        try {
            BuryPoint buryPoint = BuryPoint.normal(pointKey);
            track(buryPoint, appendData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void track(@NonNull BuryPoint point) {
        try {
            track(point, (Pair<String, String>[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SafeVarargs
    public final void track(@NonNull BuryPoint point, Pair<String, String>... appendData) {
        try {
            track(point, true, appendData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SafeVarargs
    public final void track(@NonNull BuryPoint point, boolean useParent, Pair<String, String>... appendData) {
        if (useParent) {
            //修改，让自己先收集一下数据，然后让父层处理上报
//            if (parentWrapper != null) {
//                parentWrapper.track(point, true, appendData);
//                return;
//            }
            List<Pair<String, String>> data = new ArrayList<>();
            if (appendData != null)
                data.addAll(Arrays.asList(appendData));
            allocateAllDataThenDispatch(point, data);
            return;
        }

        try {
            point.handle(this, appendData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void allocateAllDataThenDispatch(@NonNull BuryPoint point, List<Pair<String, String>> appendData) {
        try {
            point.allocate(this, appendData);
            if (parentWrapper != null) {
                parentWrapper.notifyActive(this);
                parentWrapper.allocateAllDataThenDispatch(point, appendData);
            } else {
                point.handle(this, appendData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void injectChunkData(@NonNull DataRepoChunk dataRepoChunk) {
        //inject the data of this chunk
    }

    void uploadPoint(String pointKey, List<Pair<String, String>> params) {
//        if (AppUtil.isDebuggable())
//            L.e("track_msg", "point key:" + pointKey + "\r\nparams:" + GsonUtil.toJson(params));
//        MotorLogManager.track(pointKey, params);
        if (buryPointUploader != null)
            buryPointUploader.upload(pointKey, params);
    }

    @Nullable
    public static BuryPointUploader buryPointUploader;

    public interface BuryPointUploader {
        void upload(String pointKey, List<Pair<String, String>> params);
    }
}
