package se.piggest.happycorp.Controller

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.util.CoilUtils
import okhttp3.OkHttpClient
import se.piggest.happycorp.Services.DataService

import se.piggest.happycorp.Services.DataService.context
import java.io.File

class App : Application() {

    //special class for getting context from everywhere in app by introduce instance of app
    override fun onCreate() {
        super.onCreate()
        instance = this

        // initiate image loader with help of Coil Framework
        val imageLoader = ImageLoader.Builder(instance)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(instance))
                    .build()
            }
            .componentRegistry {
                add(SvgDecoder(instance))
            }
            .build()
        Coil.setImageLoader(imageLoader)

        val workersFolder = File(instance.filesDir, "workers")

        checkIfDirectoryExistsAndCreate(workersFolder)

        DataService.getWorkerFilesList()

    }

    private fun checkIfDirectoryExistsAndCreate(dir: File) {
        if (!dir.exists()) {
            dir.mkdir()
        }
    }

    companion object {
        lateinit var instance: App
            private set
    }
}