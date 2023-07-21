package movierankchart.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URI;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ErrorResponse {
    private URI type;  // 에러를 분류하기 위한 URI 식별자
    private String title;  // 에러에 대한 간략한 설명
    private int status;  // HTTP response code
    private String detail;  // 에러에 대한 자세한 설명
    private URI instance;  // 에러 발생 근원 URI
}
