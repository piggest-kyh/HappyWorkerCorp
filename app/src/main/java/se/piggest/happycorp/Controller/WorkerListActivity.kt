package se.piggest.happycorp.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.piggest.happycorp.Adapters.WorkerListAdapter
import se.piggest.happycorp.R
import se.piggest.happycorp.Services.DataService

class WorkerListActivity : AppCompatActivity() {

    //Show worker list and update accordingly

    lateinit var backButton:ImageButton
    lateinit var adapter: WorkerListAdapter
    lateinit var workerListView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_list)

        adapter = WorkerListAdapter(this, DataService.workerList) {
            Intent(this, EditWorkerActivity::class.java)
                .putExtra("isNewWorker", false)
                .putExtra("workerId", it.id)
                .apply { startActivity(this) }
        }

        workerListView = findViewById<RecyclerView>(R.id.worker_list)
        workerListView.adapter = adapter
        workerListView.layoutManager = LinearLayoutManager(this)

        backButton = findViewById(R.id.back_button_worker_list)
        backButton.setOnClickListener{
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        DataService.workerList.clear()
        DataService.getWorkerFilesList()
        adapter.notifyDataSetChanged()
    }
}