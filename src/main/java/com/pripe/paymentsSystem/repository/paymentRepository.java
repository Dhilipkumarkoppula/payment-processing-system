package com.pripe.paymentsSystem.repository;

import com.pripe.paymentsSystem.entity.payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface paymentRepository extends JpaRepository<payment, UUID> {
}
