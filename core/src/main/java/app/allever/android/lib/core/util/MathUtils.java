package app.allever.android.lib.core.util;

import android.graphics.PointF;

/**
 * @author allever
 */
public class MathUtils {
    /***
     * 两点距离
     * @param pointA
     * @param pointB
     * @return
     */
    public static float lineLength(PointF pointA, PointF pointB) {
        double absX = Math.abs(pointA.x - pointB.x);
        double absY = Math.abs(pointA.y - pointB.y);
        return (float) Math.sqrt(absX * absX + absY * absY);
    }

    /***
     * 求角 BAC或角 CAB的余弦值
     * @param pointB
     * @param pointA
     * @param pointC
     * @return
     */
    public static float cos(PointF pointB, PointF pointA, PointF pointC) {
        //两点距离公式
        float abLength = MathUtils.lineLength(pointA, pointB);
        float acLength = MathUtils.lineLength(pointA, pointC);
        float bcLength = MathUtils.lineLength(pointB, pointC);

        float result = (abLength * abLength + acLength * acLength - bcLength * bcLength) / (2 * abLength * acLength);
        return result;
    }

    /***
     * 求 x1, x2 为顶点的角的角度
     * @param p1x
     * @param p1y
     * @param p2x
     * @param p2y
     * @param p3x
     * @param p3y
     * @return 角度
     */
    public static int getAngle(float p1x, float p1y, float p2x, float p2y, float p3x, float p3y) {
        //向量的点乘
        float t = (p2x - p1x) * (p3x - p1x) + (p2y - p1y) * (p3y - p1y);
        //为了精确直接使用而不使用中间变量
        //包含了步骤：A=向量的点乘/向量的模相乘
        //          B=arccos(A)，用反余弦求出弧度
        //          result=180*B/π 弧度转角度制
        int result = (int) (180 * Math.acos(
                t / Math.sqrt
                        ((Math.abs((p2x - p1x) * (p2x - p1x)) + Math.abs((p2y - p1y) * (p2y - p1y)))
                                * (Math.abs((p3x - p1x) * (p3x - p1x)) + Math.abs((p3y - p1y) * (p3y - p1y)))
                        ))
                / Math.PI);
        //      pi   = 180
        //      x    =  ？
        //====> ?=180*x/pi
        return result;
    }


    /**
     * 四舍五入
     *
     * @param value
     * @return
     */
    public static int round(float value) {
        return (int) Math.round(value);
    }

    public static int round(double value) {
        return (int) Math.round(value);
    }

    public static int round(int value) {
        return (int) Math.round(value);
    }
}
