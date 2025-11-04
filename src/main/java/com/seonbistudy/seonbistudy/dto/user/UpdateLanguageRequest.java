package com.seonbistudy.seonbistudy.dto.user;

import com.seonbistudy.seonbistudy.model.enums.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLanguageRequest {
    private Language targetLanguage;
    private Long userId;
}
