import router from "@/scripts/router.js";
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

instance.interceptors.response.use(
  (response) => {
    const res = response.data;

    // code가 200이 아닐 경우, 에러로 간주
    if (res.code !== "200") {
      // 필요한 경우 전역 alert 처리
      console.log(res.msg || "오류가 발생했습니다.");
      return Promise.reject(new Error(res.msg || "오류가 발생했습니다."));
    }

    // code === "200" 이면 성공, data 유무에 따라 적절히 반환
    return res.hasOwnProperty("data") ? res.data : null;
  },
  (error) => {
    const errorMsg =
      error.response?.data?.msg || "서버에서 에러가 발생했습니다.";

    if (error.response?.status === 401) {
      router.push("/login");
      return Promise.reject(new Error(errorMsg));
    }

    return Promise.reject(error);
  },
);
export default instance;
