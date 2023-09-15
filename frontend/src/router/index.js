import { createRouter, createWebHistory } from 'vue-router';
const Sample = () => import(/* webpackChunkName: "Sample" */ '@/pages/Sample')
const File = () => import(/* webpackChunkName: "File" */ '@/pages/File')

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/sample',    name: 'sample',   component: Sample,    meta: { } },
    { path: '/file',      name: 'file',     component: File,      meta: { } },
    // 모바일 샘플
    { path: '/m',      name: 'mobile',     component: Sample,     meta: { isMobile: true } },
  ]
})
export default router