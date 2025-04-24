import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from '@/scripts/router.js'
import App from './App.vue'

// createApp(App).use(store).use(router).mount('#app')

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.mount('#app')


