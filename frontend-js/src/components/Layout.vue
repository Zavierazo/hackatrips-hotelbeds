<template>
	<div class="app-layout">
		<nav>
			<!-- router-link :to="{ name: 'sign-out' }">Sign out</router-link -->
		</nav>

		<slot name="content"></slot>

		<footer>
			<p>&copy; 2017</p>
		</footer>
	</div>
</template>

<script>
	export default {
		name: 'app-layout'
	}
</script>
