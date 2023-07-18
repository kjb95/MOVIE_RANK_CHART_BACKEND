package movierankchart.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    MOVIE_OPEN_API_HISTORY_EMPTY("[BATCH] 해당하는 movie_open_api_history가 비어있습니다."),
    KOBIS_CALL_FAIL("[BATCH] KOBIS OPEN API 호출을 실패 했습니다."),
    STEP_TYPE_NOT_FOUND("[BATCH] 해당하는 Batch Step 이름이 존재하지 않습니다.");

    private final String message;
}
