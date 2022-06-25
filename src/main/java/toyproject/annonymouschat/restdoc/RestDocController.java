package toyproject.annonymouschat.restdoc;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestDocController {
    @GetMapping("/rest-doc")
    public ResponseEntity restDocs(@RequestBody RestDocDto dto) {
        dto.setName(dto.getName() + " 뭄무");
        dto.setAge(dto.getAge() + 100);
        dto.setLocation(dto.getLocation() + " 뭄무");

        return ResponseEntity.ok(dto);
    }
}
