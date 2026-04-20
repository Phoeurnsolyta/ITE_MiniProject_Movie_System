package model;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class MovieDetail {
    private Integer id;
    private String title;
    private String releaseDate;
    private Double voteAverage;
    private Integer runtime;
    private Long budget;
    private List<Genre> genres;
    private List<String> originCountry;
}
