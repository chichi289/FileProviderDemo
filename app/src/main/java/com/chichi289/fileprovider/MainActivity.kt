package com.chichi289.fileprovider

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileWriter

// Ref:- https://medium.com/androiddevelopers/sharing-content-between-android-apps-2e6db9d1368b
// Ref:- https://youtu.be/C28pvd2plBA
// Ref:- https://speakerdeck.com/ianhanniballake/forget-the-storage-permission-alternatives-for-sharing-and-collaborating

class MainActivity : AppCompatActivity() {

    companion object {
        // same as the one defined in android:authorities in AndroidManifest.xml
        private const val FILE_AUTHORITY = "com.chichi289.fileprovider.fileprovider"
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // /data/user/0/com.chichi289.fileprovider/cache
        Log.e(TAG, "cacheDir.absolutePath: ${cacheDir.absolutePath}")

        val testFolder = File(cacheDir.absolutePath, "Test")

        if (!testFolder.exists()) {
            testFolder.mkdirs()
        }
        val file = File(testFolder, "test_file.txt")
        if (!file.exists()) file.createNewFile()

        val writer = FileWriter(file)
        writer.append("sBody")
        writer.flush()
        writer.close()

        // File path : /data/user/0/com.chichi289.fileprovider/cache/Test/test_file.txt
        Log.e(TAG, "File path : ${file.absolutePath}")

        /*
        cacheDir corresponds to the cache-path element in the provider definition.

        <cache-path
            name="secure_path_name"
            path="/Test" />

        URI content://com.chichi289.fileprovider.fileprovider/secure_path_name/test_file.txt

        <cache-path
            name="secure_path_name"
            path="." />

        URI content://com.chichi289.fileprovider.fileprovider/secure_path_name/Test/test_file.txt

        * */

        // get the URI for your file
        // do whatever you want with the URI
        val uri = FileProvider.getUriForFile(this, FILE_AUTHORITY, file)

        // show the uri in the UI
        val textView: TextView = findViewById(R.id.tvUri)
        textView.text = uri.toString()

        // We have uri : content://com.chichi289.fileprovider.fileprovider/app_a_cache_path/test_file.txt
        Log.e(TAG, "We have uri : $uri")


        findViewById<Button>(R.id.btnShare).setOnClickListener {

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "text/plain"
            }
            startActivity(sendIntent)

            // Use ShareCompat for better compatibility
            /*val intent = ShareCompat.IntentBuilder(this)
                .setType("text/plain")
                .setStream(uri)
                .setChooserTitle("Select your application")
                .intent

            intent.data = uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)*/
        }

    }
}