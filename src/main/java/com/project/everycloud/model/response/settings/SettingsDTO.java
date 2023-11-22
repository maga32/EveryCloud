package com.project.everycloud.model.response.settings;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SettingsDTO {
    private String type;
    private String externalUrl;
    private String useTrash;
    private String trashPath;

    private String metaTitle;
    private String metaAuthor;
    private String metaDescription;
    private String metaKeywords;
}
