package osp.leobert.android.burypointcontextdemo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import osp.leobert.android.tracker.DataHandler
import osp.leobert.android.tracker.DataRepoChunk
import osp.leobert.android.tracker.model.VisitCard
import osp.leobert.android.tracker.notation.PointData
import osp.leobert.android.tracker.type.Type

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DataHandler.logger.showLog(true)

        findViewById<View>(R.id.a).setOnClickListener {

            main()
        }
    }

    interface I {
        val i: Int
    }

    class Foo : I {
        @field:PointData(fName = "AAA")
        val a: Int = 1

        @field:PointData(fName = "bbb")
        val b: Boolean = true

        @field:PointData(fName = "ccc")
        val c: Long? = null

        @field:PointData(fName = "bar", type = Type.Object)
        val bar: Bar = Bar()

        @get:PointData(fName = "i")
        override val i: Int
            get() = 2


        override fun toString(): String {
            return "Foo(a=$a, b=$b, c=$c, bar=$bar)"
        }
    }

    class Bar {
        @field:PointData(fName = "AAA")
        val a: Int = 1

        @field:PointData(fName = "bbb")
        val b: Boolean = true

        @field:PointData(fName = "ccc")
        val c: Long? = null
        override fun toString(): String {
            return "Bar(a=$a, b=$b, c=$c)"
        }
    }

    class IImpl : I {
        @get:PointData(fName = "i")
        override val i: Int
            get() = 2

        @PointData(fName = "errUnit")
        fun testUnit() {

        }
    }

    fun main() {
//        val a = DataHandler.genFieldStrategy(VisitCard.make(JFoo()).address)
//
//        val i = DataHandler.genMethodStrategy(VisitCard.make(IImpl()).address)


        val chunk = DataRepoChunk()
        DataHandler.pressAllData("foo", chunk, Foo())
        DataHandler.pressAllData("jFoo", chunk, JFoo())
        Log.e("lmsg", chunk.debugRepo())

        val chunki = DataRepoChunk()
        DataHandler.pressAllData("i", chunki, IImpl())
        Log.e("lmsg", chunki.debugRepo())

        val chunkJF = DataRepoChunk()
        DataHandler.pressAllData("jFoo", chunkJF, JFoo())
        Log.e("lmsg", chunkJF.debugRepo())

        val chunkJB = DataRepoChunk()
        DataHandler.pressAllData("jBar", chunkJB, JFoo.JBar())
        Log.e("lmsg", chunkJB.debugRepo())


        //如果说因为某些业务需求，需要继承并新写数据类，且只改变一些业务逻辑，与埋点没有关系时，可以指定原先的类
        val chunkJB2 = DataRepoChunk()
        val jbar2 = JFoo.JBar()
        DataHandler.pressAllDataWithStrategy(
            "jBar2", chunkJB2, jbar2, VisitCard.make(JFoo::class.java, jbar2)
        )
        Log.e("lmsg", chunkJB2.debugRepo())
    }
}

