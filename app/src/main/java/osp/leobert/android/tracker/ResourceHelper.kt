package osp.leobert.android.tracker

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.text.TextUtils
import android.view.View
import androidx.annotation.AnyRes
import osp.leobert.android.burypointcontextdemo.R
import java.util.*

/**
 * <p><b>Package:</b> osp.leobert.android.tracker </p>
 * <p><b>Classname:</b> ResourceHelper </p>
 * Created by leobert on 2019-11-07.
 */
class ResourceHelper {
    companion object {

        fun getGlobalIdName(view: View): String? {
            Preconditions.checkNotNull(view)

            val id = view.id
            /*LayoutInflaterWrapper会为根View添加如下tag值*/
            val idNameSpace: String? = view.getTag(R.id.id_namespace_tag) as String?
            val isRootView = !TextUtils.isEmpty(idNameSpace)
            if (id == View.NO_ID && !isRootView) {
                //this view has no id assigned
                return "{NO_ID}"
            }

            try {
                var context = view.context
                while (context is ContextWrapper) {
                    if (context is Activity) break
                    context = context.baseContext
                }

                val activityName = context.javaClass.simpleName
                val layoutFileName = getParentLocation(view)
                val idName: String
                idName = if (id == View.NO_ID) {
                    /*如果View 无id，且该View是布局文件的根View,将
                        * 文件名作为View的唯一标识*/
                    String.format(Locale.CHINA, "root_id:%s", layoutFileName)
                } else {
                    getResourceEntryName(context, id)
                }

                return String.format("%s#%s#%s", activityName, layoutFileName, idName)
            } catch (e: Exception) {
                e.printStackTrace()
                //error occur when fetch id resource
                return null
            }
        }

        private fun getParentLocation(view: View): String { //layout file name and it's parents

            val idNameSpace = view.getTag(R.id.id_namespace_tag) as String?
            if (!TextUtils.isEmpty(idNameSpace)) return idNameSpace.toString()

            var tmp = view
            var string  = ""
            while (tmp.parent != null && tmp.parent is View) {
                val parent = tmp.parent as View

                val space = parent.getTag(R.id.id_namespace_tag) as String?
                if (!TextUtils.isEmpty(space))
                    return space.toString()+string
                else {
                    val id = parent.id
                    id.apply {
                        string = if (this == View.NO_ID)
                            "#{NO_ID}$string"
                        else {
                            "#${getResourceEntryName(view.context, id)}$string"
                        }
                    }
                }


                tmp = parent
            }

            return "*"
        }

        fun getResourceEntryName(context: Context, @AnyRes id: Int): String {
            Preconditions.checkNotNull(context)

            return try {
                context.resources.getResourceEntryName(id)
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
                "*"
            }

        }
    }
}

//fun Int.idKey(@IdRes id: Int, context: Context): String {
//    val layoutFileName = ResourceHelper.getResourceEntryName(context, this)
//    val idName = ResourceHelper.getResourceEntryName(context, id)
//
//    return String.format("%s_%s", layoutFileName, idName)
//}