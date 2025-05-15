<template>
  <div class="home">
    <div class="album py-5 bg-light">
      <div class="container">
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
          <div class="col" v-for="(item, idx) in state.items" :key="idx">
            <Card :item="item" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import axios from "@/axios.js";
import Card from "@/components/user/Card.vue";

const state = reactive({
  items: [],
});
const api = () => {
  axios.get("/api/items").then((res) => {
    state.items = res;
  });
};

api();
</script>

<style></style>
