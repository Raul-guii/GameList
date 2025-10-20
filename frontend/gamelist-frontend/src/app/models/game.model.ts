export interface GameDTOCover{
    url?: string;
}

export interface GameDTO{
    id?: number;
    name?: string;
    rating?: number;
    summary?: string;
    cover?: GameDTOCover;
    coverUrl?: string;
    genres?: number[];
    genresNames?: string[];
}