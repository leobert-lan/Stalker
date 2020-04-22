package osp.leobert.android.burypoint.model

import osp.leobert.android.burypoint.DataHandler
import osp.leobert.android.burypoint.MethodReader
import java.lang.reflect.Method

/**
 * <p><b>Package:</b> osp.leobert.android.burypoint.model </p>
 * <p><b>Project:</b> BuryPointContextDemo </p>
 * <p><b>Classname:</b> MethodInfo </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2020/4/21.
 */
class MethodInfo(
    val clzTag: String? = null,
    val nameChunk: String,
    val method: Method,
    val methodReader: MethodReader
) {

    private val pn = if (clzTag == null) nameChunk else clzTag + "_" + nameChunk

    fun key(namePrefix: String): String =
        "$namePrefix${DataHandler.separator}$pn"


//    fun key(namePrefix: String): String = "$namePrefix${DataHandler.separator}$nameChunk"

}