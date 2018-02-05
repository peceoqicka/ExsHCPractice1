package com.em.hcpractice1

/**
 * <pre>
 *      author  :   Acer
 *      e-mail  :   xx@xxx
 *      time    :   2018/2/1
 *      desc    :
 *      version :   1.0
 * </pre>
 */
data class PieData(var dataList: List<Data>) {
    data class Data(var name: String, var value: Float, var color: String)
}