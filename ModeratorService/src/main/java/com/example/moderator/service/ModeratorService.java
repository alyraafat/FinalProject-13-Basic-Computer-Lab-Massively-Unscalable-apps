package com.example.moderator.service;

import com.example.moderator.command.Command;
import com.example.moderator.command.CommandController;
import com.example.moderator.command.impl.*;
import com.example.moderator.model.Moderator;
import com.example.moderator.model.Report;
import com.example.moderator.rabbitmq.ModeratorProducer;
import com.example.moderator.repository.ModeratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ModeratorService {

    private final ModeratorRepository moderatorRepository;
    private CommandController<Void> commandController;
    private final ReportService reportService;
    private final ModeratorProducer moderatorProducer;

    @Autowired
    public ModeratorService(ModeratorRepository moderatorRepository,
                            ReportService reportService, ModeratorProducer moderatorProducer) {
        this.moderatorRepository = moderatorRepository;
        this.reportService = reportService;
        this.moderatorProducer = moderatorProducer;
    }

    // ========== Moderator Management ==========
    public List<Moderator> getModeratorsForCommunity(UUID communityId) {
        return moderatorRepository.findByCommunityId(communityId);
    }

    @Transactional
    public Moderator addModerator(UUID userId, UUID communityId, UUID moderatorId) {
        verifyModerator(moderatorId, communityId);
        if (moderatorRepository.existsByUserIdAndCommunityId(userId, communityId)) {
            throw new ModeratorAlreadyExistsException(userId, communityId);
        }
        return moderatorRepository.save(new Moderator(userId, communityId));
    }

    @Transactional
    public void removeModerator(UUID userId, UUID communityId, UUID moderatorId) {
        verifyModerator(moderatorId, communityId);
        Moderator moderator = moderatorRepository.findByUserIdAndCommunityId(userId, communityId)
                .orElseThrow(() -> new ModeratorNotFoundException(userId, communityId));
        moderatorRepository.delete(moderator);
    }

    public boolean isModerator(UUID userId, UUID communityId) {
        return moderatorRepository.existsByUserIdAndCommunityId(userId, communityId);
    }



    // ========== Command Pattern Operations ==========
    public List<Report> viewReports(UUID moderatorId) {
        Command<List<Report>> command = new ViewReportsCommand(reportService, moderatorRepository, moderatorId);
        CommandController<List<Report>> reportController = new CommandController<>();
        reportController.setCommand(command);
        return reportController.executeCommand();
    }

    @Transactional
    public void removeComment(UUID moderatorId, UUID communityId, UUID threadId, UUID commentId) {
        verifyModerator(moderatorId, communityId);
        Command<Void> command = new RemoveCommentCommand(commentId, threadId, moderatorProducer);
        commandController = new CommandController<>();
        commandController.setCommand(command);
        commandController.executeCommand();
        List<UUID> reports = reportService.getReportsForItem(commentId);
        reportService.markReportAsHandledMultiple(reports);
    }

    @Transactional
    public void banUser(UUID moderatorId, UUID communityId, UUID userId) {
        verifyModerator(moderatorId, communityId);
        Command<Void> command = new BanUserCommand(userId, communityId, moderatorProducer);
        commandController = new CommandController<>();
        commandController.setCommand(command);
        commandController.executeCommand();
    }

    @Transactional
    public void unbanUser(UUID moderatorId, UUID communityId, UUID userId) {
        verifyModerator(moderatorId, communityId);
        Command<Void> command = new UnbanUserCommand(userId, communityId, moderatorProducer);
        commandController = new CommandController<>();
        commandController.setCommand(command);
        commandController.executeCommand();
    }

    // ========== Helper Methods ==========
    private void verifyModerator(UUID userId, UUID communityId) {
        // If no moderators exist for the community, allow the action (first moderator)
        boolean communityHasModerators = moderatorRepository.existsByCommunityId(communityId);
        if (!communityHasModerators) {
            return;
        }

        // Otherwise, check if the user is a moderator
        if (!isModerator(userId, communityId)) {
            throw new UnauthorizedModeratorActionException(userId, communityId);
        }
    }

    public UUID parseModeratorIDFromHeader(String userHeaderId){
        UUID moderatorId;
        try {
            moderatorId = UUID.fromString(userHeaderId);
        } catch (Exception e) {
           throw new IllegalArgumentException("Failed to parse ModeratorID from Header");
        }
        return moderatorId;
    }

    // ========== Custom Exceptions ==========
    public static class ModeratorAlreadyExistsException extends RuntimeException {
        public ModeratorAlreadyExistsException(UUID userId, UUID communityId) {
            super("User " + userId + " is already a moderator for community " + communityId);
        }
    }

    public static class ModeratorNotFoundException extends RuntimeException {
        public ModeratorNotFoundException(UUID userId, UUID communityId) {
            super("Moderator not found for user " + userId + " in community " + communityId);
        }
    }

    public static class UnauthorizedModeratorActionException extends RuntimeException {
        public UnauthorizedModeratorActionException(UUID userId, UUID communityId) {
            super("User " + userId + " is not authorized to perform moderator actions in community " + communityId);
        }
    }
}
