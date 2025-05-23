import axios from "axios";
import { useAccountStore } from "@/scripts/useAccountStore.js";

/* Axios 기본 설정 */
const instance = axios.create({
  baseURL: "http://localhost:8080", // 서버 URL을 넣어주세요.
  withCredentials: true, // 쿠키를 자동으로 포함시킬 수 있도록 설정
});

// 요청 인터셉터 설정
instance.interceptors.request.use(
  (config) => {
    // JWT가 포함된 쿠키가 자동으로 요청에 포함됩니다.
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

// 응답 인터셉터 설정
instance.interceptors.response.use(
  (response) => {
    const res = response.data;

    // 1. boolean만 단독으로 오는 경우는 성공으로 간주
    if (typeof res === "boolean") return res;

    // 2-1. code가 200이 아닐 경우, 에러로 간주
    if (res.code !== "200") {
      // 리뷰 없음은 정상으로 간주
      if (res.code === "404") return null;
      return Promise.reject(new Error(res.msg || "오류가 발생했습니다."));
    }

    // 2-2. code가 200 이면 성공
    return res.hasOwnProperty("data") ? res.data : null;
  },

  (error) => {
    const accountStore = useAccountStore();

    // 토큰이 유효하지않거나 만료된 경우
    if (error.response?.status === 401) {
      // 로그인 상태 false로 변경
      accountStore.logout();
    }

    const errorMsg =
      error.response?.data?.msg || "서버에서 에러가 발생했습니다.";
    console.log(error);

    return Promise.reject(error);
  },
);
export default instance;
