import { createApp } from "vue";
import { createPinia } from "pinia";
import router from "@/scripts/router.js";
import App from "./App.vue";
import vueDebounce from "vue-debounce";
// createApp(App).use(store).use(router).mount('#app')

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);
app.use(router);
app.use(vueDebounce);
app.mount("#app");
