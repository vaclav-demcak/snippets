package vd.samples.springboot.commons.plugins.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ActionResponse {
    String body;
}
