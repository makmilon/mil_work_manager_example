package com.example.workmanager

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val request = OneTimeWorkRequestBuilder<MyWorker>().build()
        WorkManager.getInstance(this).enqueue(request)

    }


    // Define a worker class that extends the Worker class
    class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

        override fun doWork(): Result {
            // Make the API request using Volley
            val queue = Volley.newRequestQueue(applicationContext)
            val url = "https://jsonplaceholder.typicode.com/posts/1"
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    // Handle the response
                    Log.d("MyWorker", "API response: $response")
                    Toast.makeText(applicationContext, "Hi Milon", Toast.LENGTH_SHORT).show()
                },
                { error ->
                    // Handle the error
                    Log.e("MyWorker", "API error: ${error.message}")
                }
            )
            queue.add(stringRequest)

            // Reschedule the worker to run again after a certain period of time
            val delay = 60L // 30 seconds
            val request = OneTimeWorkRequestBuilder<MyWorker>()
                .setInitialDelay(delay, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(applicationContext).enqueue(request)

            // Return success
            return Result.success()
        }
    }


}