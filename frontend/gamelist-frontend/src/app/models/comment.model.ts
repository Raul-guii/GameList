export interface Comment {

    id: number;

    content: string;

    username: string;

    createdAt: string;

    updatedAt?: string;
}

export interface CreateCommentRequest {
    content: string;
}

export interface UpdateCommentRequest {
    content: string;
}