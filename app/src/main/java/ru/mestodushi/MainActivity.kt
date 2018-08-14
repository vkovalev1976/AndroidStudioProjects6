package ru.mestodushi

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DecorContentParent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

   lateinit var vText:TextView
   lateinit var vList:LinearLayout
   lateinit var vListView:ListView
    var request: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        vList = findViewById<LinearLayout>(R.id.act1_list)
        vListView = findViewById<ListView>(R.id.act1_listView)
//        vText = findViewById<TextView>(R.id.act1_text)
//        vText.setTextColor(0xFFFF0000.toInt())
//        vText.setOnClickListener {
//            Log.e("tag", "НАЖАТА КНОПКА!!!")
//            val i=Intent(this, SecondActivity::class.java)
//            i.putExtra("tag1",vText.text)
//            startActivityForResult(i,0)

            val o= createRequest("https://api.rss2json.com/v1/api.json?rss_url=http%3A%2F%2Ffeeds.bbci.co.uk%2Fnews%2Frss.xml")
                    .map{ Gson().fromJson(it,Feed::class.java) }
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

            request=o.subscribe({

//                showLinearLayout(it.items)
                showListView(it.items)
//               for (item in it.items)
//                   Log.w("test", "title: ${item.title}")
            },{
                Log.e("test", "",it)

            })
//        }

//        Log.v("tag", "Был запушен onCreate")
    }

    fun showLinearLayout(feedList: ArrayList<FeedItem>) {

        val inflater = layoutInflater
        for (f in feedList) {
            val view = inflater.inflate(R.layout.list_item, vList,false)
            val vTitle=view.findViewById<TextView>(R.id.item_title)
            vTitle.text=f.title
            vList.addView(view)

        }
    }

    fun showListView(feedList: ArrayList<FeedItem>){
       vListView.adapter= Adapter(feedList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data!=null){
           val str=data.getStringExtra("tag2")

            vText.text=str
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        request?.dispose()
        super.onDestroy()
    }
}

class Feed(
        val items:ArrayList<FeedItem>
)

class FeedItem(
        val title:String,
        val link:String,
        val thumbnail:String,
        val description:String
)

class Adapter(val items:ArrayList<FeedItem>):BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val inflater = LayoutInflater.from(parent!!.context)

        val view = convertView ?: inflater. inflate(R.layout.list_item, parent,false)
        val vTitle = view.findViewById<TextView>(R.id.item_title)

        val item = getItem(position) as FeedItem

        vTitle.text = item.title

        return view
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

}

/*https://api.rss2json.com/v1/api.json?rss_url=http%3A%2F%2Ffeeds.bbci.co.uk%2Fnews%2Frss.xml
{
"items": [{
        "title": "UK pledges extra \u00a344m for Channel border security",
        "pubDate": "2018-01-18 00:06:16",
        "Link": "http:\/\/www.bbc.co.uk\/news\/uk-politics-42723401",
        "guid": "http:\/\/www.bbc.co.uk\/news\/uk-politics-42723401",
        "author": "",
        "thumbnail": "http:\/\/c.files.bbci.co.uk\/8542\/production\/_99641143_gettyimages-531721098.jpg",
        "description": "The money will be spent on fencing, CCTV and infrared detection technology, the UK government ...",
        "content": "The money will be spent on fencing, CCTV and infrared detection technology, the UK government will...",
        "enclosure": {
            "thumbnail": "http:\/\/c.files.bbci.co.uk\/8542\/production\/_99641143_gettyimages-531721098.jpg"
            },
            "categories": []
      }
 }
 */