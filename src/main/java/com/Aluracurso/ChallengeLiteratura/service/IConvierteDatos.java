package com.Aluracurso.ChallengeLiteratura.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}

