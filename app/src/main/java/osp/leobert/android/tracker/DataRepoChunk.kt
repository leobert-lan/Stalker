package osp.leobert.android.tracker

import com.google.gson.Gson
import java.util.*

/**
 * <p><b>Package:</b> osp.leobert.android.burypoint </p>
 * <p><b>Project:</b> BuryPointContextDemo </p>
 * <p><b>Classname:</b> DataRepoChunk </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2020-04-01.
 */
class DataRepoChunk {

    companion object {
        val onUsing: Queue<DataRepoChunk> = LinkedList<DataRepoChunk>()
        val unoccupied: Queue<DataRepoChunk> = ArrayDeque<DataRepoChunk>()

        fun obtain(): DataRepoChunk {
            return (unoccupied.takeIf { it.isNotEmpty() }?.poll() ?: DataRepoChunk()).apply {
                onUsing.add(this)
            }
        }

        fun gc(element: DataRepoChunk) {
            element.clear()
            onUsing.remove(element)
            unoccupied.takeIf { it.size < 10 }?.add(element)
        }
    }

    val repo: MutableMap<String, in Any> = hashMapOf()

    fun put(key: String, obj: Any) {
        repo[key] = obj
    }

    fun read(key: String): Any? = repo[key]

    fun clear() {
        repo.clear()
    }

    fun debugRepo(): String = Gson().toJson(repo)

}