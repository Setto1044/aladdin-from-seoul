import './assets/main.css'
import '@vueform/slider/themes/default.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import 'v-calendar/dist/style.css'
import VCalendar from 'v-calendar'

import App from './App.vue'
import router from './router'

// No need for dotenv in the browser
console.log(import.meta.env.VITE_ODSAY_DEV_API_KEY) // Access environment variables

const app = createApp(App)

const pinia = createPinia()

// Pinia plugin registration
pinia.use(piniaPluginPersistedstate)

app.use(pinia)
app.use(router)
app.use(VCalendar, {})

app.mount('#app')
