package toyproject.annonymouschat.config.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class ModelView {
    public ModelView(String viewName) {
        this.viewName = viewName;
    }

    public ModelView() {
    }

    private String viewName;
    private Map<String, Object> model = new HashMap<>();
}
