export interface GameDTOCover{
    url?: string;
}


export interface GenreDTO{
    id: number;
    name: string;
}

export interface GameDTO{
    id?: number;
    name?: string;
    rating?: number;
    summary?: string;
    cover?: GameDTOCover;
    coverUrl?: string;
    genres?: GenreDTO[];
}