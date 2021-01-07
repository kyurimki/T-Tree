package com.ttree.ttree.domain.repository;

import com.ttree.ttree.domain.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}