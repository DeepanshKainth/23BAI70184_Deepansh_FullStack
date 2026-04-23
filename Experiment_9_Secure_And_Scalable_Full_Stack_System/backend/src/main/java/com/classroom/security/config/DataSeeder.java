package com.classroom.security.config;

import com.classroom.security.entity.Poll;
import com.classroom.security.entity.PollOption;
import com.classroom.security.entity.PollStatus;
import com.classroom.security.repository.PollRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedPolls(PollRepository pollRepository) {
        return args -> {
            if (pollRepository.count() > 0) {
                return;
            }

            Poll firstPoll = new Poll();
            firstPoll.setQuestion("Which LivePoll feature should we build first?");
            firstPoll.setDescription("A starter poll for the classroom demo.");
            firstPoll.setCreatedBy("system");
            firstPoll.setStatus(PollStatus.OPEN);

            addOption(firstPoll, "Live results chart");
            addOption(firstPoll, "Anonymous voting");
            addOption(firstPoll, "Poll scheduling");

            Poll secondPoll = new Poll();
            secondPoll.setQuestion("Which frontend integration matters most?");
            secondPoll.setDescription("Shows how React talks to a secured backend.");
            secondPoll.setCreatedBy("system");
            secondPoll.setStatus(PollStatus.OPEN);

            addOption(secondPoll, "Google login popup");
            addOption(secondPoll, "Protected admin panel");
            addOption(secondPoll, "Automatic token refresh");

            pollRepository.save(firstPoll);
            pollRepository.save(secondPoll);
        };
    }

    private void addOption(Poll poll, String text) {
        PollOption option = new PollOption();
        option.setText(text);
        poll.addOption(option);
    }
}