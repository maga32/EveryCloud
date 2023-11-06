import { createRouter, createWebHistory, useRoute } from 'vue-router'
const Sample          = () => import(/* webpackChunkName: "Sample" */ '@/pages/Sample.vue')
const Sample2         = () => import(/* webpackChunkName: "Sample2" */ '@/pages/Sample2.vue')
const File            = () => import(/* webpackChunkName: "File" */ '@/pages/file/File.vue')
const Share           = () => import(/* webpackChunkName: "Share" */ '@/pages/share/Share.vue')
const UpdateUserForm  = () => import(/* webpackChunkName: "User" */ '@/pages/user/UpdateUserForm.vue')
const LoginForm       = () => import(/* webpackChunkName: "User" */ '@/pages/user/LoginForm.vue')

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/',                name: 'home',           component: Sample,          meta: { } },
    { path: '/sample',          name: 'sample',         component: Sample,          meta: { } },
    { path: '/sample2',         name: 'sample2',        component: Sample2,         meta: { } },
    { path: '/file',            name: 'file',           component: File,            meta: { } },
    { path: '/share',           name: 'share',          component: Share,           meta: { } },
    { path: '/updateUserForm',  name: 'updateUserForm', component: UpdateUserForm,  meta: { } },
    { path: '/loginForm',       name: 'loginForm',      component: LoginForm,       meta: { } },
    // 모바일 샘플
    { path: '/m',      name: 'mobile',     component: Sample,     meta: { isMobile: true } },
  ]
})
export default router