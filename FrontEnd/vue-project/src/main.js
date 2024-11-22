import './assets/main.css'
import '@vueform/slider/themes/default.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import 'v-calendar/dist/style.css'
import VCalendar from 'v-calendar'

import App from './App.vue'
import router from './router'

const app = createApp(App)

const pinia = createPinia()

// Pinia 플러그인 등록
pinia.use(piniaPluginPersistedstate)

app.use(pinia)
app.use(router)
app.use(VCalendar, {})

app.mount('#app')
