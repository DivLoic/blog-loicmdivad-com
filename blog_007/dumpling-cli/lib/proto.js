import protobuf from "protobufjs";

var Root  = protobuf.Root,
    Type  = protobuf.Type,
    Enum = protobuf.Enum,
    Field = protobuf.Field;

const OperationMessage = new Enum("Operation", {"ADD": 0, "REMOVE": 1})

const SessionMessage = new Type("Session")
    .add(new Field("user_id", 1, "string"))
    .add(new Field("table_id", 2, "string"))
    .add(new Field("store_id", 3, "string"));

const CommandMessage = new Type("Command")
    .add(new Field("event_time", 1, "int64"))
    .add(new Field("item_id", 2, "string"))
    .add(new Field("item_name", 3, "string"))
    .add(new Field("operation", 4, "fr.ldivad.dumpling.Operation"))
    .add(new Field("session", 5, "fr.ldivad.dumpling.Session"))

const root = new Root()
    .define("fr.ldivad.dumpling").add(OperationMessage)
    .define("fr.ldivad.dumpling").add(SessionMessage)
    .define("fr.ldivad.dumpling").add(CommandMessage);

export let Command = root.lookupType("fr.ldivad.dumpling.Command").ctor;

export const Format = {
    operation: (cmd) => OperationMessage.valuesById[cmd["operation"]],
    date: (cmd) => new Date(parseInt(cmd["event_time"])).toLocaleString("en-GB"),
}

