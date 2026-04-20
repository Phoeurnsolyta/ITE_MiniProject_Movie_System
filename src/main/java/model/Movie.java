package model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Movie {
    private Integer id;
    private String title;
    private String releaseDate;
    private Double voteAverage;
}
