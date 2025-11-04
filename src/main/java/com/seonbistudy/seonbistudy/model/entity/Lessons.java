package com.seonbistudy.seonbistudy.model.entity;

import com.seonbistudy.seonbistudy.model.base.BaseEntity;
import com.seonbistudy.seonbistudy.model.enums.LessonType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_lessons")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lessons extends BaseEntity {

    private String title; // Tiêu đề bài học

    @Column(nullable = false)
    private int lessonOrder;

    @Column(nullable = false, unique = true)
    private String lessonSlug;

    @ManyToOne(fetch = FetchType.LAZY)
    private LearningPaths learningPaths;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LessonType lessonType;
}