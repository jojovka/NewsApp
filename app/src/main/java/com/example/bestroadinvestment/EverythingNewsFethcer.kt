package com.example.bestroadinvestment

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import java.net.URL
import javax.net.ssl.HttpsURLConnection

@Suppress("DEPRECATION")

interface EverythingNewsFethcer {

    fun whenEverythingNewsFetchedSuccessfully(articles: List<Article>?)

    fun whenNewsFetchedOnError(error: String?)

}

@Suppress("DEPRECATION")
class EverythingFetchingAsyncTask(
        private val q: String? = null,
        private val page: Int = 1,
        private val newsFetchedListener: EverythingFragment? = null
) : AsyncTask<String, String, Unit>() {

    private fun sendGet(url: String) {
        val connection = URL(url).openConnection() as HttpsURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("User-Agent", "Mozilla/5.0")
        connection.connect()
        val responseCode = connection.responseCode
        val responseMessage = connection.responseMessage
        if (responseCode !in 200..299) {
            return
        }
        connection.inputStream.bufferedReader().use { reader ->
            val response = StringBuffer()
            val iterator = reader.lineSequence().iterator()
            while (iterator.hasNext()) {
                val line = iterator.next()
                response.append(line)
            }
            reader.close()
            this.parseReturnedJsonData(response.toString())
        }
    }


    override fun doInBackground(vararg p0: String?) {
        val myurl =
                "https://newsapi.org/v2/everything?q=covid&apiKey=e65ee0938a2a43ebb15923b48faed18d&page=$page"
        this.sendGet(myurl)
    }

    override fun onPostExecute(result: Unit?) {

    }

    private fun parseReturnedJsonData(s: String) {
        val p = Gson()
        val rt = p.fromJson(s, NewsResult::class.java) ?: return

        if (rt.status == "ok") {
            Handler(Looper.getMainLooper()).post {
                newsFetchedListener?.whenEverythingNewsFetchedSuccessfully(rt.articles)
            }
        } else {
            Handler(Looper.getMainLooper()).post {
                newsFetchedListener?.whenNewsFetchedOnError("Error")
            }
        }
    }

}
