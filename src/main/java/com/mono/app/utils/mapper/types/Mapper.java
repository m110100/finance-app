package com.mono.app.utils.mapper.types;

/**
 * Интерфейс для преобразования между DAO и DTO.
 *
 * @param <T> Тип объекта DAO.
 * @param <V> Тип объекта DTO - Response.
 */
public interface Mapper<T, V> {
    /**
     * Преобразует DAO в DTO.
     *
     * @param dao Объект DAO для преобразования.
     * @return Преобразованный объект DTO.
     */
    V toDTO(T dao);
}
