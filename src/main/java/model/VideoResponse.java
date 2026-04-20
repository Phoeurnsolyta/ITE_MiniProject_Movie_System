package model;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VideoResponse {
    private Integer id;
    private List<Video> results;
}
