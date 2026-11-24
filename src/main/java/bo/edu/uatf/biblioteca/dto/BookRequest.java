package bo.edu.uatf.biblioteca.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookRequest(
        @NotBlank @Schema(example = "Cien anos de soledad") String title,
        @NotBlank @Schema(example = "Gabriel Garcia Marquez") String author,
        @NotBlank @Schema(example = "9780307474728") String isbn,
        @NotNull @Positive @Schema(example = "1967") Integer publicationYear,
        @NotBlank @Schema(example = "Novela") String category,
        @NotNull @Schema(example = "true") Boolean available) {
}
