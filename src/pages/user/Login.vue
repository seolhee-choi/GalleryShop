<template>
  <div class="form-signin w-100 m-auto">
    <h1 class="h2">로그인</h1>
    <div class="form-floating">
      <input
        type="email"
        class="form-control"
        id="floatingInput"
        placeholder="name@example.com"
        v-model="state.form.email"
        @keyup.enter="submit()"
      />
      <label for="floatingInput">Email address</label>
    </div>
    <div class="form-floating">
      <input
        type="password"
        class="form-control"
        id="floatingPassword"
        placeholder="Password"
        v-model="state.form.password"
        @keyup.enter="submit()"
      />
      <label for="floatingPassword">Password</label>
    </div>
    <div class="form-check text-start my-3">
      <router-link to="/join" class="join" style="color: #ee422d"
        >회원가입</router-link
      >
    </div>
    <button class="btn btn-primary w-100 py-2" @click="submit()">
      Sign in
    </button>
    <p class="mt-5 mb-3 text-body-secondary">&copy; 2017–2025</p>
  </div>
</template>

<script setup>
import { reactive, nextTick, watch, toRef } from "vue";
import { useAlert } from "@/utils/alert.js";
import { validate } from "@/utils/validation.js";
import { useAccountStore } from "@/scripts/useAccountStore.js";
import { useCartStore } from "@/scripts/useCartStore.js";
const { vAlert, vSuccess } = useAlert();
import axios from "@/axios.js";
import router from "@/scripts/router.js";

const accountStore = useAccountStore();
const isLoggedIn = toRef(accountStore, "isLoggedIn");
const cartStore = useCartStore();
const state = reactive({
  form: {
    email: "",
    password: "",
  },
});

//유효성 검사 함수
const validateForm = () => {
  const errors = validate({
    email: state.form.email,
    password: state.form.password,
  });
  return errors;
};
const submit = async () => {
  const errors = validateForm();

  // 유효성 검사에서 오류 있으면 return
  if (errors.email || errors.password) {
    vAlert(errors.email || errors.password);
    return;
  }

  try {
    const res = await axios.post("/api/account/login", state.form, {
      withCredentials: true,
    });
    await accountStore.setAccount(res);
    await nextTick();

    if (accountStore.isLoggedIn) {
      vSuccess("로그인하였습니다.");
      const userId = res.id; // 또는 res.data.id 등 응답 형식에 맞게
      cartStore.userId = userId;
      await router.push("/"); // 인증 상태가 업데이트 된 후에 라우터 호출
    } else {
      console.warn("상태 반영실패");
    }
  } catch (error) {
    const errMsg = error.response.data.msg;
    vAlert(errMsg);
  }
};

watch(isLoggedIn, (val) => {
  if (val) {
    router.push("/");
  }
});
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

.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}

.join {
  float: right;
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
