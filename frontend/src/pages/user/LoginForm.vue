<template>
<Form @submit="submit">
  <div class="row m-0">
    <div class="col-sm-1 col-xl-2 col-xxl-3"></div>
    <div class="col-12 col-sm-10 col-xl-8 col-xxl-6">
      <div class="row border border-secondary bg-light-subtle rounded-3">
        <div class="col-12 text-center py-4 fs-2">
          Login
        </div>

        <div class="col-8 col-sm-9">
          <div class="py-2">
            아이디
          </div>
          <div class="pb-2">
            <Field type="text" size="20" rules="required" class="form-control" name="id" label="아이디" v-model="form.user.id"/>
            <ErrorMessage name="id" as="p" class="text-danger" />
          </div>
          <div class="py-2">
            비밀번호
          </div>
          <div class="pb-2">
            <Field type="password" size="20" rules="required" class="form-control" name="pass" label="비밀번호" v-model="form.user.pass"/>
            <ErrorMessage name="pass" as="p" class="text-danger" />
          </div>
        </div>

        <div class="col-4 col-sm-3 d-grid gap-2">
          <button class="btn btn-primary mt-4 mb-2 d-flex justify-content-center" type="submit">
            <div class="align-self-center">로그인</div>
          </button>
        </div>

      </div>
    </div>
    <div class="col-sm-1 col-xl-2 col-xxl-3"></div>
  </div>
</Form>
</template>

<script setup>
import { onMounted, reactive } from 'vue'
import Swal from 'sweetalert2'
import router from '@/router'

const form = reactive({
  user: {
    id: '',
    pass: '',
  },
})

onMounted(() => {
  $store.dispatch('user/getSession')
    .then(()=>{
      if($store.state.user.user?.id) {
        router.replace($store.getters['link/siteHtml'])
        Swal.fire({ icon: 'error', text: '이미 로그인 되어있습니다.' })
      }
    })
})

const submit = () => {
  $http.post('/login', form.user, null)
    .then((response) => {
      if(response.data) {
        $store.dispatch('user/getSession')
        router.replace($store.getters['link/tempSiteHtml'])
      }
    })
}
</script>

<style>
</style>