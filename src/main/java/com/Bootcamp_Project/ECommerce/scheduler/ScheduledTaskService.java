package com.Bootcamp_Project.ECommerce.scheduler;

import com.Bootcamp_Project.ECommerce.repos.UserRepository;
import com.Bootcamp_Project.ECommerce.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTaskService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailSenderService emailSenderService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledTask()
    {
        List<String> nonActiveUsers = userRepository.findAllNonActiveUser();
        if(nonActiveUsers.size() !=0)
        {
            for(String email : nonActiveUsers)
            {
                emailSenderService.sendSimpleEmail(email , "Your account is not activated. " +
                        "Please Contact Admin." , "NON ACTIVE ACCOUNT");
            }
        }
    }
}
