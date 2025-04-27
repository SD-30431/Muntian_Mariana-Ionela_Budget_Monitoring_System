package com.example.demo.BusinessLogic;

import com.example.demo.Model.ActivityLog;
import com.example.demo.Repository.ActivityLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    public ActivityLog saveActivity(String username, String action) {
        ActivityLog log = new ActivityLog(username, action);
        return activityLogRepository.save(log);
    }

    public List<ActivityLog> getAllActivities() {
        return activityLogRepository.findAll();
    }
}
