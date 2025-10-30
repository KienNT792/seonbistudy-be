package com.seonbistudy.seonbistudy.model.entity;

import com.seonbistudy.seonbistudy.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tbl_level_thresholds")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LevelThreshold extends BaseEntity {
    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "xp_required", nullable = false)
    private Long xpRequired;
}
