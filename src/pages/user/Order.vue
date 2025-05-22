<template>
  <div class="order">
    <div class="container">
      <main>
        <div class="py-5 text-center">
          <h1 class="h2">주문하기</h1>
          <p class="lead">배송받으실 정보를 입력해주세요</p>
        </div>
        <div class="row g-5">
          <div class="col-md-5 col-lg-4">
            <h4 class="d-flex justify-content-between align-items-center mb-3">
              <span>구입 목록</span
              ><span class="badge badge-custom rounded-pill">{{
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
                    <span class="badge badge-custom rounded-pill">{{
                      i.quantity
                    }}</span>
                  </h6>
                </div>
                <span class="purchase-amount">
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
  if (state.form.payment === "card") {
    const { error: cardError } = cardSerialNumberFormatter(
      state.form.cardNumber,
    );
    if (cardError) {
      vAlert(cardError, "error");
      return;
    }
  }

  axios
    .post("/api/orders", args)
    .then((res) => {
      console.log(res);
      console.log(res.code);
      if (res === true) {
        vSuccess("결제되었습니다.");
        cartStore.setItems([]);
        router.push("/orders").catch((err) => {
          console.error("라우터 이동 중 오류:", err);
        });
      } else {
        vAlert("결제 실패");
      }
    })
    .catch(() => vAlert("서버 오류"));
};
</script>

<style scoped>
.order main {
  background: #fff;
  padding: 30px 40px;
  border-radius: 12px;
  box-shadow: 0 6px 12px rgba(238, 66, 45, 0.1);
  /* flex 레이아웃 무시, 부트스트랩 그리드에 맡김 */
}

.order .col-md-5.col-lg-4.order-md-last {
  /* border-left 제거 or 주석 처리 (이게 넓이 차지해서 밀림 원인일 수 있음) */
  /* border-left: 3px solid #ee422d; */
  padding-left: 0; /* padding-left도 일단 없애기 */
  margin-top: 30px; /* 모바일 뷰에서 위쪽 간격만 조금 띄움 */
}

/* 상단 제목 정렬 및 색상 등은 유지 */
.order h4 {
  font-weight: 600;
  color: #c23523;
  margin-bottom: 25px;
  border-bottom: 2px solid #ee422d;
  padding-bottom: 8px;
}

/* 리스트 스타일과 버튼 스타일 등 기존 스타일 유지 */
.order .list-group-item {
  border: none;
  padding: 12px 0;
  font-weight: 500;
  color: #6e2e2d;
  font-size: 15px;
  border-bottom: 1px solid #f5c6cb;
}

.order .total-price {
  margin-top: 30px;
  font-weight: 700;
  font-size: 1.6rem;
  color: #ee422d;
  text-align: center;
}

/* 주문자 정보, 폼, 버튼 등 스타일 유지 */
.order .form-control {
  border: 1.5px solid #f5c6cb;
  border-radius: 8px;
  padding: 10px 14px;
  font-size: 1rem;
  transition: border-color 0.3s ease;
}

.order .form-control:focus {
  border-color: #ee422d;
  box-shadow: 0 0 8px rgba(238, 66, 45, 0.25);
  outline: none;
}

.order button.btn-primary {
  background-color: #ee422d;
  border-color: #ee422d;
  font-weight: 700;
  font-size: 1.2rem;
  padding: 14px 0;
  border-radius: 10px;
  transition: background-color 0.3s ease;
}

.order button.btn-primary:hover {
  background-color: #c23523;
  border-color: #c23523;
  cursor: pointer;
}

.badge-custom {
  background-color: #ee422d; /* 기존 주황/빨강 계열 컬러 */
  color: white;
}

.purchase-amount {
  white-space: nowrap;
  font-weight: 600;
  color: #9b2c2c;
}
</style>
