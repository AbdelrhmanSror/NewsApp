package com.example.newsApp.network

import android.net.Uri

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

/**
 * just simple class to make construct uri and fetching data easy for me
 */
class HttpLib private constructor() {
    private var builder: Uri.Builder? = null
    private var uri: Uri? = null
    //creating an object url from the Uri object
    //surrounding it with try catch block because Malformed exception may happen
    private val urlObject: URL?
        get() {
            var mUrl: URL? = null
            try {
                mUrl = URL(uri!!.toString())
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

            return mUrl
        }
    private//checking if the connection is successful
    val json: String
        get() {
            var httpURLConnection: HttpURLConnection? = null
            val inputStream: InputStream?
            val stringBuilder = StringBuilder()
            try {
                httpURLConnection = urlObject!!.openConnection() as HttpURLConnection
                httpURLConnection.requestMethod = "GET"
                httpURLConnection.connect()
                if (httpURLConnection.responseCode == 200) {
                    inputStream = httpURLConnection.inputStream
                    if (inputStream != null) {
                        val bufferedReader = BufferedReader(InputStreamReader(inputStream, Charset.forName("UTF-8")))
                        var read: String? = bufferedReader.readLine()
                        while (read != null) {
                            stringBuilder.append(read)
                            read = bufferedReader.readLine()
                        }
                        inputStream.close()

                    }

                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                httpURLConnection?.disconnect()

            }
            return stringBuilder.toString()

        }

    class HttpBuilder {
        private val httpLib = HttpLib()

        fun addUrl(url: String): HttpBuilder {
            httpLib.builder = Uri.parse(url).buildUpon()
            return this

        }

        fun build(): HttpLib? {
            if (httpLib.builder == null) {
                return null
            }
            httpLib.uri = httpLib.builder!!.build()
            return httpLib
        }

        fun addQuery(name: String, value: String): HttpBuilder {
            httpLib.builder!!.appendQueryParameter(name, value)
            return this

        }

        fun addPath(name: String): HttpBuilder {
            httpLib.builder!!.appendPath(name)
            return this

        }

    }

    fun startFetching(): String {
        return json
    }
}