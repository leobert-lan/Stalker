package osp.leobert.android.burypoint

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
    }

    val repo: MutableMap<String, in Any> = hashMapOf()

    fun put(key: String, obj: Any) {
        repo[key] = obj
    }

    fun read(key: String): Any? = repo[key]

    fun clear() {
        repo.clear()
    }

    fun debugRepo():String  = Gson().toJson(repo)

}