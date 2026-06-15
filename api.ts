//백엔드 api 주소로 fetch 요청 보내는 공통함수
// 성공 실패 json 응답처리
// jwt 토큰 있으면 authorization헤더에 넣어서 요청하는 파일임
const API_BASE_URL = "http://localhost:8080";

type ApiSuccessResponse<T> = {
  success: true;
  data: T;
  message: string;
};

type ApiErrorResponse = {
  success: false;
  error: {
    code: string;
    message: string;
  };
};

type ApiResponse<T> = ApiSuccessResponse<T> | ApiErrorResponse;

export async function requestApi<T>(
  path: string,
  options: RequestInit = {}
): Promise<T> {
  const token = localStorage.getItem("accessToken");

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(options.headers || {}),
    },
  }); //토큰 자동붙이기

  const result: ApiResponse<T> = await response.json();

  if (!response.ok || result.success === false) {
    const message =
      result.success === false
        ? result.error.message
        : "API 요청에 실패했습니다.";

    throw new Error(message);
  }

  return result.data;
}
