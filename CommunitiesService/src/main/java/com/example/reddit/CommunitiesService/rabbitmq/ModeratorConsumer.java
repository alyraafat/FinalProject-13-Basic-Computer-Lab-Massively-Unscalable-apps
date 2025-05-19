package com.example.reddit.CommunitiesService.rabbitmq;

import com.example.reddit.CommunitiesService.dto.BanRequest;
import com.example.reddit.CommunitiesService.dto.UnbanRequest;
import com.example.reddit.CommunitiesService.services.CommunityService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = RabbitMQConfig.MOD_COMMANDS_QUEUE)
public class ModeratorConsumer {

    // Add any service you want and call the method in its rabitthandler
    private final CommunityService communityService;

    public ModeratorConsumer(CommunityService communityService) {
        this.communityService = communityService;
    }

    @RabbitHandler
    public void banUser(BanRequest banRequest) {
        System.out.println("User: " + banRequest.getUserID() + "is banned from Community: " + banRequest.getCommunityID());
        communityService.banUser(banRequest.getCommunityID(), banRequest.getUserID());
    }

    @RabbitHandler
    public void unbanUser(UnbanRequest unbanRequest) {
        System.out.println("User: " + unbanRequest.getUserID() + "is unbanned from Community: " + unbanRequest.getCommunityID());
        communityService.unbanUser(unbanRequest.getCommunityID(), unbanRequest.getUserID());
    }
}
