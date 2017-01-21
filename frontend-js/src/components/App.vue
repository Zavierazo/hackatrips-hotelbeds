<template>
	<div class="app-container">
		<template v-if="$route.matched.length">
			<router-view></router-view>
		</template>
	</div>
</template>

<script>
	import auth from '../auth'

	export default {
		name: 'app-container',
		data: {
			loggedIn: auth.loggedIn()
		},
		created () {
			auth.onChange = loggedIn => {
				this.loggedIn = loggedIn
			}
		}
	}
</script>

<style>
	@import '../styles/normalize';
	@import '../styles/source_sans_pro';

	body {
		font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif;
		font-weight: 400;
		-webkit-font-smoothing: antialiased;
		-moz-osx-font-smoothing: grayscale;
		color: #333;
		font-size: 80%;
		width: 700px;
		margin: 0 auto;
	}
</style>
