package by.nortin.restjwt.dto;

import static by.nortin.restjwt.utils.Constants.AUTHOR_PATTERN;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookDto extends BaseDto {

    @NotBlank(message = "Enter title")
    private String title;
    @NotBlank(message = "Enter author")
    @Pattern(regexp = AUTHOR_PATTERN, message = "Incorrect author`s name")
    private String author;
}
