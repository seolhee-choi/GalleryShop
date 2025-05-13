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
const { vAlert, vSuccess } = useAlert();
import axios from "@/axios.js";
import router from "@/scripts/router.js";

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

//에러 메세지 처리 함수
const errorResponse = (err) => {
  const status = err.response?.status;
  const errMsg = err.response?.data?.error;

  if (status === 400) {
    vAlert(errMsg);
  } else {
    vAlert(errMsg);
  }
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
      errorResponse(err);
    });
};
</script>

<style scoped>
.form-signin {
  max-width: 330px;
  padding: 1rem;
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
</style>
