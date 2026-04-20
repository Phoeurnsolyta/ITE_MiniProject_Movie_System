package model;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class MovieResponse {
    private Integer page;
    private List<Movie> results;
    private Integer totalPages;
    private Integer totalResults;

}
