import { Jobs } from "./Jobs";
import { Messages } from "./Messages";

export interface Messages_Jobs{
    id?: number;
    message: Messages;
    job: Jobs;
}