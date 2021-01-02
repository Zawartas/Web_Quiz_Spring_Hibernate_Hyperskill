package com.example.demo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedRepository extends JpaRepository<Completed, Long> {
    Page<Completed> findByUserId(long userId, Pageable pageable);
}
