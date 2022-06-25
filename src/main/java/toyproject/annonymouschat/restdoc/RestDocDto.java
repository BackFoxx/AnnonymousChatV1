package toyproject.annonymouschat.restdoc;

import lombok.*;

@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class RestDocDto {
    String name;
    int age;
    String location;
}
