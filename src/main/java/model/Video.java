package model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Video {
    private String name;
    private String key;
    private String site;
    private String type;
    private Boolean official;
}
