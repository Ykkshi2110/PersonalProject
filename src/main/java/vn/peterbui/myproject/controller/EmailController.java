package vn.peterbui.myproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.peterbui.myproject.convert.annotation.ApiMessage;
import vn.peterbui.myproject.service.EmailService;
import vn.peterbui.myproject.service.SubscriberService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    private final SubscriberService subscriberService;

    @GetMapping("/mails")
    @ApiMessage("Send a simple email")
//    @Scheduled(cron = "*/30 * * * * *")
//    @Transactional
    public String sendEmail(){
          this.subscriberService.sendSubscribersEmailJobs();
        return "ok";
    }
}
