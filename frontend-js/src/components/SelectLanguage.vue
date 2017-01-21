<template>
	<div class="select-language">
		<a v-if="$store.getters.currentLanguage != key" href="#" class="language-item" v-for="(lang, key) in getAvailableLanguages()" @click.prevent="setLanguage(key)">{{ lang.name }}</a>
		<span class="language-item active" v-else>{{ lang.name }}</span>
	</div>
</template>

<script>
	import Vue from 'vue'

	export default {
		mounted() {
		},
		methods: {
			setLanguage(key) {
				// TODO: check if page reload is required
				this.$store.commit('setLanguage', key)
			},
			getAvailableLanguages() {
				return this.$store.getters.availableLanguages
			}
		}
	}
</script>

<style scoped lang="scss">
	.select-language {
		.language-item {
			margin-right: 0.8rem;

			&.active {
				font-weight: bold;
			}
		}
	}
</style>
