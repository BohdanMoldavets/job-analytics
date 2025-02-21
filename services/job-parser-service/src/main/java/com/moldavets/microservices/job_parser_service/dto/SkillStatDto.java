package com.moldavets.microservices.job_parser_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkillStatDto {

    @JsonProperty("skill_name")
    String skillName;

    @JsonProperty("count")
    Integer count;
}
