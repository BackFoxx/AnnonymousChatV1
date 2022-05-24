package toyproject.annonymouschat.config.controller;

import lombok.extern.slf4j.Slf4j;
import toyproject.annonymouschat.config.controller.customAnnotation.MyController;
import toyproject.annonymouschat.config.controller.customAnnotation.MyRequestMapping;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ControllerMappingV1 {
    public static final String BASE_PACKAGE = "toyproject.annonymouschat"; // 최초 베이스 패키지

    public Map<String, Object> controllerMapper() {
        Map<String, Object> controllerMap = new HashMap<>();
        controllerMapper(BASE_PACKAGE, controllerMap);
        return controllerMap;
    }

    private void controllerMapper(String packageName, Map<String, Object> controllerMap) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        reader.lines()
                .forEach(fileName -> {
                    if (fileName.endsWith(".class")) {
                        Class findClass = getClass(fileName, packageName);

                        if (findClass.isAnnotationPresent(MyController.class) && findClass.isAnnotationPresent(MyRequestMapping.class)) {
                            String URI = ((MyRequestMapping) findClass.getAnnotation(MyRequestMapping.class)).value();
                            try {
                                controllerMap.put(URI, findClass.newInstance());
                            } catch (InstantiationException | IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        controllerMapper(packageName + "." + fileName, controllerMap);
                    }
                });
    }

    private Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            log.error("클래스 없음, className = {}", className);
            throw new RuntimeException(e);
        }
    }
}
