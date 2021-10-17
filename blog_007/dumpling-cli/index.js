const subscriptionName = 'projects/loicmdivad/subscriptions/test-command';
const timeout = 60;

import {PubSub} from '@google-cloud/pubsub';
import {Command, Format} from './lib/proto.js';
import {startSpinner, initSpinner, stopSpinner} from './lib/spinner.js';

const spinner = initSpinner();

const pubSubClient = new PubSub();

var Records = [];

const oldRecords = {
    "event_time": "...",
    "item_id": "...",
    "item_name": "...",
    "operation": "...",
    "session": "..."
}
function format(event) {
   return  {
        "event_time": Format.date(event),
        "item_id": event["item_id"],
        "item_name": event["item_name"],
        "operation": Format.operation(event),
        "session": event["session"]["table_id"]
    }
}

function printTable(records) {
    console.clear();

    if (records.length < 9) console.table(records)
    else console.table([oldRecords, ...records.slice(-8)])
}

function listenForMessages() {
    const subscription = pubSubClient.subscription(subscriptionName);

    const messageHandler = message => {
        Records.push(format(Command.decode(message.data)));
        stopSpinner(spinner);
        printTable(Records);
        startSpinner(spinner);
        message.ack();
    };

    subscription.on('message', messageHandler);
}

console.clear();
startSpinner(spinner);
listenForMessages();