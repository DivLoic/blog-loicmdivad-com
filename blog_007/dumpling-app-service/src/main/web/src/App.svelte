<script>
    import {items} from "./stores/items.js";
    import Cart from "./Cart.svelte";
    import Item from "./Item.svelte";
    import Sauce from "./Sauce.svelte";
    import Header from "./Header.svelte";

    export let user = {
        loggedIn: false,
        username: null,
        userId: null
    };

    export let commands = $items.map(item => {
            return {id: item.id, number: 0}
        }
    )

    function getItem(id) {
        return $items.find((item) => item.id === id)
    }

    function login() {
        user = {
            loggedIn: true,
            username: "loicmdivad",
            userId: "4be37fed-0b40-4209-a176-0ce6eb61dd9e"
        }
    }

    function deleteItem(event) {
        commands = commands.map(item => {
            if (item.id !== event.detail.id) return item
            else return {id: item.id, number: Math.max(0, item.number - 1)}
        });
    }

    function addItem(event) {
        commands = commands.map(item => {
            if (item.id !== event.detail.id) return item
            else return {id: item.id, number: item.number + 1}
        });
    }

    function handleCheckout() {
        selectionEnabled = false
    }

    function handleAddFeedback(event) {
        console.log("handleAddFeedback!")
    }
    function handleDeleteFeedback(event) {
        console.log("handleDeleteFeedback!")
    }
    let currency = "€ 💶";
    let total = 0;
    let selectionEnabled = true;
    $: total = commands
        .map((item) => item.number * getItem(item.id).price)
        .reduce((a, b) => a + b, 0)

</script>

<main>
    <Header/>
    <div class="container">
        <div id="row selection">
            {#each commands as {id, number}}
                <Item id={id}
                      number={number}
                      enable={selectionEnabled}
                      on:add-req={addItem}
                      on:del-req={deleteItem}
                      on:add-res={handleAddFeedback}
                      on:del-res={handleDeleteFeedback}/>
            {/each}
        </div>
    </div>
    <Sauce enabled={selectionEnabled}/>
    <Cart on:order={handleCheckout} total={total} currency={currency}/>
</main>

<style>
</style>