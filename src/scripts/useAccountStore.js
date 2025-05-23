import { defineStore } from "pinia";
import axios from "@/axios.js";

export const useAccountStore = defineStore("account", {
  state: () => ({
    isLoggingOut: false,
    account: {
      id: null,
      email: "",
      role: "",
    },
  }),

  getters: {
    isLoggedIn: (state) => !!state.account.id,
  },

  actions: {
    async check() {
      try {
        const response = await axios.get("/api/account/check", {
          withCredentials: true, // 서버로 JWT 토큰을 포함해서 요청을 보냄
        });

        // 서버가 인증된 사용자 정보를 보내면
        if (response && response.id && response.email) {
          this.account = {
            id: response.id,
            email: response.email,
            role: response.role,
          };
          return true;
        } else {
          this.account = { id: null, email: "", role: "" };
          return false;
        }
      } catch (error) {
        console.error("check() 호출 실패:", error);
        this.account = { id: null, email: "", role: "" }; // 로그인 안된 상태
        return false;
      }
    },

    async logout() {
      this.isLoggingOut = true;
      try {
        await axios.post("/api/account/logout");
        this.account = { id: 0, email: "", role: "" };
      } finally {
        this.isLoggingOut = false;
      }
    },
    // 사용자의 정보 업데이트
    setAccount(account) {
      this.account = account;
    },

    clearAccount() {
      this.account.id = null;
      this.account.email = "";
    },
  },
  // persist: true ❌ 제거!
});
