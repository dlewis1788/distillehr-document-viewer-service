package com.projectivesoftware.viewer.service;

import com.projectivesoftware.viewer.domain.IdentifierMapping;
import com.projectivesoftware.viewer.domain.IdentifierType;
import com.projectivesoftware.viewer.domain.SystemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentifierMappingRepository extends PagingAndSortingRepository<IdentifierMapping, Long> {

    Page<IdentifierMapping> findByIdentifierMappingId(Long identifierMappingId, Pageable pageable);

    IdentifierMapping findByTargetSystemTypeAndTargetIdentifierTypeAndTargetIdentifierValue(
            SystemType targetSystemType,
            IdentifierType targetIdentifierType,
            Long targetIdentifierValue
    );
}
