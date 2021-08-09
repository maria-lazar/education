package com.marialazar.petadoption

import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.marialazar.petadoption.core.TAG


class MainActivity : AppCompatActivity() {
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var connectivityLiveData: ConnectivityLiveData

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        
        val networkIcon = findViewById<ImageView>(R.id.network_icon)
        connectivityManager = getSystemService(android.net.ConnectivityManager::class.java)
        connectivityLiveData = ConnectivityLiveData(connectivityManager)
        connectivityLiveData.observe(this) {
            Log.d(TAG, "connectivityLiveData $it")
            if (it) {
                networkIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_network
                    )
                );
            } else {
                networkIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_network_not
                    )
                );
            }
        }
        Log.i(TAG, "onCreate")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}