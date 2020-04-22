package osp.leobert.android.burypoint

import osp.leobert.android.burypoint.log.impl.DefaultLogger
import osp.leobert.android.burypoint.model.FieldInfo
import osp.leobert.android.burypoint.model.MethodInfo
import osp.leobert.android.burypoint.model.VisitCard
import osp.leobert.android.burypoint.notation.PdInherit
import osp.leobert.android.burypoint.notation.PointData
import osp.leobert.android.burypoint.type.Type
import osp.leobert.android.burypoint.type.TypeInferUtils
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*

/**
 * <p><b>Package:</b> osp.leobert.android.burypoint </p>
 * <p><b>Project:</b> BuryPointContextDemo </p>
 * <p><b>Classname:</b> DataHandler </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2020-04-01.
 */
@Suppress("WeakerAccess", "Unused")
object DataHandler {

    val logger = DefaultLogger("[tracker] ")
    const val separator = "."


    private val fieldStrategies: MutableMap<String, List<FieldInfo>> = hashMapOf()

    private val methodStrategies: MutableMap<String, List<MethodInfo>> = hashMapOf()


    fun isFieldStrategyExist(visitCard: VisitCard<*>): Boolean {
        return this.isFieldStrategyExist(visitCard.address)
    }

    private fun isFieldStrategyExist(objectClz: Class<*>): Boolean {
        return this.isFieldStrategyExist(objectClz.name)
    }

    private fun isFieldStrategyExist(objectClzPath: String): Boolean {
        return fieldStrategies.containsKey(objectClzPath)
    }

    fun isMethodStrategyExist(visitCard: VisitCard<*>): Boolean {
        return this.isMethodStrategyExist(visitCard.address)
    }

    private fun isMethodStrategyExist(objectClz: Class<*>): Boolean {
        return this.isMethodStrategyExist(objectClz.name)
    }

    private fun isMethodStrategyExist(objectClzPath: String): Boolean {
        return methodStrategies.containsKey(objectClzPath)
    }

    private fun fetchFieldStrategy(visitCard: VisitCard<*>): List<FieldInfo>? {
        return this.fetchFieldStrategy(visitCard.address)
    }

    private fun fetchMethodStrategy(visitCard: VisitCard<*>): List<MethodInfo>? {
        return this.fetchMethodStrategy(visitCard.address)
    }

    private fun fetchFieldStrategy(objectClz: Class<*>): List<FieldInfo>? {
        return if (isFieldStrategyExist(objectClz)) fieldStrategies[objectClz.name] else genFieldStrategy(
            objectClz
        )
    }

    private fun fetchMethodStrategy(objectClz: Class<*>): List<MethodInfo>? {
        return if (isMethodStrategyExist(objectClz)) methodStrategies[objectClz.name] else genMethodStrategy(
            objectClz
        )
    }

    private fun needInherit(objectClz: Class<*>): Boolean {
        return objectClz.getAnnotation(PdInherit::class.java) != null
    }

    private fun findInherit(objectClz: Class<*>): PdInherit? {
        return objectClz.getAnnotation(PdInherit::class.java)
    }

    fun genFieldStrategy(objectClz: Class<*>): List<FieldInfo> {
        var temp: Class<*> = objectClz
        val strategy = ArrayList<FieldInfo>()
        var tag: String? = null

        while (true) {
            val list = listOf(*temp.declaredFields)
            for (i in list.indices) {
                val field = list[i]
                if (field.isAnnotationPresent(PointData::class.java)) {
                    val stateField = analyseField(field, tag)

                    if (stateField != null)
                        strategy.add(stateField)
                }
            }

            val pdInherit = findInherit(temp) ?: break

            temp = temp.superclass as Class<*>
            tag = pdInherit.superPrefix
        }


        fieldStrategies[objectClz.name] = strategy
        return strategy
    }

    private fun analyseField(field: Field, tag: String?): FieldInfo? {
        if (!field.isAnnotationPresent(PointData::class.java))
            return null
        var pointDataNotation: PointData? = null
        for (notation in field.declaredAnnotations) {
            if (notation.annotationClass.java == PointData::class.java) {
                pointDataNotation = notation as PointData
                break
            }
        }

        if (pointDataNotation == null)
            throw MException("check logic,PointData is not supposed to be null")

        val fieldName =
            if (pointDataNotation.fName.isEmpty()) field.name else pointDataNotation.fName


        var type = pointDataNotation.type

        if (type == Type.Infer)
            type = TypeInferUtils.infer(field)
        else if (type == Type.Object) {
            try {
                return analyseCustoms(field, pointDataNotation)
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
                throw MException(e.message)
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
                throw MException(e.message)
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
                throw MException(e.message)
            } catch (e: InstantiationException) {
                e.printStackTrace()
                throw MException(e.message)
            }

        } else {
            if (type.canBeChecked()) {
                val isCorrectType = type.check(field)
                if (!isCorrectType) {
                    logger.error("", "unCorrect Type set for$fieldName")
                    return FieldInfo(tag, fieldName, field, Type.Infer.fieldReader)
                }
            } else {
                logger.debug(
                    "", "you have notated " + fieldName + " as " + type.name +
                            ", one cannot be checked, with cautions"
                )
            }
        }

        return FieldInfo(tag, fieldName, field, type.fieldReader)
    }

    fun genMethodStrategy(objectClz: Class<*>): List<MethodInfo> {
        var temp: Class<*> = objectClz
        val strategy = ArrayList<MethodInfo>()
        var tag: String? = null

        while (true) {
            val list = listOf(*temp.declaredMethods)
            for (i in list.indices) {
                val method = list[i]
                if (method.genericParameterTypes.isNotEmpty()) {
                    logger.monitor("only support none param method, ignore $method")
                    continue
                }
                if (method.isAnnotationPresent(PointData::class.java)) {

                    if (method == null || method.returnType.toString().contains("void")) {
                        logger.error(
                            "",
                            "cannot support void or kotlin unit method, ignore $method"
                        )
                        continue
                    }


                    val methodInfo = analyseMethod(method, tag)

                    if (methodInfo != null)
                        strategy.add(methodInfo)
                }
            }

            val pdInherit = findInherit(temp) ?: break

            temp = temp.superclass as Class<*>
            tag = pdInherit.superPrefix
        }


        methodStrategies[objectClz.name] = strategy
        return strategy
    }

    private fun analyseMethod(method: Method, tag: String?): MethodInfo? {
        if (!method.isAnnotationPresent(PointData::class.java))
            return null
        var pointDataNotation: PointData? = null
        for (notation in method.declaredAnnotations) {
            if (notation.annotationClass.java == PointData::class.java) {
                pointDataNotation = notation as PointData
                break
            }
        }

        if (pointDataNotation == null)
            throw MException("check logic,PointData is not supposed to be null")

        val nameChunk = pointDataNotation.fName
        if (nameChunk.isEmpty()) {
            logger.error("", "must give fName for method ${method.name}")
            return null
        }

        var type = pointDataNotation.type

        if (type == Type.Infer)
            type = TypeInferUtils.inferMethod(method)


        return MethodInfo(tag, nameChunk, method, type.methodReader)
    }


    @Throws(
        NoSuchMethodException::class,
        IllegalAccessException::class,
        InvocationTargetException::class,
        InstantiationException::class
    )
    private fun analyseCustoms(field: Field, pointDataNotation: PointData): FieldInfo {
        val fieldName =
            if (pointDataNotation.fName.isEmpty()) field.name else pointDataNotation.fName

        return FieldInfo(null, fieldName, field, FieldReader.ObjectReader)
    }

    fun pressAllData(name: String, chunk: DataRepoChunk, data: Any) {
        fetchFieldStrategy(VisitCard.make(data))?.forEach {
            //如果是对象，会做透传代理
            try {
                it.fieldReader.read(name, it, data, chunk)
            } catch (e: RuntimeException) {
                logger.error("exception", e.localizedMessage)
            } catch (e: ReflectiveOperationException) {
                logger.error("exception", e.localizedMessage)
            }
        }

        fetchMethodStrategy(VisitCard.make(data))?.forEach {
            try {
                it.methodReader.read(name, it, data, chunk)
            } catch (e: RuntimeException) {
                logger.error("exception", e.localizedMessage)
            } catch (e: ReflectiveOperationException) {
                logger.error("exception", e.localizedMessage)
            }
        }
    }

    fun pressAllDataWithStrategy(name: String, chunk: DataRepoChunk, data: Any,visitCard: VisitCard<*>) {
        fetchFieldStrategy(visitCard)?.forEach {
            //如果是对象，会做透传代理
            try {
                it.fieldReader.read(name, it, data, chunk)
            } catch (e: RuntimeException) {
                logger.error("exception", e.localizedMessage)
            } catch (e: ReflectiveOperationException) {
                logger.error("exception", e.localizedMessage)
            }
        }

        fetchMethodStrategy(visitCard)?.forEach {
            try {
                it.methodReader.read(name, it, data, chunk)
            } catch (e: RuntimeException) {
                logger.error("exception", e.localizedMessage)
            } catch (e: ReflectiveOperationException) {
                logger.error("exception", e.localizedMessage)
            }
        }
    }


    fun handle(chunk: DataRepoChunk, config: PointDataConfig, allocator: Pair<String, String>) {
//        config.P
        //{
        //       "point":"真实点号"，
        //       "key":"unique key",
        //       "data":[
        //            {
        //                "p":"n.property",
        //                "n":"name"
        //            },
        //            ...
        //       ]，
        //       "group":"如果有必要的话，按照Activity分组可以减少寻找策略时的时间复杂度"
        //    }
        //这里我们使用的是将根数据存入chunk，按照配置要不停解析对应的内容
//        config.P?.takeIf { !config.N.isNullOrEmpty() && it.contains(".") }?.let {
//            var tmp: Any?
//            it.split(".").forEachIndexed { index, p ->
//                if (index > 0) {
//                    if (index == 1) {
//                        tmp = chunk.read(p)
//                    } else {
//                        tmp = tmp?.
//                    }
//                }
//            }
//        }

//        todo 2020年04月21日10:50:56 数据已经全部解析到chunk中了，按照config处理为pair
    }
}