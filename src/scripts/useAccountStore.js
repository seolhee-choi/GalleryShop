import { defineStore } from "pinia";
import axios from "axios";

export const useAccountStore = defineStore("account", {
  state: () => ({
    account: {
      id: null,
      email: "",
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
        if (response.data && response.data.id && response.data.email) {
          this.account = { id: response.data.id, email: response.data.email };
        } else {
          this.account = { id: null, email: "" }; // 로그인 안된 상태
        }
      } catch (error) {
        console.error("check() 호출 실패:", error);
        this.account = { id: null, email: "" }; // 로그인 안된 상태
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

    // async check() {
    //   try {
    //     const res = await axios.get("/api/account/check", {
    //       withCredentials: true,
    //     });
    //     const data = res.data;
    //
    //     if (data && data.id && data.email) {
    //       this.account = { id: data.id, email: data.email };
    //     } else {
    //       this.clearAccount();
    //     }
    //   } catch (error) {
    //     console.error("check() 호출 실패:", error);
    //     this.clearAccount();
    //   }
    // },
  },

  // persist: true ❌ 제거!
});
