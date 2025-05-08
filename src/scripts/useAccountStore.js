import { defineStore } from "pinia";

export const useAccountStore = defineStore("account", {
  state: () => ({
    account: {
      id: 0,
      email: "",
    },
  }),
  actions: {
    setAccount({ id, email }) {
      this.account.id = id;
      this.account.email = email;
    },
  },
  persist: true, //localStorage에 저장하게해줌
});
