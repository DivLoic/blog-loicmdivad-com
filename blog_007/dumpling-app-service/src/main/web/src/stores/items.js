import {readable} from 'svelte/store';

export const items = readable([
    {
        id: 1,
        name: "Shui Jiao",
        price: 5,
        picture: "https://dumplingschool.com/wp-content/uploads/2019/01/shui-jiao-dumpling-768x576.jpg"
    },
    {
        id: 2,
        name: "Xiao Long Bao",
        price: 2,
        picture: "https://dumplingschool.com/wp-content/uploads/2019/01/shutterstock_577219102-768x512.jpg"
    },
    {
        id: 3,
        name: "Guo Tie",
        price: 10,
        picture: "https://dumplingschool.com/wp-content/uploads/2019/01/shutterstock_134661824-768x512.jpg"
    },
    {
        id: 4,
        name: "Wonton",
        price: 15,
        picture: "https://dumplingschool.com/wp-content/uploads/2019/01/shutterstock_193733918-768x512.jpg"
    },
], function start(set) {
    // implementation goes here

    return function stop() {
    };
});
