package se.piggest.happycorp.Services

import android.widget.ImageView
import android.widget.Toast
import coil.load
import coil.transform.CircleCropTransformation
import com.google.gson.Gson
import se.piggest.happycorp.Controller.App
import se.piggest.happycorp.Model.Worker
import se.piggest.happycorp.R
import java.io.File
import java.io.PrintWriter
import java.lang.Exception
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

object DataService {


    //file for working with information about worker
    val context = App.instance
    var workerList: MutableList<Worker> = mutableListOf()
    fun printToast( message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).apply { show() }
    }

    fun getImageUrlBy(seed: String): String {
        //generate uniq url
        return "https://avatars.dicebear.com/api/adventurer/$seed.svg"
    }

    fun getImgByName(name: String, avatarImage: ImageView) {
        //getting image from API by given URL
        val url = getImageUrlBy(name)

        avatarImage.load(url){
            crossfade(true)
            placeholder(R.drawable.default_avatar)
            transformations(CircleCropTransformation())
        }
    }

    fun getWorkerFilesList() {
        //gettin worker list from file which keeps localy on device
        val workerDir = getWorkersDir()
        workerDir.listFiles()?.forEach {
            try {
                val newWorker  = convertJsonFromFile(it)
                if (newWorker != null)
                    workerList.add(newWorker)
            } catch (e:Exception) {
                printToast("error while adding worker to list" + e.localizedMessage)
            }
        }
        workerList.sortWith(compareBy { it.name })
    }

    fun writeFileFor(worker: Worker, succeed: (Boolean) -> Unit) {
        //Convert worker model to JSON String and then write in file
        val workerId = worker.id
        val jsonWorker = Gson().toJson(worker)
        val workerFile = File(getWorkersDir(), workerId)
        val writer = PrintWriter(workerFile)

        try {
            writer.write(jsonWorker)
            writer.close()
            printToast("File saved successfully")
            succeed(true)
        } catch (e: Exception){
            printToast("Something goes wrong " + e.localizedMessage)
            succeed(false)
        }
    }

    fun deleteWorkerWith(id: String) {
        //delete worker with given id
        try {
            val workerFile = findWorkerFileWith(id)
            workerFile!!.delete()
        } catch (e: Exception){
            printToast("Error while firing worker" + e.localizedMessage)
        }
    }

    fun convertJsonFromFile(workerFile: File): Worker? {
        //in this func we take json string  file and return Worker object
        var worker: Worker? = null
        val scanner = Scanner(workerFile)
        val sb = StringBuilder()
        while (scanner.hasNextLine()){
            sb.append(scanner.nextLine() )
        }


        try {
            val stringFromFile = sb.toString()
            worker = Gson().fromJson(stringFromFile, Worker::class.java)
        } catch (e: Exception) {
            printToast("Error while convert from JSON" + e.localizedMessage)
        }
        return worker
    }

    fun getWorkersDir() : File {
        return File(App.instance.filesDir, "workers")
    }

    private fun checkIfDirectoryExistsAndCreate(dir: File) {
        if (!dir.exists()) {
            dir.mkdir()
        }
    }

    fun formattedDate(date: Date): String {
        //Date formatter for convert System Date to Readable date
        val format = SimpleDateFormat("yyyy/MM/dd")
        val formattedDate = format.format(date)
        return formattedDate
    }

    fun findWorkerFileWith(id: String): File?{
        //Find worker file by id
        return try {
            File(getWorkersDir(), id)
        } catch (e: Exception) {
            printToast("Error while getting worker file")
            null
        }
    }

}


