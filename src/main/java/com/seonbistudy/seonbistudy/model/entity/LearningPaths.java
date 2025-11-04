package com.seonbistudy.seonbistudy.model.entity;

import com.seonbistudy.seonbistudy.model.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "tbl_learning_paths")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningPaths extends BaseEntity {
    private String pathName;
    private String pathSlug;
    private String description;
}
