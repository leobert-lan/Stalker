package osp.leobert.android.tracker;

import android.util.Pair;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 外部套用，用于修改、拦截、添加事件
 * 2019年11月12日13:00:39修改：不再拦截，仅用于修改和添加
 */
public abstract class BuryPointContextWrapper extends BuryPointContext {

    public static BuryPointContextWrapper createDefault() {
        return new BuryPointContextWrapper() {
            @Nullable
            @Override
            protected List<Pair<String, String>> createContextDataInternal(String pointKey) {
                return Collections.emptyList();
            }
        };
    }

    /**
     * 已经激活的child，因为其本身向父层传递了事件而激活
     */
    @Nullable
    private BuryPointContext activeChild;
    private final Set<BuryPointContext> children = new HashSet<>();

    @Override
    protected final Set<BuryPoint> transferByKey(String original) {
        Set<BuryPoint> tmp = new HashSet<>(transferKeyInternal(original));
        if (activeChild != null)
            tmp.addAll(activeChild.transferByKey(original));
        return tmp;
    }

    public boolean allowWrapper(@NonNull BuryPointContextWrapper wrapper) {
        return notAncestorOrMyself(wrapper)/*不能完全保证无循环嵌套*/;
    }

    private boolean notAncestorOrMyself(@NonNull BuryPointContextWrapper target) {
        if (parentWrapper == null)
            return !this.equals(target);
        return !this.equals(target) && parentWrapper.notAncestorOrMyself(target);
    }

    protected Set<BuryPoint> transferKeyInternal(String original) {
        return Collections.emptySet();
    }

    void addChild(BuryPointContext child) {
        children.add(child);
    }

    @Nullable
    @CallSuper
    public List<Pair<String, String>> createContextData(String pointKey) {
        List<Pair<String, String>> ret = null;
        List<Pair<String, String>> internal = createContextDataInternal(pointKey);
        if (internal != null) {
            ret = new ArrayList<>(internal);
        }
        if (activeChild != null) {
            List<Pair<String, String>> wrapRet = activeChild.createContextData(pointKey);
            if (wrapRet != null) {
                if (ret == null) ret = new ArrayList<>();
                ret.addAll(wrapRet);
            }
            //已经处理完事件数据，让其失活
            activeChild = null;
        }
        return ret;
    }

    @Override
    public List<Pair<String, String>> allocateContextData(String key) {
        return createContextDataInternal(key);
    }

    @Nullable
    protected abstract List<Pair<String, String>> createContextDataInternal(String pointKey);

    void notifyActive(BuryPointContext buryPointContext) {
        activeChild = buryPointContext;
    }
}
