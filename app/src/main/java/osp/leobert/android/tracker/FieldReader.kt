package osp.leobert.android.tracker

import osp.leobert.android.tracker.model.FieldInfo

/**
 * <p><b>Package:</b> osp.leobert.android.burypoint </p>
 * <p><b>Project:</b> BuryPointContextDemo </p>
 * <p><b>Classname:</b> FieldReader </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2020-04-01.
 */
abstract class FieldReader {

    @Throws(IllegalAccessException::class, IllegalArgumentException::class)
    abstract fun read(
        namePrefix: String,
        fieldInfo: FieldInfo,
        obj: Any,
        chunk: DataRepoChunk
    )

    object IntReader : FieldReader() {
        override fun read(
            namePrefix: String,
            fieldInfo: FieldInfo,
            obj: Any,
            chunk: DataRepoChunk
        ) {
            fieldInfo.field.isAccessible = true
            chunk.put(fieldInfo.key(namePrefix), fieldInfo.field.getInt(obj).toString())
        }

    }

    object LongReader : FieldReader() {
        override fun read(
            namePrefix: String,
            fieldInfo: FieldInfo,
            obj: Any,
            chunk: DataRepoChunk
        ) {
            fieldInfo.field.isAccessible = true
            chunk.put(fieldInfo.key(namePrefix), fieldInfo.field.getLong(obj).toString())
        }
    }

    object ShortReader : FieldReader() {
        override fun read(
            namePrefix: String,
            fieldInfo: FieldInfo,
            obj: Any,
            chunk: DataRepoChunk
        ) {
            fieldInfo.field.isAccessible = true
            chunk.put(fieldInfo.key(namePrefix), fieldInfo.field.getShort(obj).toString())
        }
    }

    object StringReader : FieldReader() {
        override fun read(
            namePrefix: String,
            fieldInfo: FieldInfo,
            obj: Any,
            chunk: DataRepoChunk
        ) {
            fieldInfo.field.isAccessible = true
            chunk.put(fieldInfo.key(namePrefix), (fieldInfo.field.get(obj) ?: "null").toString())
        }
    }

    object FloatReader : FieldReader() {
        override fun read(
            namePrefix: String,
            fieldInfo: FieldInfo,
            obj: Any,
            chunk: DataRepoChunk
        ) {
            fieldInfo.field.isAccessible = true
            chunk.put(fieldInfo.key(namePrefix), fieldInfo.field.getFloat(obj).toString())
        }
    }

    object DoubleReader : FieldReader() {
        override fun read(
            namePrefix: String,
            fieldInfo: FieldInfo,
            obj: Any,
            chunk: DataRepoChunk
        ) {
            fieldInfo.field.isAccessible = true
            chunk.put(fieldInfo.key(namePrefix), fieldInfo.field.getDouble(obj).toString())
        }
    }

    object ByteReader : FieldReader() {
        override fun read(
            namePrefix: String,
            fieldInfo: FieldInfo,
            obj: Any,
            chunk: DataRepoChunk
        ) {
            fieldInfo.field.isAccessible = true
            chunk.put(fieldInfo.key(namePrefix), fieldInfo.field.getByte(obj).toString())
        }
    }

    object BooleanReader : FieldReader() {
        override fun read(
            namePrefix: String,
            fieldInfo: FieldInfo,
            obj: Any,
            chunk: DataRepoChunk
        ) {
            fieldInfo.field.isAccessible = true
            chunk.put(fieldInfo.key(namePrefix), fieldInfo.field.getBoolean(obj).toString())
        }
    }

    object CharReader : FieldReader() {
        override fun read(
            namePrefix: String,
            fieldInfo: FieldInfo,
            obj: Any,
            chunk: DataRepoChunk
        ) {
            fieldInfo.field.isAccessible = true
            chunk.put(fieldInfo.key(namePrefix), fieldInfo.field.getChar(obj).toString())
        }
    }

    object PrimitiveBoxedReader : FieldReader() {
        override fun read(
            namePrefix: String,
            fieldInfo: FieldInfo,
            obj: Any,
            chunk: DataRepoChunk
        ) {
            fieldInfo.field.isAccessible = true
            chunk.put(fieldInfo.key(namePrefix), (fieldInfo.field.get(obj) ?: "null").toString())
        }
    }

    object ObjectReader : FieldReader() {
        override fun read(
            namePrefix: String,
            fieldInfo: FieldInfo,
            obj: Any,
            chunk: DataRepoChunk
        ) {
            fieldInfo.field.isAccessible = true
            val delegate = fieldInfo.field.get(obj)

            if (delegate == null) {
                DataHandler.logger.monitor("ignore save delegate for null object");
            } else
                DataHandler.pressAllData(
                    "$namePrefix${DataHandler.separator}${fieldInfo.propertyName}",
                    chunk,
                    delegate
                )
        }
    }

    object DefaultReader : FieldReader() {
        override fun read(
            namePrefix: String,
            fieldInfo: FieldInfo,
            obj: Any,
            chunk: DataRepoChunk
        ) {
            //only out put log.
            DataHandler.logger.monitor("DefaultReader used $fieldInfo")
        }

    }

}