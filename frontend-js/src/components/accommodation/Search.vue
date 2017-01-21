<template>
	<app-layout>
		<div slot="content">
			<p v-bind:title="msg">{{ msg }}</p>
		</div>
	</app-layout>
</template>

<script>
	export default {
		data () {
			return {
				msg: 'Welcome back!'
			}
		},
		methods: {
		}
	}
</script>
