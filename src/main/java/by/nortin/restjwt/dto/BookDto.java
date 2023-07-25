package by.nortin.restjwt.dto;

import static by.nortin.restjwt.utils.Constants.AUTHOR_PATTERN;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(description = "Entity of Book")
public class BookDto extends BaseDto {

    @NotBlank(message = "Enter title")
    @Schema(description = "Book`s title", example = "A Son of the Sun")
    private String title;
    @NotBlank(message = "Enter author")
    @Pattern(regexp = AUTHOR_PATTERN, message = "Incorrect author`s name")
    @Schema(description = "Book`s author", example = "Jack London")
    private String author;
}
