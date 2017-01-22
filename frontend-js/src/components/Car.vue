<template>
    <form>
        <h3>Reserva un coche para compartir</h3>

        <label>Origen</label>
        <input >

        <label>Destino</label>
        <input >

        <label>Plazas</label>
        <select>
            <option>1</option>
            <option>2</option>
            <option>3</option>
            <option>4</option>
        </select>

        <button>Reservar</button>
    </form>
</template>

<style scoped lang="scss">
    form {
        clear: both;
        width: 80%;
        margin: 2.5em auto 1.5em;

        h3 {
            font-weight: 200;
            font-size: 1.8em;
        }
    }


</style>

<script>
    export default{
        data(){
            return{
            }
        },
        components:{
        }
    }
</script>
