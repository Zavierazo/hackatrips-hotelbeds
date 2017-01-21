<template>
	<div>
		<h3>Bedsonline</h3>

		<p v-if="$route.query.redirect">
			{{ $t("auth.login_first") }}
		</p>

		<p v-if="$route.query.from == 'session'">
			{{ $t("auth.signed_out")}}
		</p>

		<form @submit.prevent="login">
			<input type="text" v-model="username" v-bind:placeholder="$t('auth.username')" name="username"><br>
			<input-password v-model="password" v-bind:placeholder="$t('auth.password')" name="password"></input-password><br>

			<button type="submit">{{ $t("auth.sign_in") }}</button>
			
			<p v-if="error" class="error">{{ $t("auth.bad_login") }}</p>

			<p><a href="">{{ $t("auth.forgot_password") }}</a></p>
		</form>

		<footer>
			<select-language></select-language>

			<p>{{ $t("footer.copyright") }}</p>
		</footer>
	</div>
</template>

<script>
	import auth from '../../auth'

	export default {
		data: function() {
			return {
				error: false
			}
		},
		methods: {
			login() {
				auth.login(this.username, this.password, loggedIn => {
					if (!loggedIn) {
						this.error = true
					} else {
						if (this.$route.query.redirect) {
							this.$router.replace(this.$route.query.redirect)
						} else {
							this.$router.push({
								name: 'home'
							})
						}
					}
				})
			},
			setLang(key) {
				this.$root.$lang.lang = key
				this.$cookie.set('lang', key)
			}
		}
	}
</script>

<style lang="scss">
	form {
		padding: 1rem 0;

		input {
			margin-bottom: 0.35rem;
		}
	}
</style>
