
export interface Game {
    id: number;
    name: string;
    summary?: string;
    coverUrl?: string | null;
    rating?: number;
    genres?: number[] | string[];

}