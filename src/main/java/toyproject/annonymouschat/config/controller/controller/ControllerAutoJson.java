package toyproject.annonymouschat.config.controller.controller;

public interface ControllerAutoJson<E> {
    Object process(E requestBody);
}
