<template>
  <div class="col-12" id="fileListContainer">
    <div id="fileList" class="px-2"></div>
    <div id="loadingList" class="text-center m-2" :class="!loadingList || 'act'"><i class="fa-solid fa-circle-notch fa-spin" /> Loading... </div>
    <button @click="testtest">test?</button>
    {{loadingList}}
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

const props = defineProps(['form', 'loadingList'])

function testtest() {
  console.log(props.form)
  console.log(props)
}
// const emit = defineEmits(["update"])
//
// emit("update", "batman")
</script>

<style>
</style>