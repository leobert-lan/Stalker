package osp.leobert.android.burypointcontextdemo;

import osp.leobert.android.burypoint.notation.PdInherit;
import osp.leobert.android.burypoint.notation.PointData;

/**
 * <p><b>Package:</b> osp.leobert.android.burypointcontextdemo </p>
 * <p><b>Project:</b> BuryPointContextDemo </p>
 * <p><b>Classname:</b> JFoo </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2020/4/21.
 */


public class JFoo {
    @PointData(fName = "jA")
    public Integer a = 2;

    @PointData(fName = "jL")
    private Long getLong() {
        return 3L;
    }

    @PointData(fName = "onlySuper")
    public final String testOnlySuper() {
        return "3L";
    }

    @PointData(fName = "jB")
    public String b = "JFoo";

    @PdInherit(superPrefix = "JFOO")
    public static class JBar extends JFoo {
        @PointData(fName = "jB")
        public String b = "JBar";

        @PointData(fName = "jL")
//        @Override
        private Long getLong() {
            return 4L;
        }

        @PointData(fName = "err1")
        public void testVoid() {

        }

        @PointData(fName = "err2")
        public Integer test(int a) {
            return 4 + a;
        }

        @PointData(fName = "err3")
        public Integer test3() {
            throw new RuntimeException();
        }

    }
}
