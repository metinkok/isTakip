import { Messages } from "./Messages";
import { Users } from "./Users";

export interface Messages_Users{
    id?: number;
    message: Messages;
    user: Users;
}