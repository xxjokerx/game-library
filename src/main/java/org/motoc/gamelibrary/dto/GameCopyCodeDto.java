package org.motoc.gamelibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameCopyCodeDto {

    @Pattern(regexp = "^[0-9]{1,5}$")
    private String objectCode;

}
