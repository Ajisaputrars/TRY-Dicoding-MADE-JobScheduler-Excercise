package com.example.ajisaputrars.myjobscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnStart, btnCancel;
    Switch switchTest;
    private int jobId = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button)findViewById(R.id.btn_start);
        btnCancel = (Button)findViewById(R.id.btn_cancel);
        switchTest = findViewById(R.id.switch_test);

        btnStart.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

//        switchTest.setChecked(false);
        isJobIdRunning(this, jobId);

        switchTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
//                    Toast.makeText(getApplicationContext(), "Checked Now", Toast.LENGTH_SHORT).show();
                    startJob();
                } else {
                    // The toggle is disabled
                    cancelJob();
                }
            }
        });

    }

    private boolean isJobIdRunning( Context context, int JobId) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;
        for ( JobInfo jobInfo : jobScheduler.getAllPendingJobs() ) {
            if ( jobInfo.getId() == JobId ) {
                Log.d("jobInfo", "Ada nih ");
                switchTest.setChecked(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                startJob();
                break;
            case R.id.btn_cancel:
                cancelJob();
                break;
        }
    }

    private void startJob(){
        ComponentName serviceComponent = new ComponentName(this, GetCurrentWeatherJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, serviceComponent);

        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setRequiresDeviceIdle(false);
        builder.setRequiresCharging(false);
        builder.setPeriodic(7000); // 1000 ms = 1 detik

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
        Toast.makeText(this, "Job Service started", Toast.LENGTH_SHORT).show();
    }
    private void cancelJob(){
        JobScheduler tm = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.cancel(jobId);
        Toast.makeText(this, "Job Service canceled", Toast.LENGTH_SHORT).show();
//        finish();
    }
}
