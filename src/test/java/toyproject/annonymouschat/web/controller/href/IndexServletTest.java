package toyproject.annonymouschat.web.controller.href;

import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;
import toyproject.annonymouschat.config.controller.ArgumentResolverV1;
import toyproject.annonymouschat.config.controller.controller.ControllerWithTwoMap;
import toyproject.annonymouschat.config.controller.customAnnotation.ReturnType;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class IndexServletTest {
    @Test
    void isInstanceOf() throws InstantiationException, IllegalAccessException {
//        IndexServlet indexServlet = new IndexServlet();
//        System.out.println(indexServlet instanceof ControllerWithTwoMap); // true
//
//        Object indexServletClass = IndexServlet.class;
//        System.out.println(indexServletClass instanceof ControllerWithTwoMap); //false

        Object indexServlet = IndexServlet.class.newInstance();
        System.out.println(indexServlet);
        System.out.println(indexServlet instanceof ControllerWithTwoMap);
    }

    @Test
    void annotationInheritance() throws NoSuchMethodException {
        //일반적인 클래스의 어노테이션값 가져오기
        IndexServlet indexServlet = new IndexServlet();
//        System.out.println(AnnotationUtils.isCandidateClass(indexServlet.getClass(), ReturnType.class));
//        ReturnType.ReturnTypes type = indexServlet.getClass().getDeclaredMethod("process", Map.class, Map.class)
//                .getAnnotation(ReturnType.class).type();
//        System.out.println(type);

//        //프록시 객체의 어노테이션 가져오기
//        Enhancer enhancer = new Enhancer();
//        enhancer.setSuperclass(indexServlet.getClass());
//        enhancer.setCallback(new ArgumentResolverV1(indexServlet));
//        Object proxy = enhancer.create();
//
//        Method[] methods = proxy.getClass().getDeclaredMethods();
//        Arrays.stream(methods).filter(method -> method.isAnnotationPresent(ReturnType.class))
//                .forEach(method -> System.out.println("methodName : " + method.getName() + ", returnType : " + method.getAnnotation(ReturnType.class)));


        //프록시 객체의 부모 객체(프록시로 감싸기 전의 원본)에서 어노테이션 가져오기
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(indexServlet.getClass());
        enhancer.setCallback(new ArgumentResolverV1(indexServlet));
        Object proxy = enhancer.create();

        Class superclass = proxy.getClass().getSuperclass();
        ReturnType.ReturnTypes type = Arrays.stream(superclass.getDeclaredMethods())
                .filter(method -> method.getName().equals("process"))
                .findAny().get().getAnnotation(ReturnType.class).type();
        System.out.println(type);
    }
}