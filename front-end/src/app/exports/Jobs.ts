export interface Jobs{
    id?: number;
    isim: string;
    aciklama: string;
    status: number;
    point: number;
    deadline: Date;
    parent: number;
}