import { defineStore } from "pinia";

export const useCartStore = defineStore("cart", {
  state: () => ({
    items: [], //각 item에 quantity(수량) 포함
    userId: null,
  }),
  actions: {
    setItems(items) {
      this.items = items;
      if (this.userId) {
        localStorage.setItem(`cart_${this.userId}`, JSON.stringify(items));
      }
    },
    updateQuantity(itemId, quantity) {
      const item = this.items.find((i) => i.id === itemId);
      if (item) item.quantity = quantity;

      if (this.userId) {
        localStorage.setItem(`cart_${this.userId}`, JSON.stringify(this.items));
      }
    },
    clearCart() {
      this.items = [];
      if (this.userId) {
        localStorage.removeItem(`cart_${this.userId}`);
      }
    },
  },
  persist: true, //localStorage에 저장하게해줌
});
