package vn.peterbui.myproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.peterbui.myproject.convert.SecurityUtil;
import vn.peterbui.myproject.convert.annotation.ApiMessage;
import vn.peterbui.myproject.domain.Subscriber;
import vn.peterbui.myproject.exception.IdInvalidException;
import vn.peterbui.myproject.service.SubscriberService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SubscriberController {
    private final SubscriberService subscriberService;

    @PostMapping("/subscribers")
    @ApiMessage("Create a subscriber")
    public ResponseEntity<Subscriber> createSubscriber(@Valid @RequestBody Subscriber reqSubscriber) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.handleCreateSubscriber(reqSubscriber));
    }

    @PutMapping("/subscribers")
    @ApiMessage("Update a subscriber")
    public ResponseEntity<Subscriber> updateSubscriber(@RequestBody Subscriber reqSubscriber) {
        return ResponseEntity.status(HttpStatus.OK).body(this.subscriberService.handleUpdateSubscriber(reqSubscriber));
    }

    @DeleteMapping("/subscribers/{id}")
    @ApiMessage("Delete a subscriber")
    public ResponseEntity<Void> deleteSubscriber(@PathVariable Long id) {
        this.subscriberService.handleDeleteSubscriber(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/subscribers/skill")
    @ApiMessage("Get subscriber's skill")
    public ResponseEntity<Subscriber> getSubscriberSkill() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get() : "";
        return ResponseEntity.ok().body(this.subscriberService.findByEmail(email));
    }
}
