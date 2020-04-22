package osp.leobert.android.burypointcontextdemo

import org.junit.Assert.assertEquals
import org.junit.Test
import osp.leobert.android.tracker.notation.PointData

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    class Foo {
        @field:PointData(fName = "AAA")
        val a: Int = 1
    }

//    @Test
//    fun aaa() {
//        val chunk = DataRepoChunk()
//        DataHandler.pressAllData(chunk, Foo())
//        chunk.read("a")
//    }
}
