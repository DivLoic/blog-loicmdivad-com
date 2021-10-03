<script>
    import {createEventDispatcher} from 'svelte';
    import {items} from "./stores/items.js";
    import Plus from "./Plus.svelte";
    import Minus from "./Minus.svelte";

    export let id;
    export let number;
    export let enable;

    const dispatch = createEventDispatcher();
    const addReq = () => dispatch("add-req", {"id": id})
    const delReq = () => dispatch("del-req", {"id": id})
    const addRes = (event) => dispatch("add-res", {"result": event.detail.result})
    const delRes = (event) => dispatch("del-res", {"result": event.detail.result})

    function getItem(id) {
        return $items.find((item) => item.id === id)
    }

    $: disabled = !enable || (number === 0)

</script>
<div class="row item">
    <div class="col">
        <img src={getItem(id).picture} alt={getItem(id).name}/>
    </div>
    <div class="col-2">
        <p class="text-truncate">{getItem(id).name}</p>
    </div>
    <div class="col">
        <p class="money text-truncate">{getItem(id).price} €</p>
    </div>
    <div class="col">
        <Minus id={id}
               name={getItem(id).price}
               disabled={disabled}
               on:request={delReq}
               on:response={delRes}/>
    </div>
    <div class="col num-item">
        <p>{number}</p>
    </div>
    <div class="col">
        <Plus id={id}
              name={getItem(id).price}
              disabled={!enable}
              on:request={addReq}
              on:response={addRes}/>
    </div>
</div>

<style>
    img {
        height: 3rem;
        width: 3rem;
        object-fit: cover;
        border: 0.05em solid darkgray;
        border-radius: 15%;
    }

    p {
        height: 100%;
        font-size: 0.8rem;
        color: dimgrey;
        line-height: 3rem;
        text-align: center;
        margin-bottom: 0;
    }

    p.money {
        overflow: visible;
    }

    div.item {
        height: 3rem;
        margin-bottom: 1.5rem;
    }

    div.item div.col {
        height: 100%;
        display: inline-block;
        vertical-align: middle;
        line-height: normal;
    }
</style>