package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.OtpToken;
import com.healthconnect.finalbackendcapstone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {

    Optional<OtpToken> findByTokenAndOtpTypeAndIsUsedFalse(String token, OtpToken.OtpType otpType);
    
    Optional<OtpToken> findByUserAndOtpTypeAndIsUsedFalse(User user, OtpToken.OtpType otpType);
    
    @Query("SELECT o FROM OtpToken o WHERE o.user = ?1 AND o.otpType = ?2 AND o.isUsed = false AND o.expiresAt > ?3")
    Optional<OtpToken> findValidTokenByUserAndType(User user, OtpToken.OtpType otpType, LocalDateTime now);
    
    List<OtpToken> findByUserAndOtpType(User user, OtpToken.OtpType otpType);
    
    void deleteByUserAndOtpType(User user, OtpToken.OtpType otpType);
    
    void deleteByExpiresAtBefore(LocalDateTime dateTime);
} 