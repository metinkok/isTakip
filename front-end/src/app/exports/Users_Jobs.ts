import { Jobs } from "./Jobs";
import { Users } from "./Users";

export interface Users_Jobs{
    id?: number;
    user: Users;
    job: Jobs;
}