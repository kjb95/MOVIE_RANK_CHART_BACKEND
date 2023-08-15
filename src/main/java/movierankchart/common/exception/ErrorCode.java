package movierankchart.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION("/errors/method-argument-type-mismatch", "메소드에서의 잘못된 인자 타입 매핑", 400),
    MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION("/errors/missing-servlet-request-parameter", "클라이언트 요청의 필수 파라미터 누락", 400),
    NO_SUCH_ELEMENT_EXCEPTION("/errors/no-such-element", "찾으려는 데이터가 존재하지 않음", 404),
    BIND_EXCEPTION("errors/bind", "바인딩 실패", 400),
    ILLEGAL_ARGUMENT_EXCEPTION("/errors/illegal-argument", "유효하지 않은 메소드의 인자", 400),
    DATA_INTEGRITY_VIOLATION_EXCEPTION("/errors/data-integrity-violation", "데이터 무결성 위반", 422),
    AUTHENTICATION_CREDENTIALS_NOT_FOUND_EXCEPTION("/errors/authentication-credentials-not-found", "인증되지 않은 유저", 401),

    UNCAUGHT_RUNTIME_EXCEPTION("/errors/uncaught/runtime-exception", "catch 하지 못한 RuntimeException", 400),
    UNCAUGHT_EXCEPTION("/errors/uncaught/exception", "catch 하지 못한 Exception", 400);

    private String type;
    private String title;
    private int status;
}
