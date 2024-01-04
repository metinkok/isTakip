import { Users } from "./Users";

export interface Messages{
    id?: number;
    message: string;
    user_id: number;
    user: Users;
}