<template>
  <div class="form-signin w-100 m-auto">
    <h1 class="h3 mb-3 fw-normal">비밀번호 변경</h1>
    <div class="form-floating">
      <input
        type="email"
        class="form-control"
        id="floatingInput"
        v-model="accountStore.account.email"
        disabled
      />
    </div>
    <div class="form-floating">
      <input
        type="password"
        class="form-control"
        id="floatingPassword"
        placeholder="기존 비밀번호"
        v-model="state.form.password"
        @keyup.enter="submit()"
      />
      <label for="floatingPassword">기존 비밀번호</label>
    </div>
    <div class="form-floating">
      <input
        type="password"
        class="form-control"
        id="floatingNewPassword"
        placeholder="새로운 비밀번호"
        v-model="state.form.newPassword"
        @keyup.enter="submit()"
      />
      <label for="floatingNewPassword">새로운 비밀번호</label>
    </div>
    <button class="btn btn-primary w-100 py-2" @click="submit()">
      비밀번호 변경
    </button>
    <p class="mt-5 mb-3 text-body-secondary">&copy; 2017–2025</p>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import { validate } from "@/utils/validation.js";
import { useAlert } from "@/utils/alert.js";
import { useAccountStore } from "@/scripts/useAccountStore.js";
import axios from "@/axios.js";
import router from "@/scripts/router.js";

const { vAlert, vSuccess } = useAlert();
const accountStore = useAccountStore();
const state = reactive({
  form: {
    email: accountStore.account.email,
    password: "",
    newPassword: "",
  },
});

//유효성 검사 함수
const validateForm = () => {
  const errors = validate({
    email: accountStore.account.email,
    password: state.form.password,
    newPassword: state.form.newPassword,
  });
  return errors;
};

const submit = () => {
  const errors = validateForm();

  if (errors.password || errors.newPassword) {
    vAlert(errors.password || errors.newPassword);
    return;
  }

  axios
    .post("/api/account/changePassword", state.form)
    .then((res) => {
      vSuccess("비밀번호가 변경되었습니다.");
      router.push("/");
    })
    .catch((err) => {
      const errMsg = err.response.data.msg;
      vAlert(errMsg);
    });
};
</script>

<style scoped>
.form-signin {
  max-width: 330px;
  padding: 1rem;
  margin-top: 120px; /* 네비바 펼쳤을 때 가려지지 않도록 충분한 여백 */
  margin-left: auto;
  margin-right: auto;
}

.form-signin .form-floating:focus-within {
  z-index: 2;
}

.form-signin input[type="email"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}

.form-signin input[type="password"]:first-of-type {
  margin-bottom: -1px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}

.form-signin input[type="password"]:last-of-type {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}

.btn-primary {
  background-color: #ee422d;
  border-color: #ee422d;
  padding: 0.375rem 0.75rem;
  font-size: 0.9rem;
}

.btn-primary:hover {
  background-color: #c23523;
  border-color: #c23523;
}
</style>
