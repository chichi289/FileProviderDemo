package com.chichi289.receiverapp

import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ReceiverApp"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*if (intent?.action == Intent.ACTION_SEND) {
            if ("text/plain" == intent.type) {
                intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                    // Update UI to reflect text being shared
                    findViewById<TextView>(R.id.tvName).text = it
                }
            }
        }*/

        findViewById<TextView>(R.id.tvName).text = intent.data.toString()
        findViewById<TextView>(R.id.tvName).text = intent.data.toString()
        Log.e(TAG, "intent.data: ${intent.data}")
        /*
         * Get the file's content URI from the incoming Intent, then
         * get the file's MIME type
         */
        val mimeType: String? = intent.data?.let { returnUri ->
            contentResolver.getType(returnUri)
        }
        Log.e(TAG, "mimeType: $mimeType")

        /*
     * Get the file's content URI from the incoming Intent,
     * then query the server app to get the file's display name
     * and size.
     */
        ShareCompat.IntentReader(this).stream?.let { returnUri ->
            contentResolver.query(returnUri, null, null, null, null)
        }?.use { cursor ->
            /*
             * Get the column indexes of the data in the Cursor,
             * move to the first row in the Cursor, get the data,
             * and display it.
             */
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
            findViewById<TextView>(R.id.tvName).text = cursor.getString(nameIndex)
            findViewById<TextView>(R.id.tvSize).text = cursor.getLong(sizeIndex).toString()

        }

    }
}