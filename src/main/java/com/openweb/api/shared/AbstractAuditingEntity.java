package com.openweb.api.shared;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base abstract class for entities which will hold definitions for created, last modified,
 * last modified by attributes.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    @JsonIgnore
    private LocalDateTime createdDate = LocalDateTime.now();


    @UpdateTimestamp
    @Column(name = "last_modified_date")
    @JsonIgnore
    private LocalDateTime lastModifiedDate;
    /**
     *
     */
    @Column(name = "delete_dat")
    private LocalDateTime deletedAt;
}
