<script>
    import {API, ApiUrl} from "./stores/api.js"

    let extra;
    let modal;
    let errorMessage;
    export let enabled;
    const route = "/sauce/toggle/"

    const publish = async (value) => API._toggle($ApiUrl, route, value)

    function toggleSauce(event) {
        console.log(event.target.checked)
        publish(event.target.checked)
            .then((response) => {
                if (response.ok) console.log("This should never be ok!")
                return response.json()
            })
            .then((bodyResponse) => {
                if (bodyResponse.success) return;
                console.log("As expected, it fails: " + bodyResponse.comment)
                errorMessage = bodyResponse.comment
                modal.click()
            })
            .catch((err) => {
                console.log(err)
            })
    }
</script>
<button type="button" data-bs-toggle="modal" data-bs-target="#exampleModal" style="display:none;"
        bind:this={modal}></button>
<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">💥 Error, sorry the sauces are
                    unavailable.</h5>
                <button on:click={() => extra = false} type="button" class="btn-close"
                        data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <p>
                    An unexpected error occurred: ${errorMessage}
                <p>
            </div>
            <div class="modal-footer">
                <button on:click={() => extra = false} type="button" class="btn btn-secondary"
                        data-bs-dismiss="modal">Close
                </button>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <div>
        <div id="sauce">
            <div class="row">
                <div class="col-8 col-sm-10">
                    <p class="h6">Extra sauce?</p>
                </div>
                <div class="col-4  col-sm-2">
                    <ul class="tg-list">
                        <li class="tg-list-item">
                            <input on:change={toggleSauce}
                                   bind:checked={extra}
                                   disabled="{!enabled}"
                                   title="toggle-sauce"
                                   class="tgl tgl-ios" id="tg1" type="checkbox"/>
                            <label class="tgl-btn" for="tg1"></label>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    p.h6 {
        line-height: 2.5em;
        color: dimgrey;
    }

    div#sauce {
        flex: 1 1 auto;
        padding: 1rem 1rem;
    }

    div.modal-body{
        color: dimgrey;
        font-size: 0.8rem;
        word-break: break-all;
    }

    ul {
        list-style: none;
        margin: 0.5em 0;
        padding: 0;
    }

    li {
        list-style: none;
        margin: 0;
        padding: 0;
    }

    .tg-list {
        text-align: center;
        display: flex;
        align-items: center;
    }

    .tg-list-item {
        margin: 0 /*0.5em*/;
    }

    .tgl {
        display: none;
    }

    .tgl,
    .tgl:after,
    .tgl:before,
    .tgl *,
    .tgl *:after,
    .tgl *:before,
    .tgl + .tgl-btn {
        box-sizing: border-box;
    }

    .tgl::-moz-selection,
    .tgl:after::-moz-selection,
    .tgl:before::-moz-selection,
    .tgl *::-moz-selection,
    .tgl *:after::-moz-selection,
    .tgl *:before::-moz-selection,
    .tgl + .tgl-btn::-moz-selection {
        background: none;
    }

    .tgl::selection,
    .tgl:after::selection,
    .tgl:before::selection,
    .tgl *::selection,
    .tgl *:after::selection,
    .tgl *:before::selection,
    .tgl + .tgl-btn::selection {
        background: none;
    }

    .tgl + .tgl-btn {
        outline: 0;
        display: block;
        width: 4em;
        height: 2em;
        position: relative;
        cursor: pointer;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
    }

    .tgl + .tgl-btn:after,
    .tgl + .tgl-btn:before {
        position: relative;
        display: block;
        content: "";
        width: 50%;
        height: 100%;
    }

    .tgl + .tgl-btn:after {
        left: 0;
    }

    .tgl + .tgl-btn:before {
        display: none;
    }

    .tgl:checked + .tgl-btn:after {
        left: 50%;
    }

    .tgl-ios + .tgl-btn {
        background: #fbfbfb;
        border-radius: 2em;
        padding: 2px;
        transition: all 0.4s ease;
        border: 1px solid #e8eae9;
    }

    .tgl-ios + .tgl-btn:after {
        border-radius: 2em;
        background: #fbfbfb;
        transition: left 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275), padding 0.3s ease, margin 0.3s ease;
        box-shadow: 0 0 0 1px rgba(0, 0, 0, 0.1), 0 4px 0 rgba(0, 0, 0, 0.08);
    }

    .tgl-ios + .tgl-btn:hover:after {
        will-change: padding;
    }

    .tgl-ios + .tgl-btn:active {
        box-shadow: inset 0 0 0 2em #e8eae9;
    }

    .tgl-ios + .tgl-btn:active:after {
        padding-right: 0.8em;
    }

    .tgl-ios:checked + .tgl-btn {
        background: #188754;
    }

    .tgl-ios:checked + .tgl-btn:active {
        box-shadow: none;
    }

    .tgl-ios:checked + .tgl-btn:active:after {
        margin-left: -0.8em;
    }

    ul.tg-list li.tg-list-item {
        width: 100%;
    }

    ul.tg-list li.tg-list-item label {
        margin-left: 0; /*40%;*/
    }
</style>