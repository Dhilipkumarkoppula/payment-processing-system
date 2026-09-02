package com.pripe.paymentsSystem.repository;

import com.pripe.paymentsSystem.entity.webhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface webhookEventRepository extends JpaRepository<webhookEvent, UUID> {

    boolean existsByEventId(String EventId);
}
