<template>
  <div class="form-signin w-100 m-auto">
    <h1 class="h3 mb-3 fw-normal">Please sign in</h1>
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
      <!--      <input-->
      <!--        class="form-check-input"-->
      <!--        type="checkbox"-->
      <!--        value="remember-me"-->
      <!--        id="checkDefault"-->
      <!--      />-->
      <!--      <label-->
      <!--        class="form-check-label"-->
      <!--        for="checkDefault"-->
      <!--        @keyup.enter="submit()"-->
      <!--      >-->
      <!--        Remember me-->
      <!--      </label>-->
      <router-link to="/join" class="join">회원가입</router-link>
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
const { vAlert, vSuccess } = useAlert();
import axios from "@/axios.js";
import router from "@/scripts/router.js";

const accountStore = useAccountStore();
const isLoggedIn = toRef(accountStore, "isLoggedIn");

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
    await axios.post("/api/account/login", state.form, {
      withCredentials: true,
    });

    await accountStore.check();
    await nextTick();

    if (accountStore.isLoggedIn) {
      vSuccess("로그인하였습니다.");
      // await router.push("/"); // 인증 상태가 업데이트 된 후에 라우터 호출
    } else {
      console.warn("상태 반영실패");
    }
  } catch (error) {
    vAlert(error);
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
</style>
