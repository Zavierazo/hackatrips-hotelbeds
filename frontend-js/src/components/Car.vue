<template>
    <form>
        <h3 class="heading-3">Reserva un coche para compartir</h3>

        <label for="inputDes">Elige tu trayecto</label>
        <input placeholder="Origen" id="inputOr">

        <input placeholder="Destino" id="inputDes">

        <label for="selectPla">Plazas</label>
        <select id="selectPla">
            <option>1</option>
            <option>2</option>
            <option>3</option>
            <option>4</option>
        </select>

        <button>Reservar</button>
    </form>
</template>

<style scoped lang="scss">
    label{
        padding: 15px 0;
    }
    input {
        margin-bottom : 15px;width: 45%;
        margin-right: 9.6%;
        box-sizing: border-box;
        &:last-of-type{
             margin-right :0;
         }

    }

    select {
        padding    : 15px;
        font-size  : 16px;
        width      : 100%;

        border     : 0;
        background : transparent;
        border     : 2px solid #F44336;
    }

    form {
        clear  : both;
        width  : 80%;
        margin : 2.5em auto 1.5em;

    h3 {
        font-weight : 200;
        font-size   : 1.8em;
    }

    }

    button {
        background: #F44336;
        padding: 15px;
        border: 0;
        color: #fff;
        display: block;
        margin: 30px 0 30px 0;
        float: right;
        font-size: 18px;
    }


</style>

<script>
    export default{
        data(){
            return {}
        },
        components: {}
    }
</script>
