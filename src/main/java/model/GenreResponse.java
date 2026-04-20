package model;
import lombok.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GenreResponse {
    private List<Genre> genres;
}
