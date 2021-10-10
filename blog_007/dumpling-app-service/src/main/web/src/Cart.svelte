<script>
    import {createEventDispatcher} from "svelte";
    import {API, ApiUrl} from "./stores/api";

    export let total;
    export let currency;

    let orderSent = false;
    const route = "/checkout/"

    const dispatch = createEventDispatcher();
    const order = () => dispatch("order", {sent: true})
    const checkout = async () => API._checkout($ApiUrl, route)

    function send() {
        order()
        orderSent = true;
        checkout()
            .catch((err) => console.log(err));
    }
</script>

<div class="container">
    <div class="card border-success">
        <div id="cart" class="card-body text-success">
            <div class="row">
                <div class="col-8 col-sm-10">
                    <p class="h6">Total amount is {total} {currency}</p>
                </div>
                <div class="col-4 col-sm-2">
                    {#if orderSent}
                        <div class="row row justify-content-center">
                            <div class="spinner-border text-success" role="status">
                                <span class="visually-hidden">Loading...</span>
                            </div>
                        </div>
                    {:else}
                        <button on:click={send} type="button"
                                class="btn btn-success delete-item">order
                        </button>
                    {/if}
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    p {
        margin: 0;
        height: 100%;
        line-height: 2.2rem;
    }
</style>