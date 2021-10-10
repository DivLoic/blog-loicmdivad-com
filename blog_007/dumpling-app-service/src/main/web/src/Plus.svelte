<script>
    import {createEventDispatcher} from "svelte";
    import {API, ApiUrl} from "./stores/api.js"

    export let id;
    export let name;
    export let disabled;

    const route = "/command/add/"
    const dispatch = createEventDispatcher();

    const request = () => dispatch("request", {sent: true})
    const response = (data) => dispatch("response", {result: data})
    const publish = async () => API._command(id, name, $ApiUrl, route, "ADD")

    function post() {
        request()
        publish()
            .then((response) => {
                //response.ok
                return response.json()
            })
            .then((result) => response(result))
            .catch((err) => {
                console.log(err)
                response({id: -1, status: "error"})
            })
    }
</script>

<p>
    <button class="btn btn-success add-item" disabled={disabled} on:click={post} type="button">
        +
    </button>
</p>

<style>
    p {
        height: 100%;
        line-height: 3rem;
        text-align: center;
        margin-bottom: 0;
    }

    button:active {
        transition: 0.1s;
        color: #188754;
        border-color: #188754;
        background-color: white;
    }
</style>