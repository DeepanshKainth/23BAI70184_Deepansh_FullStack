package com.classroom.security.controller;

import com.classroom.security.dto.PollRequestDTO;
import com.classroom.security.dto.PollResponseDTO;
import com.classroom.security.dto.VoteRequestDTO;
import com.classroom.security.service.PollService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    private final PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @GetMapping
    public List<PollResponseDTO> getAllPolls() {
        return pollService.getAllPolls();
    }

    @GetMapping("/{pollId}")
    public PollResponseDTO getPollById(@PathVariable Long pollId) {
        return pollService.getPollById(pollId);
    }

    @GetMapping("/{pollId}/results")
    public PollResponseDTO getPollResults(@PathVariable Long pollId) {
        return pollService.getPollResults(pollId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public PollResponseDTO createPoll(@Valid @RequestBody PollRequestDTO requestDTO, Authentication authentication) {
        return pollService.createPoll(requestDTO, authentication);
    }

    @PostMapping("/{pollId}/vote")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public PollResponseDTO vote(@PathVariable Long pollId,
                                @Valid @RequestBody VoteRequestDTO requestDTO,
                                Authentication authentication) {
        return pollService.vote(pollId, requestDTO, authentication);
    }

    @DeleteMapping("/{pollId}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePoll(@PathVariable Long pollId) {
        pollService.deletePoll(pollId);
    }
}