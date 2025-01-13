package vn.peterbui.myproject.service;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.peterbui.myproject.domain.Job;
import vn.peterbui.myproject.domain.Skill;
import vn.peterbui.myproject.domain.Subscriber;
import vn.peterbui.myproject.domain.response.email.ResEmailJob;
import vn.peterbui.myproject.exception.IdInvalidException;
import vn.peterbui.myproject.repository.JobRepository;
import vn.peterbui.myproject.repository.SkillRepository;
import vn.peterbui.myproject.repository.SubscriberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@Transactional
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public Subscriber handleCreateSubscriber(@Valid Subscriber reqSubscriber) {

        if (this.subscriberRepository.existsByEmail(reqSubscriber.getEmail())) {
            throw new IdInvalidException("Email already exists");
        }

        if (reqSubscriber.getSkills() != null) {
            List<Long> skillIds = reqSubscriber
                    .getSkills()
                    .stream()
                    .map(Skill::getId)
                    .toList();
            List<Skill> skills = this.skillRepository.findByIdIn(skillIds);
            reqSubscriber.setSkills(skills);
        }

        return this.subscriberRepository.save(reqSubscriber);
    }

    public Subscriber handleUpdateSubscriber(Subscriber reqSubscriber) {
        Subscriber currentSubscriber = this.subscriberRepository
                .findById(reqSubscriber.getId())
                .orElseThrow(() -> new IdInvalidException("Subscriber not found"));
        if (reqSubscriber.getSkills() != null) {
            List<Long> skillIds = reqSubscriber
                    .getSkills()
                    .stream()
                    .map(Skill::getId)
                    .toList();
            List<Skill> skills = this.skillRepository.findByIdIn(skillIds);
            currentSubscriber.setSkills(skills);
        }
        return this.subscriberRepository.save(currentSubscriber);
    }

    public void handleDeleteSubscriber(Long id) {
        Subscriber currentSubscriber = this.subscriberRepository
                .findById(id)
                .orElseThrow(() -> new IdInvalidException("Subscriber not found"));
        this.subscriberRepository.delete(currentSubscriber);
    }

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(job
                .getCompany()
                .getName()));
        List<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> s = skills
                .stream()
                .map(skill -> new ResEmailJob.SkillEmail(skill.getName()))
                .toList();
        res.setSkills(s);
        return res;
    }

    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (!listSubs.isEmpty()) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && !listSkills.isEmpty()) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && !listJobs.isEmpty()) {
                        List<ResEmailJob> arr = listJobs
                                .stream()
                                .map(this::convertJobToSendEmail)
                                .toList();
                        this.emailService.sendEmailFromTemplateSync(sub.getEmail(), "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay", "job", sub.getName(), arr);
                    }
                }
            }
        }
    }

    public Subscriber findByEmail(String email) {
        return this.subscriberRepository.findByEmail(email);
    }


}
