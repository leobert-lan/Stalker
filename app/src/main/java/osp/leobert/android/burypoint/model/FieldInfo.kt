package osp.leobert.android.burypoint.model

import osp.leobert.android.burypoint.DataHandler
import osp.leobert.android.burypoint.FieldReader
import java.lang.reflect.Field

/**
 * <p><b>Package:</b> osp.leobert.android.burypoint.model </p>
 * <p><b>Project:</b> BuryPointContextDemo </p>
 * <p><b>Classname:</b> FieldInfo </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2020-04-01.
 */
class FieldInfo(
    val clzTag: String? = null,
    val propertyName: String,
    val field: Field,
    val fieldReader: FieldReader
) {

    private val pn = if (clzTag == null) propertyName else clzTag + "_" + propertyName

    fun key(namePrefix: String): String =
        "$namePrefix${DataHandler.separator}$pn"


    override fun toString(): String {
        return "FieldInfo(propertyName='$propertyName')"
    }


}