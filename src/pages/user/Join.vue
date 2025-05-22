<template>
  <div class="container">
    <main>
      <div class="py-5 text-center">
        <h1 class="h2">회원가입</h1>
        <p class="lead">회원가입을 위한 정보를 기입해주세요</p>
      </div>
      <div class="row justify-content-center">
        <div class="col-md-7 col-lg-6">
          <h4 class="mb-3 text-center">회원 정보</h4>
          <div class="row g-3 w-100">
            <div class="col-12">
              <label for="email" class="form-label" @keyup.enter="join()"
                >Email</label
              >
              <input
                v-model="state.form.email"
                type="email"
                class="form-control"
                placeholder="you@example.com"
                required
              />
            </div>
            <div class="col-12">
              <label for="password" class="form-label" @keyup.enter="join()"
                >비밀번호</label
              >
              <input
                v-model="state.form.password"
                type="password"
                class="form-control"
                placeholder="영문+숫자 구성으로 입력해주세요"
                required
              />
            </div>
          </div>
          <hr class="my-4 w-100" />
          <div class="form-check text-center">
            <input
              v-model="state.form.checkbox"
              true-value="Yes"
              false-value="No"
              type="checkbox"
              class="form-check-input"
              id="same-address"
            />
            <label
              class="form-check-label"
              for="same-address"
              @keyup.enter="join()"
              >개인정보 수집에 동의합니다.</label
            >
          </div>
          <hr class="my-4 w-100" />
          <button class="w-100 btn btn-primary btn-lg" @click="join()">
            회원가입
          </button>
        </div>
      </div>
    </main>
    <div v-if="emailError" class="alert alert-danger">
      {{ emailError }}
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch } from "vue";
import { validate } from "@/utils/validation.js";
import { debounce } from "vue-debounce";
import { useAlert } from "@/utils/alert.js";
import axios from "@/axios.js";
import router from "@/scripts/router.js";

const { vAlert, vSuccess } = useAlert();

const state = reactive({
  form: {
    email: "",
    password: "",
    checkbox: "No",
  },
});

const emailError = ref("");

watch(
  () => state.form.email,
  debounce((newEmail) => {
    if (!newEmail) return;
    axios.get(`/api/account/checkEmail?email=${newEmail}`).then((res) => {
      // emailError.value = res.data.exists ? "이미 등록된 이메일입니다." : "";
      emailError.value = res.data.exists
        ? vAlert("이미 등록된 이메일입니다.")
        : "";
    });
  }, 500),
);
const join = async () => {
  //유효성 검사 실행
  const errors = validate({
    email: state.form.email,
    password: state.form.password,
    checkbox: state.form.checkbox,
  });

  if (errors.email) {
    vAlert(errors.email);
  } else if (errors.password) {
    vAlert(errors.password);
  } else if (errors.checkbox) {
    vAlert(errors.checkbox);
  } else {
    await axios
      .post("/api/account/join", state.form)
      .then(async (res) => {
        vSuccess("회원가입이 완료되었습니다.");

        // 회원가입 성공 후 로그인 요청
        await axios.post("/api/account/login", {
          email: state.form.email,
          password: state.form.password,
        });

        router.push("/");
      })
      .catch((err) => {
        vAlert(" 이미 등록된 이메일입니다.");
      });
  }
};
</script>

<style lang="scss" scoped></style>
