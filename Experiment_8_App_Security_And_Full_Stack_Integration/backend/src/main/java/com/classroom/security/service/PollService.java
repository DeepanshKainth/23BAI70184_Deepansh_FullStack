package com.classroom.security.service;

import com.classroom.security.dto.PollOptionResponseDTO;
import com.classroom.security.dto.PollRequestDTO;
import com.classroom.security.dto.PollResponseDTO;
import com.classroom.security.dto.VoteRequestDTO;
import com.classroom.security.entity.Poll;
import com.classroom.security.entity.PollOption;
import com.classroom.security.entity.PollStatus;
import com.classroom.security.entity.Vote;
import com.classroom.security.exception.ResourceNotFoundException;
import com.classroom.security.repository.PollRepository;
import com.classroom.security.repository.VoteRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class PollService {

    private final PollRepository pollRepository;
    private final VoteRepository voteRepository;

    public PollService(PollRepository pollRepository, VoteRepository voteRepository) {
        this.pollRepository = pollRepository;
        this.voteRepository = voteRepository;
    }

    public PollResponseDTO createPoll(PollRequestDTO requestDTO, Authentication authentication) {
        List<String> sanitizedOptions = sanitizeOptions(requestDTO.getOptions());
        if (sanitizedOptions.size() < 2) {
            throw new IllegalArgumentException("Please provide at least two non-empty options.");
        }

        Poll poll = new Poll();
        poll.setQuestion(trimToNull(requestDTO.getQuestion()));
        poll.setDescription(trimToNull(requestDTO.getDescription()));
        poll.setExpiresAt(requestDTO.getExpiresAt());
        poll.setCreatedBy(authentication.getName());
        poll.setStatus(PollStatus.OPEN);

        sanitizedOptions.forEach(optionText -> {
            PollOption option = new PollOption();
            option.setText(optionText);
            poll.addOption(option);
        });

        return toResponse(pollRepository.save(poll));
    }

    @Transactional(readOnly = true)
    public List<PollResponseDTO> getAllPolls() {
        return pollRepository.findAllWithOptions().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PollResponseDTO getPollById(Long pollId) {
        return toResponse(findPollOrThrow(pollId));
    }

    @Transactional(readOnly = true)
    public PollResponseDTO getPollResults(Long pollId) {
        return getPollById(pollId);
    }

    public PollResponseDTO vote(Long pollId, VoteRequestDTO requestDTO, Authentication authentication) {
        Poll poll = findPollOrThrow(pollId);
        if (isClosed(poll)) {
            throw new IllegalArgumentException("This poll is closed.");
        }

        String voterName = authentication.getName();
        if (voteRepository.existsByPollIdAndVoterName(pollId, voterName)) {
            throw new IllegalArgumentException("You have already voted in this poll.");
        }

        PollOption selectedOption = poll.getOptions().stream()
            .filter(option -> Objects.equals(option.getId(), requestDTO.getOptionId()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("The selected option is not part of this poll."));

        selectedOption.incrementVoteCount();

        Vote vote = new Vote();
        vote.setPoll(poll);
        vote.setPollOption(selectedOption);
        vote.setVoterName(voterName);
        voteRepository.save(vote);

        return toResponse(poll);
    }

    public void deletePoll(Long pollId) {
        Poll poll = findPollOrThrow(pollId);
        pollRepository.delete(poll);
    }

    private Poll findPollOrThrow(Long pollId) {
        return pollRepository.findById(pollId)
            .orElseThrow(() -> new ResourceNotFoundException("Poll not found with id: " + pollId));
    }

    private boolean isClosed(Poll poll) {
        if (poll.getStatus() == PollStatus.CLOSED) {
            return true;
        }

        return poll.getExpiresAt() != null && poll.getExpiresAt().isBefore(LocalDateTime.now());
    }

    private PollResponseDTO toResponse(Poll poll) {
        PollResponseDTO responseDTO = new PollResponseDTO();
        responseDTO.setId(poll.getId());
        responseDTO.setQuestion(poll.getQuestion());
        responseDTO.setDescription(poll.getDescription());
        responseDTO.setCreatedBy(poll.getCreatedBy());
        responseDTO.setCreatedAt(poll.getCreatedAt());
        responseDTO.setExpiresAt(poll.getExpiresAt());
        responseDTO.setStatus(isClosed(poll) ? PollStatus.CLOSED : PollStatus.OPEN);

        List<PollOptionResponseDTO> optionResponses = poll.getOptions().stream()
            .map(option -> {
                PollOptionResponseDTO optionResponse = new PollOptionResponseDTO();
                optionResponse.setId(option.getId());
                optionResponse.setText(option.getText());
                optionResponse.setVoteCount(option.getVoteCount());
                return optionResponse;
            })
            .toList();

        int totalVotes = optionResponses.stream().mapToInt(PollOptionResponseDTO::getVoteCount).sum();
        optionResponses.forEach(optionResponse -> {
            double percentage = totalVotes == 0
                ? 0.0
                : Math.round((optionResponse.getVoteCount() * 1000.0 / totalVotes)) / 10.0;
            optionResponse.setPercentage(percentage);
        });

        responseDTO.setTotalVotes(totalVotes);
        responseDTO.setOptions(optionResponses);
        return responseDTO;
    }

    private List<String> sanitizeOptions(List<String> options) {
        if (options == null) {
            return List.of();
        }

        return options.stream()
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(option -> !option.isBlank())
            .toList();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}