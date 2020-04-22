package osp.leobert.android.burypoint

import osp.leobert.android.burypoint.model.MethodInfo

/**
 * <p><b>Package:</b> osp.leobert.android.burypoint </p>
 * <p><b>Project:</b> BuryPointContextDemo </p>
 * <p><b>Classname:</b> MethodReader </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2020/4/21.
 */
abstract class MethodReader {
    @Throws(RuntimeException::class, ReflectiveOperationException::class)
    abstract fun read(
        namePrefix: String,
        methodInfo: MethodInfo,
        obj: Any,
        chunk: DataRepoChunk
    )

    object PrimitiveReader : MethodReader() {
        override fun read(
            namePrefix: String,
            methodInfo: MethodInfo,
            obj: Any,
            chunk: DataRepoChunk
        ) {
            methodInfo.method.isAccessible = true

            chunk.put(
                methodInfo.key(namePrefix),
                (methodInfo.method.invoke(obj) ?: "null").toString()
            )
        }
    }
    //其他的都不考虑

    object DefaultReader : MethodReader() {
        override fun read(
            namePrefix: String,
            methodInfo: MethodInfo,
            obj: Any,
            chunk: DataRepoChunk
        ) {
            //only out put log.
            DataHandler.logger.monitor("DefaultReader used $methodInfo")
        }
    }


}