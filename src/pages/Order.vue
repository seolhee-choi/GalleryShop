<template>
  <div class="order">
    <div class="container" data-v-inspector="src/pages/Order.vue:3:5">
      <main data-v-inspector="src/pages/Order.vue:4:7">
        <div
          class="py-5 text-center"
          data-v-inspector="src/pages/Order.vue:5:9"
        >
          <h1 class="h2" data-v-inspector="src/pages/Order.vue:6:11">
            주문하기
          </h1>
          <p class="lead" data-v-inspector="src/pages/Order.vue:7:11">
            아래 정보를 정확히 기입해라
          </p>
        </div>
        <div class="row g-5" data-v-inspector="src/pages/Order.vue:12:9">
          <div
            class="col-md-5 col-lg-4 order-md-last"
            data-v-inspector="src/pages/Order.vue:13:11"
          >
            <h4
              class="d-flex justify-content-between align-items-center mb-3"
              data-v-inspector="src/pages/Order.vue:14:13"
            >
              <span
                class="text-primary"
                data-v-inspector="src/pages/Order.vue:15:15"
                >구입 목록</span
              ><span
                class="badge bg-primary rounded-pill"
                data-v-inspector="src/pages/Order.vue:16:15"
                >{{ totalQuantity }}</span
              >
            </h4>
            <ul
              class="list-group mb-3"
              data-v-inspector="src/pages/Order.vue:18:13"
            >
              <li
                class="list-group-item d-flex justify-content-between lh-sm"
                v-for="(i, idx) in cartStore.items"
                :key="idx"
              >
                <div data-v-inspector="src/pages/Order.vue:20:17">
                  <h6 class="my-0" data-v-inspector="src/pages/Order.vue:21:19">
                    {{ i.name }}
                  </h6>
                </div>
                <span
                  class="text-body-secondary"
                  data-v-inspector="src/pages/Order.vue:24:17"
                >
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
          <div
            class="col-md-7 col-lg-8"
            data-v-inspector="src/pages/Order.vue:60:11"
          >
            <h4 class="mb-3" data-v-inspector="src/pages/Order.vue:61:13">
              주문자 정보
            </h4>
            <div
              class="needs-validation"
              novalidate=""
              data-v-inspector="src/pages/Order.vue:62:13"
            >
              <div class="row g-3" data-v-inspector="src/pages/Order.vue:63:15">
                <div
                  class="col-12"
                  data-v-inspector="src/pages/Order.vue:80:17"
                >
                  <label
                    for="username"
                    class="form-label"
                    data-v-inspector="src/pages/Order.vue:81:19"
                    >이름</label
                  >
                  <div
                    class="input-group has-validation"
                    data-v-inspector="src/pages/Order.vue:82:19"
                  >
                    <input
                      type="text"
                      class="form-control"
                      id="username"
                      required=""
                      data-v-inspector="src/pages/Order.vue:84:21"
                      v-model="state.form.name"
                    />
                  </div>
                </div>
                <div
                  class="col-12"
                  data-v-inspector="src/pages/Order.vue:100:17"
                >
                  <label
                    for="address"
                    class="form-label"
                    data-v-inspector="src/pages/Order.vue:101:19"
                    >주소</label
                  ><input
                    type="text"
                    class="form-control"
                    id="address"
                    required=""
                    data-v-inspector="src/pages/Order.vue:102:19"
                    v-model="state.form.address"
                  />
                </div>
              </div>
              <hr class="my-4" data-v-inspector="src/pages/Order.vue:144:15" />
              <h4 class="mb-3" data-v-inspector="src/pages/Order.vue:159:15">
                결제 수단
              </h4>
              <div class="my-3" data-v-inspector="src/pages/Order.vue:161:15">
                <div
                  class="form-check"
                  data-v-inspector="src/pages/Order.vue:166:17"
                >
                  <input
                    id="card"
                    name="paymentMethod"
                    type="radio"
                    value="card"
                    class="form-check-input"
                    v-model="state.form.payment"
                  />
                  <label
                    class="form-check-label"
                    for="card"
                    data-v-inspector="src/pages/Order.vue:168:19"
                  >
                    신용카드</label
                  >
                </div>
                <div
                  class="form-check"
                  data-v-inspector="src/pages/Order.vue:170:17"
                >
                  <input
                    id="bank"
                    name="paymentMethod"
                    type="radio"
                    value="bank"
                    class="form-check-input"
                    v-model="state.form.payment"
                  />
                  <label
                    class="form-check-label"
                    for="bank"
                    data-v-inspector="src/pages/Order.vue:172:19"
                    >무통장입금</label
                  >
                </div>
              </div>
              <div
                class="row gy-3"
                data-v-inspector="src/pages/Order.vue:176:15"
              >
                <div
                  class="col-md-6"
                  data-v-inspector="src/pages/Order.vue:177:17"
                >
                  <label for="cc-name" class="form-label">카드번호 </label>
                  <input
                    type="text"
                    class="form-control"
                    id="cc-name"
                    v-model="state.form.cardNumber"
                  />
                </div>
              </div>
              <hr class="my-4" data-v-inspector="src/pages/Order.vue:211:15" />
              <button
                class="w-100 btn btn-primary btn-lg"
                @click="submit()"
                data-v-inspector="src/pages/Order.vue:213:15"
              >
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
import axios from "axios";
import lib from "@/scripts/lib.js";
import router from "@/scripts/router.js";

const cartStore = useCartStore();
const items = cartStore.items;
const state = reactive({
  form: {
    name: "",
    address: "",
    payment: "",
    cardNumber: "",
    items: "",
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

const submit = () => {
  const args = JSON.parse(JSON.stringify(state.form));
  args.items = JSON.stringify(cartStore.items);

  axios.post("/api/orders", args).then(() => {
    router.push("/orders");
  });
};
</script>

<style scoped></style>
