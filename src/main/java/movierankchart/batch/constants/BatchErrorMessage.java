package movierankchart.batch.constants;

public class BatchErrorMessage {
    public static final String MOVIE_OPEN_API_HISTORY_EMPTY = "해당하는 movie_open_api_history가 비어있습니다.";
    public static final String KOBIS_CALL_FAIL = "KOBIS OPEN API 호출을 실패 했습니다.";
    public static final String STEP_TYPE_NOT_FOUND = "해당하는 Batch Step 이름이 존재하지 않습니다.";
    public static final String INVALID_MOVIE_RANK_DATA = "데이터베이스에 저장된 영화 순위 데이터가 유효하지 않습니다.";
}
