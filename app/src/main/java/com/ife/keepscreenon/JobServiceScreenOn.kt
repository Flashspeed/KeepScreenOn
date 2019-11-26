package com.ife.keepscreenon

import android.app.job.JobParameters
import android.app.job.JobService

class JobServiceScreenOn: JobService() {

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        return true
    }

}