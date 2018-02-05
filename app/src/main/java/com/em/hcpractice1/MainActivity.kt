package com.em.hcpractice1

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import com.em.hcpractice1.view.*
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.find
import java.io.InputStreamReader
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    private val viewList = ArrayList<View>()
    private lateinit var pieChartView: PieChartView
    private lateinit var histogramView: HistogramView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        val tabsTitle = resources.getStringArray(R.array.tabs_pra)

        val tabLayout = find<TabLayout>(R.id.tab_layout).also { tab ->
            tab.tabMode = TabLayout.MODE_SCROLLABLE
        }

        val viewpager = find<ViewPager>(R.id.viewpager)

        with(viewList) {
            add(ColorView(this@MainActivity))
            add(CircleView(this@MainActivity))
            add(PathView(this@MainActivity))
            histogramView = HistogramView(this@MainActivity)
            add(histogramView)
            pieChartView = PieChartView(this@MainActivity)
            add(pieChartView)
        }


        val pagerAdapter = MyAdapter(tabsTitle, viewList)
        viewpager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewpager)

        Observable.create<String> { o ->
            with(this@MainActivity.resources.assets.
                    open("data.json")) {
                val reader = InputStreamReader(this,
                        Charset.forName("UTF-8"))
                val json = reader.readText()
                reader.close()
                o.onNext(json)
            }
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { json ->
                    val pieData = Gson().fromJson(json,
                            PieData::class.java)
                    histogramView.adapter = HistogramAdapter(pieData)
                    pieChartView.adapter = PieChartAdapter(pieData)
                }
    }

    private class MyAdapter(val titles: Array<String>, val viewList: List<View>) : PagerAdapter() {
        override fun getCount(): Int = titles.size
        override fun getPageTitle(position: Int): CharSequence = titles[position]
        override fun isViewFromObject(view: View, obj: Any) = view == obj

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            return viewList[position].also { v -> container.addView(v) }
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            container.removeView(viewList[position])
        }
    }

    private class PieChartAdapter(val data: PieData) : PieChartView.Adapter() {
        override fun getItemCount() = data.dataList.size

        override fun getName(position: Int)
                = data.dataList[position].name

        override fun getValue(position: Int)
                = data.dataList[position].value

        override fun getColor(position: Int)
                = Color.parseColor(data.dataList[position].color)
    }

    private class HistogramAdapter(val data: PieData) : HistogramView.Adapter() {
        override fun getItemCount() = data.dataList.size

        override fun getName(position: Int)
                = data.dataList[position].name

        override fun getValue(position: Int)
                = data.dataList[position].value
    }
}
