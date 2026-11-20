export interface PasaporteDTO {
    numSerie: string;
    modelo: string;
    fabricante: string;
    tipoComponente: string;
    estado: string;
    fechaRegistro: string;
    fechaFinGarantia: string;
    enGarantia: boolean;
}

export interface Componente {
    id: number;
    numSerie: string;
    modelo: string;
    fabricante: string;
    tipoComponente: string;
    estado: string;
    fechaRegistro: string;
}

export interface SolicitudRmaDTO {
    id: number;
    codigoRma: string;
    numSerieComponente: string;
    modeloComponente: string;
    emailUsuario: string;
    motivo: string;
    estado: string;
    fechaSolicitud: string;
    fechaActualizacion: string;
}

export interface UsuarioPerfil {
    id: number;
    nombre: string;
    email: string;
    rol: 'CLIENTE' | 'TECNICO' | 'ADMIN';
}