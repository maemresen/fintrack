package com.maemresen.fintrack.business.dto.base;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractBaseDatedDto extends AbstractBaseDto {
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
