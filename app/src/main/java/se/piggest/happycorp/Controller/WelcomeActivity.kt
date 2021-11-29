package se.piggest.happycorp.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import se.piggest.happycorp.Model.Worker
import se.piggest.happycorp.R
import se.piggest.happycorp.Services.DataService
import java.util.*

//This is the welcome screen where magic begin
class WelcomeActivity : AppCompatActivity() {

    lateinit var numberOfWorkersTV: TextView
    lateinit var  avarageSalaryTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberOfWorkersTV = findViewById(R.id.main_workers_counter)
        avarageSalaryTV = findViewById(R.id.main_average_salary)

        setUpWorkersInfo()
    }

    override fun onResume() {
        super.onResume()

        setUpWorkersInfo()
    }

    fun setUpWorkersInfo(){
        //in this place we calculate and show  number of workers and avarage salary
        numberOfWorkersTV.text = getString(R.string.number_of_workers, Worker.calculateNumberOfWorkers())
        val averageSalary = Worker.calculateAverageSalary()
        avarageSalaryTV.text = getString(R.string.average_salary, averageSalary)
    }

    fun newWorkerBtnClicked(view: android.view.View) {
        //Go to hire new worker screen button
        val workerId = UUID.randomUUID().toString()
        Intent(this, EditWorkerActivity::class.java)
            .putExtra("isNewWorker", true)
            .putExtra("workerId", workerId)
            .apply { startActivity(this) }
    }

    fun workerListBtnClicked(view: android.view.View) {
        //Just open list of workers for managment
        Intent(this, WorkerListActivity::class.java).apply { startActivity(this) }
    }
}