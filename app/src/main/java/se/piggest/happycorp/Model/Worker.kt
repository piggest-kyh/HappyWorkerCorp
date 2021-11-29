package se.piggest.happycorp.Model

import se.piggest.happycorp.Services.DataService
import java.util.*

class Worker(val id: String, val name:String, val position:String, val salary: Int, val dateOfEmployment: Date) {

    //Worker model with singleton func
    companion object {

        fun calculateNumberOfWorkers(): Int {
            return DataService.workerList.size
        }
        fun calculateAverageSalary(): Int {
            var salarySum = 0
            return if (!DataService.workerList.isEmpty()) {
                DataService.workerList.forEach {
                    salarySum += it.salary
                }
                salarySum / calculateNumberOfWorkers()
            } else {
                0
            }

        }

    }
}