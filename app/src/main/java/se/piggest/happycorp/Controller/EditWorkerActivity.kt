package se.piggest.happycorp.Controller

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import se.piggest.happycorp.Model.Worker
import se.piggest.happycorp.R
import se.piggest.happycorp.Services.DataService
import java.text.SimpleDateFormat
import java.util.*

//Here we can edit infromation about worker. Add new one or edit current worker
class EditWorkerActivity : AppCompatActivity() {

    lateinit var  avatarImageView: ImageView
    lateinit var  nameET: EditText
    lateinit var positionET: EditText
    lateinit var salaryET: EditText
    lateinit var dateOfEmploymentTV: TextView
    lateinit var getPhotoButton: Button
    lateinit var hireOrFireButton: Button
    lateinit var saveBtn: ImageButton
    lateinit var backBtn: ImageButton
    var isNewWorker = true
    var workerId = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_worker)

        setUpViewOnCreate()

        //setup buttons and view
        backBtn.setOnClickListener{
            finish()
        }

        hireOrFireButton.setOnClickListener {
            hireOrFireButtonPressed()
        }

        saveBtn.setOnClickListener(){
            saveInfoAboutWorker()
        }
    }


    private fun setUpViewOnCreate(){
        //This funv check if it edit or create new worker options and work accrodingly

        //get extra info from intent about is this new worker or idNumber of worker
        isNewWorker = intent.getBooleanExtra("isNewWorker", true)

        workerId = intent.getStringExtra("workerId")!!

        //setting up buttons and views
        avatarImageView = findViewById(R.id.edit_worker_avatar)
        nameET = findViewById(R.id.edit_worker_name)
        dateOfEmploymentTV = findViewById(R.id.edit_date_of_employment_text_view)
        positionET = findViewById(R.id.edit_position_edit_text)
        salaryET = findViewById(R.id.edit_salary_edit_text)
        getPhotoButton = findViewById(R.id.get_photo_btn)
        hireOrFireButton = findViewById(R.id.hire_or_fire_btn)
        saveBtn = findViewById(R.id.save_button_edit)
        backBtn = findViewById(R.id.edit_worker_backButton)

        //if new worker, all field should be clear otherwise we load info from worker file
        if (isNewWorker) {
            nameET.setText("")
            positionET.setText("")
            salaryET.setText("")
            hireOrFireButton.text = getString(R.string.hire)

            val currentDate = Date()
            val formattedDate = DataService.formattedDate(currentDate)

            dateOfEmploymentTV.text = "Date of employment: $formattedDate"

            saveBtn.isClickable = false
        } else {
            saveBtn.isClickable = true
            hireOrFireButton.text = getString(R.string.fire)
            val workerFile = DataService.findWorkerFileWith(workerId)

            if(workerFile != null) {
                val worker = DataService.convertJsonFromFile(workerFile) ?: return

                nameET.setText(worker.name)
                positionET.setText(worker.position)
                salaryET.setText(worker.salary.toString())
                dateOfEmploymentTV.text = DataService.formattedDate(worker.dateOfEmployment)
                DataService.getImgByName(worker.name, avatarImageView)
            } else {
                DataService.printToast("Cannot display info about worker")
                finish()
            }
        }
    }

    fun getPhotoBtnClicked(view: android.view.View) {

        //With help of api we load random generated avatar for worker based on Worker Name

        if (nameET.text.isNotEmpty()){

            val seed  = nameET.text
            val url = DataService.getImageUrlBy(seed.toString())
            avatarImageView.load(url){
                crossfade(true)
                placeholder(R.drawable.default_avatar)
                transformations(CircleCropTransformation())
            }
        }
        else{
            DataService.printToast("Please enter a name!")
        }
    }

    fun hireOrFireButtonPressed(){

        //if it is new worker we can hire him otherwise we will fire
        if(isNewWorker){
            if (isAllFieldsNotEmptyAndSalaryConvertToInt()){
                val newWorker = createNewWorkerObject()
                DataService.writeFileFor(newWorker){ succeed ->
                    if (succeed) {
                        finish()
                    }
                }
            } else {
                DataService.printToast("Please enter correct information inside of field(s)")
            }
        } else {
            if (workerId.isNotEmpty()){
                val workerId = workerId
                DataService.deleteWorkerWith(workerId)
                finish()
            } else {
                DataService.printToast("Worker id is empty")
            }
        }
    }

    fun saveInfoAboutWorker() {
        //just save new information about existance worker
        if(isAllFieldsNotEmptyAndSalaryConvertToInt()) {
          val newWorker = createNewWorkerObject()
            DataService.writeFileFor(newWorker){ succeed ->
               if (succeed)
                   DataService.printToast("Information successfully saved")
               else
                   DataService.printToast("Can`t save information in file")
            }
        }
    }

    fun isAllFieldsNotEmptyAndSalaryConvertToInt(): Boolean {
        if (nameET.text.isNotEmpty() && salaryET.text.isNotEmpty() && positionET.text.isNotEmpty() &&  salaryET.text.toString().toIntOrNull() != null){
            return true
        }
        return false
    }

    fun createNewWorkerObject(): Worker{

        //Create new worker accordingly model
        val workerId = workerId
        val workerName = nameET.text.toString()
        val workerSalary = salaryET.text.toString().toInt()
        val workerPosition = positionET.text.toString()
        val workerDateOfEmployment = Date()
        val newWorker = Worker(workerId, workerName, workerPosition, workerSalary, workerDateOfEmployment)

        return newWorker
    }

}