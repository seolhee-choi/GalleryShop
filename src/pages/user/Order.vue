<template>
  <div class="order">
    <div class="container">
      <main>
        <div class="py-5 text-center">
          <h1 class="h2">주문하기</h1>
          <p class="lead">배송받으실 정보를 입력해주세요</p>
        </div>
        <div class="row g-5">
          <div class="col-md-5 col-lg-4 order-md-last">
            <h4 class="d-flex justify-content-between align-items-center mb-3">
              <span class="text-primary">구입 목록</span
              ><span class="badge bg-primary rounded-pill">{{
                totalQuantity
              }}</span>
            </h4>
            <ul class="list-group mb-3">
              <li
                class="list-group-item d-flex justify-content-between lh-sm"
                v-for="(i, idx) in cartStore.items"
                :key="idx"
              >
                <div>
                  <h6 class="my-0">
                    {{ i.name }}
                    <span class="badge bg-primary rounded-pill">{{
                      i.quantity
                    }}</span>
                  </h6>
                </div>
                <span class="text-body-secondary">
                  {{
                    lib.getNumberFormatted(
                      (i.price - (i.price * i.discountPer) / 100) * i.quantity,
                    )
                  }}원
                </span>
              </li>
            </ul>
            <h3 class="text-center total-price">
              총 {{ lib.getNumberFormatted(computedPrice) }}원
            </h3>
          </div>
          <div class="col-md-7 col-lg-8">
            <h4 class="mb-3">주문자 정보</h4>
            <div class="needs-validation" novalidate="">
              <div class="row g-3">
                <div class="col-12">
                  <label for="username" class="form-label">이름</label>
                  <div class="input-group has-validation">
                    <input
                      type="text"
                      class="form-control"
                      id="username"
                      required=""
                      v-model="state.form.name"
                    />
                  </div>
                </div>
                <div class="col-12">
                  <label for="address" class="form-label">주소</label
                  ><input
                    type="text"
                    class="form-control"
                    id="address"
                    required=""
                    v-model="state.form.address"
                  />
                </div>
              </div>
              <hr class="my-4" />
              <h4 class="mb-3">결제 수단</h4>
              <div class="my-3">
                <div class="form-check">
                  <input
                    id="card"
                    name="paymentMethod"
                    type="radio"
                    value="card"
                    class="form-check-input"
                    v-model="state.form.payment"
                  />
                  <label class="form-check-label" for="card"> 신용카드</label>
                </div>
                <div class="form-check">
                  <input
                    id="bank"
                    name="paymentMethod"
                    type="radio"
                    value="bank"
                    class="form-check-input"
                    v-model="state.form.payment"
                  />
                  <label class="form-check-label" for="bank">무통장입금</label>
                </div>
              </div>
              <div class="row gy-3" v-if="state.form.payment === 'card'">
                <div class="col-md-8">
                  <label for="cc-name" class="form-label">카드번호</label>
                  <input
                    type="text"
                    class="form-control"
                    id="cc-name"
                    v-model="state.form.cardNumber"
                    @input="handleCardInput"
                    placeholder="카드번호 숫자만 입력해주세요(16자리)"
                    maxlength="16"
                  />
                </div>
              </div>
              <div v-else-if="state.form.payment === 'bank'">
                <h3>하단 계좌로 입금해주시기 바랍니다.</h3>
                <p>노스뱅크 : 123-456-7890</p>
              </div>
              <hr class="my-4" />
              <button class="w-100 btn btn-primary btn-lg" @click="submit()">
                결제하기
              </button>
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed, reactive, onMounted } from "vue";
import { useCartStore } from "@/scripts/useCartStore.js";
import { useAlert } from "@/utils/alert.js";
import {
  cardSerialNumberFormatter,
  validateAddressFormat,
  validateNameFormat,
} from "@/utils/validation.js";
import axios from "@/axios.js";
import lib from "@/scripts/lib.js";
import router from "@/scripts/router.js";

const { vAlert, vSuccess } = useAlert();
const cartStore = useCartStore();
const items = cartStore.items;
const state = reactive({
  form: {
    name: "",
    address: "",
    payment: "",
    cardNumber: "",
    items: "",
    quantity: "",
  },
});
// 최종 수량 함수
const totalQuantity = computed(() => {
  return cartStore.items.reduce((acc, item) => acc + item.quantity, 0);
});

// 최종 금액 함수
const computedPrice = computed(() => {
  let result = 0;

  for (let i of cartStore.items) {
    result += (i.price - (i.price * i.discountPer) / 100) * i.quantity;
  }

  return result;
});

const handleCardInput = (e) => {
  const { formatted, error } = cardSerialNumberFormatter(e.target.value);

  if (error) {
    vAlert(error, "error");
    return;
  }

  state.form.cardNumber = formatted;
};
const submit = () => {
  const args = JSON.parse(JSON.stringify(state.form));
  args.items = JSON.stringify(cartStore.items);

  // 이름 유효성 검사
  const nameError = validateNameFormat(state.form.name);
  if (nameError) {
    vAlert(nameError, "error");
    return;
  }

  // 주소 유효성 검사
  const addrError = validateAddressFormat(state.form.address);
  if (addrError) {
    vAlert(addrError, "error");
    return;
  }

  // 카드번호 유효성 검사
  const { error: cardError } = cardSerialNumberFormatter(state.form.cardNumber);
  if (cardError) {
    vAlert(cardError, "error");
    return;
  }

  axios
    .post("/api/orders", args)
    .then((res) => {
      if (res.status === 200 && res.code === 200) {
        console.log(res.status);
        console.log(res.code);
        vSuccess("결제되었습니다.");
        cartStore.setItems([]);
        router.push("/orders");
      } else {
        vAlert("결제 실패");
      }
    })
    .catch(() => vAlert("서버 오류"));
};

/*
*
*
* const submit = () => {
  const formCopy = JSON.parse(JSON.stringify(state.form));

  // 유효성 검사
  const nameError = validateNameFormat(formCopy.name);
  if (nameError) return vAlert(nameError, "error");

  const { error: cardError } = cardSerialNumberFormatter(formCopy.cardNumber);
  if (cardError) return vAlert(cardError, "error");

  const addrError = validateAddressFormat(formCopy.address);
  if (addrError) return vAlert(addrError, "error");

  // 서버 전송용 데이터 정리
  const args = {
    ...formCopy,
    items: cartStore.items, // 배열 그대로 전송
  };

  axios
    .post("/api/orders", args)
    .then((res) => {
      if (res.status === 200) {
        vSuccess("결제되었습니다.");
        cartStore.setItems([]);
        router.push("/orders");
      } else {
        vAlert("결제 실패");
      }
    })
    .catch((err) => {
      console.error("서버 오류:", err);
      vAlert("서버 오류");
    });
};
* */
</script>

<style scoped></style>
