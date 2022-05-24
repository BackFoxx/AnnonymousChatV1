package toyproject.annonymouschat.config;
/*
데이터베이스에서 커넥션을 가져오는 데 필요한 정보를 담은  추상 클래스.

인스턴스 생성을 방지하기 위해 abstract 추상 클래스로 선언하고
인스턴스로 생성하지 않아도 변수를 사용할 수 있도록
정보들을 static 상수로 선언합니다.
*/
public abstract class ConnectionConst {
    public static final String URL = "jdbc:h2:tcp://localhost/~/test";
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "";
}
