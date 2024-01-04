export interface Response {
    LocalDateTime: Date;
    httpStatus: string;
    reason: string;
    message: string;
    id: number;
    data: Array<Object>
}