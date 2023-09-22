import { createRouter, createWebHistory } from 'vue-router';
const Sample = () => import(/* webpackChunkName: "Sample" */ '@/pages/Sample.vue')
const Sample2 = () => import(/* webpackChunkName: "Sample2" */ '@/pages/Sample2.vue')
const File = () => import(/* webpackChunkName: "File" */ '@/pages/File.vue')

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/sample',    name: 'sample',   component: Sample,    meta: { } },
    { path: '/sample2',   name: 'sample2',  component: Sample2,   meta: { } },
    { path: '/file',      name: 'file',     component: File,      meta: { } },
    // 모바일 샘플
    { path: '/m',      name: 'mobile',     component: Sample,     meta: { isMobile: true } },
  ]
})
export default router