package com.example.moderator.service;

import com.example.moderator.command.Command;
import com.example.moderator.command.CommandInvoker;
import com.example.moderator.command.impl.*;
import com.example.moderator.model.Moderator;
import com.example.moderator.model.Report;
import com.example.moderator.repository.ModeratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ModeratorService {

    private final ModeratorRepository moderatorRepository;
    private final CommandInvoker commandInvoker;
    private final ReportService reportService;

    @Autowired
    public ModeratorService(ModeratorRepository moderatorRepository,
                            CommandInvoker commandInvoker,
                            ReportService reportService) {
        this.moderatorRepository = moderatorRepository;
        this.commandInvoker = commandInvoker;
        this.reportService = reportService;
    }

    // ========== Moderator Management ==========
    public List<Moderator> getModeratorsForCommunity(UUID communityId) {
        return moderatorRepository.findByCommunityId(communityId);
    }

//    public List<Community> getCommunitiesModeratedByUser(UUID userId) {
//        return moderatorRepository.findByUserId(userId);
//    }

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
        //verifyModerator(moderatorId, communityId);
        Command<List<Report>> command = new ViewReportsCommand(reportService, moderatorRepository, moderatorId);
        return commandInvoker.execute(command);
    }

    @Transactional
    public void removeComment(UUID moderatorId, UUID communityId, UUID commentId) {
        verifyModerator(moderatorId, communityId);
        Command<Void> command = new RemoveCommentCommand(commentId);
        commandInvoker.execute(command);
        List<UUID> reports = reportService.getReportsForItem(commentId);
        reportService.markReportAsHandledMultiple(reports);
    }

    @Transactional
    public void banUser(UUID moderatorId, UUID communityId, UUID userId) {
        verifyModerator(moderatorId, communityId);
        Command<Void> command = new BanUserCommand(userId, communityId);
        commandInvoker.execute(command);
    }

    @Transactional
    public void unbanUser(UUID moderatorId, UUID communityId, UUID userId) {
        verifyModerator(moderatorId, communityId);
        Command<Void> command = new UnbanUserCommand(userId, communityId);
        commandInvoker.execute(command);
    }

    // ========== Helper Methods ==========
    private void verifyModerator(UUID userId, UUID communityId) {
        if (!isModerator(userId, communityId)) {
            throw new UnauthorizedModeratorActionException(userId, communityId);
        }
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
