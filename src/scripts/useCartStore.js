import { defineStore } from "pinia";

export const useCartStore = defineStore("cart", {
  state: () => ({
    items: [], //각 item에 quantity(수량) 포함
  }),
  actions: {
    setItems(items) {
      this.items = items;
    },
    updateQuantity(itemId, quantity) {
      const item = this.items.find((i) => i.id === itemId);
      if (item) item.quantity = quantity;
    },
  },
  persist: true, //localStorage에 저장하게해줌
});
