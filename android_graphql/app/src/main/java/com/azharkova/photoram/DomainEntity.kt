package com.azharkova.photoram

interface DomainEntityMapper<E, D> {

    fun toEntity(d: D): E

    fun toDomain(e: E): D

}