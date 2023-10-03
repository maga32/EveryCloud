<template>
<Form @submit="submit">
  <div class="p-5">
    <div class="row">
      <div class="col-12 important">
        아이디
      </div>
      <div class="col-12 col-md-6 mb-4">
        <div v-if="form.type==='admin'">
          <Field name="id" label="아이디" rules="required|alpha_num" type="text" size="20" class="form-control" v-model="form.user.id" @keyup="unDupCheck"/>
          <ErrorMessage name="id" as="p" class="text-danger"/>
          <div class="col-12 btn btn-secondary btn-lg mt-2" @click="checkOverlapId">중복확인</div>
          <Field name="duplicateChecked" label="중복확인" rules="boolean" type="hidden" class="form-control" v-model="duplicateChecked"/>
          <ErrorMessage name="duplicateChecked" as="p" class="text-danger"/>
        </div>
        <div v-if="form.type==='user'">
            {{ form.user.id }}
        </div>
      </div>
      <div class="col-12">
        새 비밀번호
      </div>
      <div class="col-12 col-md-6 mb-4">
        <Field type="password" size="20" class="form-control" name="pass" label="비밀번호" v-model="form.user.pass"
            :rules="'min:8'+(form.origPass==='admin'? '|first_required' : '')"/>
        <ErrorMessage name="pass" as="p" class="text-danger" />
      </div>

      <div class="col-12">
        비밀번호 확인
      </div>
      <div class="col-12 col-md-6 mb-4">
        <Field type="password" size="20" class="form-control" name="confirmPass" label="비밀번호" rules="confirmed:@pass" />
        <ErrorMessage name="confirmPass" as="p" class="text-danger" />
      </div>

      <div class="col-12 important">
        닉네임
      </div>
      <div class="col-12 col-md-6 mb-4">
        <Field type="text" size="20" class="form-control" name="nickname" label="닉네임" rules="required" v-model="form.user.nickname" />
        <ErrorMessage name="nickname" as="p" class="text-danger" />
      </div>

      <div class="col-12 important">
        이메일 <span class="text-danger">(비밀번호 찾기시 필요)</span>
      </div>
      <div class="col-12 col-md-6 mb-4">
        <Field type="text" size="20" class="form-control" name="email" rules="required|email" v-model="form.user.email" />
        <ErrorMessage name="email" as="p" class="text-danger" />
      </div>

      <div class="col-12 mb-4"></div>
      <div class="col-12 col-md-6 mb-4">
        <button class="col-12 btn btn-danger btn-lg mb-2" type="submit">확인</button>
        <router-link :to="{path:$store.state.link.siteHtml}" class="col-12 btn btn-secondary btn-lg">취소</router-link>
      </div>
    </div>
  </div>
</Form>
</template>

<script setup>
import { onMounted, ref, reactive } from 'vue'
import Swal from 'sweetalert2'
import router from '@/router'

const duplicateChecked = ref(true)
const params = history.state.params || {}

const form = reactive({
  type: 'user',
  origId: '',
  origPass: '',

  user: {
    id: '',
    pass: '',
    nickname: '',
    email: '',
  },
})

onMounted(() => {
  form.type = params.type
  $http.post('/updateUserForm',{params:params}, null)
    .then((response) => {
      if(!form.type || !response.data.id) {
        $store.dispatch('user/getSession')
        router.replace($store.getters['link/siteHtml'])
        Swal.fire({ icon: 'error', text: '잘못된 접근방식입니다.' })
      } else {
        form.user.id = response.data.id
        form.user.nickname = response.data.nickname
        form.user.email = response.data.email

        form.origId = response.data.id
        form.origPass = response.data.pass
      }
    })
})

function unDupCheck() {
  duplicateChecked.value = false
}

function checkOverlapId() {
  duplicateChecked.value = false
  if(!form.user.id) {
    Swal.fire({ icon: 'error', text: '아이디를 입력해주세요.', showConfirmButton: false, timer: 1500})
    return false
  } else if (form.user.id === form.origId) {
    Swal.fire({ icon: 'success', text: '사용 가능한 아이디입니다.', showConfirmButton: false, timer: 1500})
    duplicateChecked.value = true
    return false
  }

  $http.post('/checkOverlapId',null,{params:{id: form.user.id}})
    .then((response) => {
      if(response.data===true) {
        duplicateChecked.value = true
        Swal.fire({ icon: 'success', text: '사용 가능한 아이디입니다.', showConfirmButton: false, timer: 1500})
      } else {
        Swal.fire({ icon: 'error', text: '사용 불가능한 아이디입니다.', showConfirmButton: false, timer: 1500})
      }
    })
}

function submit() {
  $http.post('/updateUser', form, null)
    .then((response) => {
      if(response.data) {
        $store.dispatch('user/getSession')
        router.replace($store.getters['link/siteHtml'])
      }
    })

}
</script>

<style>
</style>