import {readable} from "svelte/store";

export const ApiUrl = readable("__api_address__", function start(set) {

    return function stop() {
    };
});
const METHOD = "POST"
const HEADERS = {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
}
const MOCKED_SESSION = {
    user_id: "loicd10",
    table_id: "table11",
    store_id: "store3"
}
export const API = {

    _command: (id, name, url, route, operation) => fetch(new URL(route, url), {
        method: METHOD,
        headers: HEADERS,
        body: JSON.stringify({
                event_time: Date.now(),
                item_id: id,
                item_name: name,
                operation: operation,
                session: MOCKED_SESSION
            }
        )
    }),

    _toggle: (url, route, status) => fetch(new URL(route, url), {
        method: METHOD,
        headers: HEADERS,
        body: JSON.stringify({
                timestamp: Date.now(),
                extra_sauce: status,
                user_id: MOCKED_SESSION.user_id,
                table_id: MOCKED_SESSION.table_id,
                store_id: MOCKED_SESSION.store_id
            }
        )
    }),

    _checkout: (url, route) => fetch(new URL(route, url), {
        method: METHOD,
        headers: HEADERS,
        body: JSON.stringify({
                event_time: Date.now(),
                session: MOCKED_SESSION
            }
        )
    })
}