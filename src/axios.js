import axios from "axios";

// Axios 기본 설정
const instance = axios.create({
  baseURL: "http://localhost:8080", // 서버 URL을 넣어주세요.
  withCredentials: true, // 쿠키를 자동으로 포함시킬 수 있도록 설정
});

// 요청 인터셉터 설정 (필요시)
instance.interceptors.request.use(
  (config) => {
    // JWT가 포함된 쿠키가 자동으로 요청에 포함됩니다.
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

// 응답 인터셉터 설정 (필요시)
instance.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // 예외 처리 (예: 인증 오류 발생 시)
    if (error.response && error.response.status === 401) {
      console.error("Unauthorized access - redirecting to login page");
      // 로그인 페이지로 리다이렉션 처리
    }
    return Promise.reject(error);
  },
);

export default instance;
