<template>
    <div class="mic">
        <div class="mic__icon" data-function="record--box">
            <svg @click="record" data-function="record" version="1.1" id="Capa_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" viewBox="0 0 57 57" style="enable-background:new 0 0 57 57;" xml:space="preserve">
                <path style="fill:none;stroke:#fff;stroke-width:2;stroke-linecap:round;stroke-miterlimit:10;" d="M43.5,28v6 c0,8.271-6.729,15-15,15s-15-6.729-15-15v-6"></path>
                <path style="fill:#C7CAC7;" d="M28.5,44L28.5,44c-5.5,0-10-4.5-10-10V10c0-5.5,4.5-10,10-10h0c5.5,0,10,4.5,10,10v24 C38.5,39.5,34,44,28.5,44z"></path>
                <line style="fill:none;stroke:#fff;stroke-width:2;stroke-linecap:round;stroke-miterlimit:10;" x1="28.5" y1="49" x2="28.5" y2="56"></line>
                <polyline style="fill:none;stroke:#fff;stroke-width:2;stroke-linecap:round;stroke-miterlimit:10;" points="34.5,56 28.5,56 22.5,56 "></polyline>
                <path style="fill:#AFB6BB;" d="M38.5,34V10c0-5.5-4.5-10-10-10v44C34,44,38.5,39.5,38.5,34z"></path>
            </svg>
        </div>
        <div class="mic__bottom">
            <label for="mic-search" class="mic__caption">Manten pulsado para grabar o introduce tu búsqueda</label>
            <input type="text" @input="autosuggest" ref="micSearch" id="mic-search" class="mic__input"
                   placeholder="¿Dónde quieres ir?">
            <ul class="mic__resultlist" ref="autosuggestResults">
            </ul>
        </div>
    </div>
</template>

<style scoped lang="scss">
    .mic {
        display: block;
        width: 100%;
        transform: translate(-50%, -50%);
        position: absolute;
        left: 50%;
        top: 50%;

        &__icon {
            margin: 0 auto;
            width: 200px;
            cursor: pointer;
            background: #F44336;

            padding: 50px;
            border-radius: 100%;
            box-shadow: 0px 0px 0px 0px #F44336;
            transition: 1s;
            &:hover {
                box-shadow: 0px 0px 0px 10px #F44336;
                /*background: transparent;*/
            }
        }
        &__bottom {
            text-align: center;
        }
        &__input {
            padding: 15px;
            font-size: 16px;
            min-width: 300px;
            border: 0;
            background: transparent;
            border-bottom: 2px solid #F44336;

            &:focus {
                outline: 0;

            }

        }
        &__caption {
            display: block;
            margin: 20px;
            color: #333;
            text-align: center;
            font-size: 24px;
        }
        &__resultlist {
            list-style: none;

            .listitem {
            }
        }
    }

</style>

<script>
    import anime from 'animejs'
    import textInput from '../services/textInput'
    //import anime from 'cartodb.js'
    //this.$el
    export default {
        props: ['value', 'placeholder'],
        mounted: function () {

//            anime({
//                targets: "[data-function='record--box']",
//                loop: true,
//                duration: 2000,
//                easing: 'easeInOutSine',
//                //background: ["rgba(51, 51, 51, 0)","rgba(51, 51, 51, 1)"],
//                direction: "alternate",
//            })

//          anime({
//              targets: "[data-function='record']",
//              loop: true,
//              duration: 1000,
//              easing: 'easeInOutSine',
//              scale: [1,1.05],
//              direction: "alternate",
//          })
        },
        methods: {
            record: function () {
            },
            autosuggest: function() {
                const input = this.$refs['micSearch']
                const resultContainer = this.$refs['autosuggestResults']

                textInput.send(input.value).then( (response) => {
                    const results = response.data
                    const maxResults = 10

                    resultContainer.innerHTML = ''

                    for (let i = 0; i < results.length && i < maxResults; i++) {
                            const listItem = window.document.createElement('li')
                            listItem.innerHTML = results[i].name
                            listItem.classList.add('listitem')
                            resultContainer.appendChild(listItem)
                    }

                    //console.log(response)
                }).catch ( (error) => {
                    console.log(error)
                })
            }
        }
    }
</script>
