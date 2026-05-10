export interface ComunidadResponse {
    id: number;
    nombre: string;
    descripcion: string;
    imagenUrl: string;
    generos: string[];
    propietarioId: number;
    propietarioNombre: string;
    fechaCreacion: string;
    numeroMiembros: number;
    esMiembro: boolean;
}

export interface ComunidadRequest {
    nombre: string;
    descripcion: string;
    generos: string[];
}

export interface UsuarioResponse {
    id: number;
    nombre: string;
    email: string;
    avatarUrl: string;
    fechaRegistro: string;
}

export interface MensajeChatResponse {
    id: number;
    comunidadId: number;
    autorId: number;
    autorNombre: string;
    autorAvatarUrl: string;
    contenido: string;
    fechaPublicacion: string;
}

export interface MensajeChatRequest {
    contenido: string;
}

export interface Page<T> {
    content: T[];
    pageable: any;
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
    empty: boolean;
}
