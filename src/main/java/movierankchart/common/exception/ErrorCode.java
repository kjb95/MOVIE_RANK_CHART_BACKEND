package movierankchart.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    MOVIE_OPEN_API_HISTORY_ID_NOT_FOUND("[BATCH] 해당하는 movie_open_api_history_id를 찾을 수 없습니다."),
    KMDB_MOVIE_DETAIL_NOT_FOUND("[BATCH] KMDB OPEN API 요청인자인 영화 제목과 코드명에 해당하는 영화상세정보를 찾을 수 없습니다."),
    KOBIS_CALL_FAIL("[BATCH] KOBIS OPEN API 호출을 실패 했습니다."),
    STEP_TYPE_NOT_FOUND("[BATCH] 해당하는 Batch Step 이름이 존재하지 않습니다.");

    private final String message;
}
