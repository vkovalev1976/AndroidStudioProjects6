package ru.mestodushi

import io.reactivex.Observable
import java.net.HttpURLConnection
import java.net.URL

/**
*Created by Slava on 10.08.2018.
*/

fun createRequest(url:String)= Observable.create<String> {
    val urlConnection = URL(url).openConnection() as HttpURLConnection
    try {
        urlConnection.connect()

        if (urlConnection.responseCode != HttpURLConnection.HTTP_OK)
            it.onError(RuntimeException(urlConnection.responseMessage))
        else {
            val str = urlConnection.inputStream.bufferedReader().readText()
            it.onNext(str)
        }
    } finally {
        urlConnection.disconnect()
    }
}