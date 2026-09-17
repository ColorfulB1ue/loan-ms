import { createApp } from 'vue'
import { createPinia } from 'pinia'
import './styles/design-tokens.css'
import './style.css'
import './styles/common.css'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import GlobalComponents from './components'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.use(ElementPlus, { locale: zhCn })
app.use(GlobalComponents)

app.mount('#app')