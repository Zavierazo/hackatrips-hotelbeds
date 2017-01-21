<template>
	<div class="app-layout">
		<h3>Bedsonline</h3>

		<nav>
			<router-link :to="{ name: 'sign-out' }">Sign out</router-link>
		</nav>

		<slot name="content"></slot>

		<footer>
			<select-language></select-language>
			<p>{{ $t("footer.copyright") }}</p>
		</footer>
	</div>
</template>

<script>
	export default {
		name: 'app-layout'
	}
</script>
