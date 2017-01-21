<template>
	<div>
		ticket-search
	</div>
</template>

<script>
	export default {
		name: 'app',
		data () {
			return {
				msg: 'Welcome back!',
				seen: true,
				todos: [
					{ text: "Hello" },
					{ text: "World" }
				]
			}
		},
		methods: {
			myMethod: function() {
				console.log("Hello world");
			}
		}
	}
</script>
