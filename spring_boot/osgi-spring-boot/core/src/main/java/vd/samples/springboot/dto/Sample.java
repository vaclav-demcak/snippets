package vd.samples.springboot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Sample {
    String name;
    Long id;
}
