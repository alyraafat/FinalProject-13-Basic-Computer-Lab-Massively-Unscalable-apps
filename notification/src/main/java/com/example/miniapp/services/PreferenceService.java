//package com.example.miniapp.services;
//
//import com.example.miniapp.models.DeliveryChannel;
//import com.example.miniapp.models.UserPreference;
//import com.example.miniapp.repositories.PreferenceRepository;
//import com.example.miniapp.services.strategy.DeliveryStrategy;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//
//import static com.example.miniapp.models.DeliveryChannel.EMAIL;
//import static com.example.miniapp.models.DeliveryChannel.PUSH;
//
//@Service
//public class PreferenceService {
//
//    private final PreferenceRepository preferenceRepository;
//    private final DeliveryStrategy emailStrategy;
//    private final DeliveryStrategy pushStrategy;
//
//    @Autowired
//    public PreferenceService(
//            PreferenceRepository preferenceRepository,
//            @Qualifier("emailStrategy") DeliveryStrategy emailStrategy,
//            @Qualifier("pushStrategy") DeliveryStrategy pushStrategy)
//    {
//        this.preferenceRepository = preferenceRepository;
//        this.emailStrategy = emailStrategy;
//        this.pushStrategy = pushStrategy;
//    }
//
//    public DeliveryStrategy getStrategy(String userId) {
//        UserPreference preference = preferenceRepository.findById(userId)
//                .orElseGet(() -> createDefaultPreference(userId));
//
//        return switch (preference.getPrimaryChannel()) {
//            case EMAIL -> emailStrategy;
//            case PUSH -> pushStrategy;
//            default -> throw new IllegalStateException("Unknown delivery channel");
//        };
//    }
//
//    private UserPreference createDefaultPreference(String userId) {
//        UserPreference defaultPref = new UserPreference(userId);
//        defaultPref.setUserId(userId);
//        defaultPref.setPrimaryChannel(DeliveryChannel.EMAIL);
//        return preferenceRepository.save(defaultPref);
//    }
//
//    public void updatePreferences(String userId, DeliveryChannel channel, boolean allowSms) {
//        UserPreference preference = preferenceRepository.findById(userId)
//                .orElse(new UserPreference(userId));
//
//        preference.setPrimaryChannel(channel);
//        preferenceRepository.save(preference);
//    }
//}