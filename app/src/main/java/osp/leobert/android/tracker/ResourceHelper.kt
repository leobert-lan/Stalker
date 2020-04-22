package osp.leobert.android.tracker

import android.content.Context
import android.text.TextUtils
import android.view.View
import androidx.annotation.AnyRes
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.core.util.Preconditions
import java.util.*

/**
 * <p><b>Package:</b> osp.leobert.android.tracker </p>
 * <p><b>Classname:</b> ResourceHelper </p>
 * Created by leobert on 2019-11-07.
 */
class ResourceHelper {
    companion object {

        fun getGlobalIdName(@NonNull view: View): String? {
            Preconditions.checkNotNull(view)

            val id = view.id
            /*LayoutInflaterWrapper会为根View添加如下tag值*/
            val idNameSpace = view.getTag(R.id.id_namespace_tag) as String?
            val isRootView = !TextUtils.isEmpty(idNameSpace)
            if (id == View.NO_ID && !isRootView) {
                //this view has no id assigned
                return null
            }

            try {
                val context = view.context

//            val activityName = context.javaClass.simpleName
                val layoutFileName = getLayoutFileName(view)
                val idName: String
                idName = if (id == View.NO_ID) {
                    /*如果View 无id，且该View是布局文件的根View,将
                    * 文件名作为View的唯一标识*/
                    String.format(Locale.CHINA, "root_id:%s", layoutFileName)
                } else {
                    getResourceEntryName(context, id)
                }

                return String.format("%s_%s", layoutFileName, idName)
            } catch (e: Exception) {
                e.printStackTrace()

                //error occur when fetch id resource
                return null
            }

        }

        /**
         * @param view
         * @return 返回值不区分 ""和null，统一返回""
         */
        @NonNull
        private fun getLayoutFileName(@NonNull view: View): String {

            val idNameSpace = view.getTag(R.id.id_namespace_tag) as String?
            if (!TextUtils.isEmpty(idNameSpace)) return idNameSpace!!

            var tmp = view
            while (tmp.parent != null && tmp.parent is View) {
                val parent = tmp.parent as View

                val space = parent.getTag(R.id.id_namespace_tag) as String?
                if (!TextUtils.isEmpty(space)) return space!!

                tmp = parent
            }

            return ""
        }

        /**
         * @param context
         * @param id
         * @return 返回值不区分 ""和null，统一返回""
         */
        fun getResourceEntryName(@NonNull context: Context, @AnyRes id: Int): String {
            Preconditions.checkNotNull(context)

            return try {
                context.resources.getResourceEntryName(id)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }
}

fun Int.idKey(@IdRes id: Int, context: Context): String {
    val layoutFileName = ResourceHelper.getResourceEntryName(context, this)
    val idName = ResourceHelper.getResourceEntryName(context, id)

    return String.format("%s_%s", layoutFileName, idName)
}