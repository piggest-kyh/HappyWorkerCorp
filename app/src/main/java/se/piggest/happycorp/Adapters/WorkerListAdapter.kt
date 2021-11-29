package se.piggest.happycorp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import coil.load
import se.piggest.happycorp.Model.Worker
import se.piggest.happycorp.R
import se.piggest.happycorp.Services.DataService

class WorkerListAdapter(val context: Context, val workers: List<Worker>, val itemClick: (Worker)->Unit): Adapter<WorkerListAdapter.WorkerHolder>() {
//Custom adapter for working list
        inner class WorkerHolder(itemView: View,  val itemClick: (Worker) -> Unit): RecyclerView.ViewHolder(itemView) {
            val workerName = itemView.findViewById<TextView>(R.id.worker_name)
            val workerDateOfEmployment = itemView.findViewById<TextView>(R.id.worker_date_of_employament)
            val workerPosition = itemView.findViewById<TextView>(R.id.worker_position)
            val workerSalary = itemView.findViewById<TextView>(R.id.worker_salary)
            val workerAvatar = itemView.findViewById<ImageView>(R.id.worker_avatar)

            fun bindWorker(worker: Worker) {
                workerName.text = worker.name
                workerPosition.text = worker.position
                workerSalary.text = "$ ${worker.salary}"
                workerDateOfEmployment.text = DataService.formattedDate(worker.dateOfEmployment)
                //Getting image from api for every worker
                DataService.getImgByName(worker.name, workerAvatar)
//                val url = DataService.getImageUrlBy(workerName.text.toString())
//                workerAvatar.load(url){
//                    crossfade(true)
//                    placeholder(R.drawable.default_avatar)
//                    transformations(coil.transform.CircleCropTransformation())
//                }

                itemView.setOnClickListener{itemClick(worker)}
            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.worker_list_item, parent, false)
        return WorkerHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: WorkerHolder, position: Int) {
        holder.bindWorker(workers[position])
    }

    override fun getItemCount(): Int {
        return workers.count()
    }

}
