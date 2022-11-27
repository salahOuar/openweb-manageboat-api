package com.openweb.api.boat.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openweb.api.security.domain.User;
import com.openweb.api.shared.AbstractAuditingEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "boats")
@Where(clause = "delete_dat IS NULL")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Boat extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "name")
    private String name;

}

